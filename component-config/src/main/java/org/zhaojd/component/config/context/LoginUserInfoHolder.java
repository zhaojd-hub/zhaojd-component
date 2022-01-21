package org.zhaojd.component.config.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LoginUserInfoHolder {
    
    public static LoginUserInfo getCurrentUser() {
        // 从Header中获取用户信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String userId = request.getHeader("userId");
        String tenantId = request.getHeader("tenantId");
        String clientCode = request.getHeader("clientCode");
        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setUserId(userId == null ? null : Long.parseLong(userId));
        userInfo.setTenantId(tenantId == null ? null : Long.parseLong(tenantId));
        userInfo.setClientCode(clientCode);
        return userInfo;
    }
}
