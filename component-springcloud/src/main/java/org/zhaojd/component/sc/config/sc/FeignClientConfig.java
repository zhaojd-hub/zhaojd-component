package org.zhaojd.component.sc.config.sc;

import java.util.TimeZone;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;

/**
 * @author jdzhao2
 */
@Configuration
public class FeignClientConfig implements FeignFormatterRegistrar {
    
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    
    @Override
    public void registerFormatters(FormatterRegistry registry) {
        DateFormatter dateFormatter = new DateFormatter(DATE_PATTERN);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        registry.addFormatter(dateFormatter);
    }
}
