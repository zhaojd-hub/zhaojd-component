package org.zhaojd.component.sc.config.apollo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import com.ctrip.framework.apollo.core.utils.ResourceUtils;
import com.google.common.base.Strings;

/**
 * @author jdzhao2
 */
public class ExampleMetaServerProvider implements MetaServerProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleMetaServerProvider.class);
    
    public static final int ORDER = MetaServerProvider.LOWEST_PRECEDENCE - 2;
    
    private static final Map<Env, String> domains = new HashMap<>();
    
    public ExampleMetaServerProvider() {
        initialize();
    }
    
    private void initialize() {
        Properties prop = new Properties();
        prop = ResourceUtils.readConfigFile("apollo-metas.properties", prop);
        
        domains.put(Env.UNKNOWN, getMetaServerAddress(prop, "default_meta", "default.meta"));
        domains.put(Env.LOCAL, getMetaServerAddress(prop, "local_meta", "local.meta"));
        domains.put(Env.DEV, getMetaServerAddress(prop, "dev_meta", "dev.meta"));
        domains.put(Env.FAT, getMetaServerAddress(prop, "fat_meta", "fat.meta"));
        domains.put(Env.UAT, getMetaServerAddress(prop, "uat_meta", "uat.meta"));
        domains.put(Env.LPT, getMetaServerAddress(prop, "lpt_meta", "lpt.meta"));
        domains.put(Env.PRO, getMetaServerAddress(prop, "pro_meta", "pro.meta"));
    }
    
    private String getMetaServerAddress(Properties prop, String sourceName, String propName) {
        // 1. Get from System Property.
        String metaAddress = System.getProperty(sourceName);
        if (Strings.isNullOrEmpty(metaAddress)) {
            // 2. Get from OS environment variable, which could not contain dot and is normally in
            // UPPER case,like DEV_META.
            metaAddress = System.getenv(sourceName.toUpperCase());
        }
        if (Strings.isNullOrEmpty(metaAddress)) {
            // 3. Get from properties file.
            metaAddress = prop.getProperty(propName);
        }
        return metaAddress;
    }
    
    @Override
    public String getMetaServerAddress(Env targetEnv) {
        String metaServerAddress = domains.get(targetEnv);
        if (Strings.isNullOrEmpty(metaServerAddress)) {
            LOGGER.warn("Could not find meta server address, "
                    + "because it is not available in neither (1) JVM system property 'apollo.meta', "
                    + "(2) OS env variable 'APOLLO_META' "
                    + "(3) property 'apollo.meta' from server.properties nor "
                    + "(4) property 'apollo.meta' from app.properties "
                    + "(5) properties from apollo-metas.properties");
        } else {
            metaServerAddress = metaServerAddress.trim();
            LOGGER.info("Located meta services from apollo-metas configuration: {}!", metaServerAddress);
        }
        return metaServerAddress;
    }
    
    @Override
    public int getOrder() {
        return ORDER;
    }
}