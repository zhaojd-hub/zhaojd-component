package org.zhaojd.component.common.exception;

import org.zhaojd.component.common.resp.ResponseCode;

/**
 * @author jdzhao2
 */
public abstract class AbstractBusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private Integer responseCode = null;
    
    public AbstractBusinessException() {
        super();
    }
    
    public AbstractBusinessException(String msg) {
        super(msg);
    }
    
    public AbstractBusinessException(String msg, Integer responseCode) {
        super(msg);
        this.responseCode = responseCode;
    }
    
    public AbstractBusinessException(String msgTemplate, Object[] objs) {
        super(String.format(msgTemplate, objs));
    }
    
    public AbstractBusinessException(Exception e) {
        super(e);
    }
    
    public AbstractBusinessException(String msg, Exception e) {
        super(msg, e);
    }
    
    
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
    
    /**
     * 子类覆盖此方法返回不同的业务异常代码
     */
    public int getResponseCode() {
        if (null == responseCode) {
            responseCode = ResponseCode.BUSINESS_EXCEPTION.getCode();
        }
        return responseCode;
    }
}