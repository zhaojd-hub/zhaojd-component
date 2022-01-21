package org.zhaojd.component.config.context;

import java.io.Serializable;

public class LoginUserInfo implements Serializable {
    
    private static final long serialVersionUID = 7951365520177084485L;
    
    private Long userId;
    
    private Long tenantId;
    
    private String clientCode;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getClientCode() {
        return clientCode;
    }
    
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }
}
