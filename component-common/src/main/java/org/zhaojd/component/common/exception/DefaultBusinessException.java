package org.zhaojd.component.common.exception;

/**
 * @author jdzhao2
 */
public class DefaultBusinessException extends AbstractBusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public DefaultBusinessException(String msg) {
        super(msg);
    }
    
    public DefaultBusinessException(String msg, Integer responseCode) {
        super(msg, responseCode);
    }
    
    public DefaultBusinessException(String msg, Exception e) {
        super(msg, e);
    }
    
    public DefaultBusinessException(String msg, Object... objs) {
        super(msg, objs);
    }
    
    /**
     * 子类覆盖此方法返回不同的业务异常代码
     */
    @Override
    public int getResponseCode() {
        return super.getResponseCode();
    }
}