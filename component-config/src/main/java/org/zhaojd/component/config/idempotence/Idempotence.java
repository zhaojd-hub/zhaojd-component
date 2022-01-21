package org.zhaojd.component.config.idempotence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等性API接口处理注解，用于避免接口短时间内重复请求，要求在已登陆平台的情况下才生效（request header中有userId 或者Authorization）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotence {
    
    /**
     * 设置请求锁定时间，限定时间内，只能请求一次
     */
    int lockTime() default 2;
}
