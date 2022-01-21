package org.zhaojd.component.sc.config.sc;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

/**
 * @author jdzhao2
 */
@Service
public class LoggerLevelRefresher implements ApplicationContextAware {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerLevelRefresher.class);
    
    private ApplicationContext applicationContext;
    
    @ApolloConfig
    private Config config;
    
    @PostConstruct
    private void initialize() {
        refreshLoggingLevels(config.getPropertyNames());
    }
    
    @ApolloConfigChangeListener(interestedKeyPrefixes = {"logging.level."})
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels(changeEvent.changedKeys());
    }
    
    private void refreshLoggingLevels(Set<String> changedKeys) {
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));
        LOGGER.info("Logging levels refreshed");
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}