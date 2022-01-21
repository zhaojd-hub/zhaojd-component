package org.zhaojd.component.sc.config.sc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author jdzhao2
 */
@Configuration
public class ExampleWebMvcRegistrations implements WebMvcRegistrations {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleWebMvcRegistrations.class);
    
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new FeignRequestMappingHandlerMapping();
    }
    
    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        
        @Override
        protected boolean isHandler(Class<?> beanType) {
            LOGGER.debug("Dealing with type = " + beanType.getName());
            
            LOGGER.debug("Has FeignClient Annotation ? " +
                    AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class));
            
            LOGGER.debug("Has RestController Annotation ? " +
                    AnnotatedElementUtils.hasAnnotation(beanType, RestController.class));
            
            return (AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class)
                    && AnnotatedElementUtils.hasAnnotation(beanType, RestController.class))
                    || (super.isHandler(beanType) && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class));
        }
    }
}