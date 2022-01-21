package org.zhaojd.component.config.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig implements WebMvcConfigurer {
    
    @Value("${spring.application.name}")
    private String appName;
    
    @Value("${system.enabledSwagger:true}")
    private Boolean enabledSwagger;
    
    @Value("${system.user-token.name:Authorization}")
    private String userTokenName;
    
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        if (Objects.nonNull(enabledSwagger) && enabledSwagger) {
            registry.addResourceHandler("swagger-ui.html", "doc.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
    
    @Bean
    public Docket api() {
        List<Parameter> paramList = new ArrayList<>();
        ParameterBuilder tokenParam = new ParameterBuilder();
        if (StringUtils.isNotBlank(userTokenName)) {
            tokenParam.name(userTokenName)
                    .description("令牌")
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build();
        }
        
        paramList.add(tokenParam.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .host("http://ip:port/lms")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build()
                .globalOperationParameters(paramList);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(appName)
                .description("后台接口文档")
                .version("1.0")
                .build();
    }
}
