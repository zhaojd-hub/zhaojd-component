package org.zhaojd.component.sc.config.sc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author jdzhao2
 */
public class ExampleRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {
    
    public ExampleRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }
    
    public ExampleRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
            List<Object> requestResponseBodyAdvice) {
        super(converters, requestResponseBodyAdvice);
    }
    
    public ExampleRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
            ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
        super(converters, manager, requestResponseBodyAdvice);
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (AnnotatedElementUtils.hasAnnotation(parameter.getContainingClass(), FeignClient.class)
                && isCustomizedType(parameter.getParameterType())) {
            return true;
        }
        return super.supportsParameter(parameter);
    }
    
    private boolean isCustomizedType(Class<?> cls) {
        return cls.getName().startsWith("org.zhaojd");
    }
    
}
