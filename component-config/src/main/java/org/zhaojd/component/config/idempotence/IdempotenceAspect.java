package org.zhaojd.component.config.idempotence;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 幂等性处理的切面
 */
@Component
@Aspect
public class IdempotenceAspect {
    
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;
    
    
    @Pointcut("@annotation(idempotence)")
    public void pointCut(Idempotence idempotence) {
    }
    
    @Around(value = "pointCut(idempotence)", argNames = "pjp,idempotence")
    public Object around(ProceedingJoinPoint pjp, Idempotence idempotence) throws Throwable {
        int lockTime = idempotence.lockTime();
        
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(servletRequestAttributes).getRequest();
        
        // 用于唯一区分用户，优先取userId，再取token（适配开发环境直接访问和生产环境通过网关访问）
        String requestUserIdentify;
        if (Objects.nonNull(request.getHeader("userId"))) {
            requestUserIdentify = request.getHeader("userId");
        } else if (Objects.nonNull(request.getHeader("Authorization"))) {
            requestUserIdentify = request.getHeader("Authorization");
        } else {
            // 无法获取用户信息时直接放过
            return pjp.proceed();
        }
        
        String requestUri = request.getRequestURI();
        String key = "IDEMPOTENCE:" + requestUri + "-" + requestUserIdentify;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            throw new RuntimeException("操作太快了，请稍等下！");
        }
        stringRedisTemplate.opsForValue().set(key, "", lockTime, TimeUnit.SECONDS);
        Object result = pjp.proceed();
        // 请求结束后删除key
        stringRedisTemplate.delete(key);
        return result;
    }
    
}
