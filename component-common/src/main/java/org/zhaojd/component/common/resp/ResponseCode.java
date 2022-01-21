package org.zhaojd.component.common.resp;

/**
 * 响应编码枚举
 *
 * @author jdzhao2
 */

public enum ResponseCode {
    /**
     * 状态码1000表示操作成功，1001-1999表示操作成功并附加其他提示信息
     */
    SUCCESS(1000, "处理成功"),
    
    /**
     * 状态码2000表示系统异常，2001-2999表示系统异常并附加其他提示信息
     */
    FAIL(2000, "系统异常"),
    
    PAGE_NOT_FOUND(2001, "页面不存在或不支持该http方法"),
    
    ILLEGAL_METHOD(2002, "非法的访问方法"),
    
    AUTH_EXCEPTION(2003, "无权限"),
    
    PARAMS_ERROR(2004, "参数解析错误，请检查是否存在参数结构错误、漏传必填参数、参数类型错误、错用全角半角字符等情况"),
    
    SERVICE_NOT_AVAILABLE(2005, "服务暂不可用"),
    
    SPEL_FAIL(2008, "验证失败"),
    
    /**
     * 状态码3000表示业务异常，3001-3999表示业务异常并附加其他提示信息
     */
    BUSINESS_EXCEPTION(3000, "业务异常"),
    
    TRAINING_NOT_FOUND(3001, "培训计划不存在"),
    ;
    
    private final int code;
    
    private final String desc;
    
    /**
     * @param code 响应码
     * @param desc 响应描述
     */
    private ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static ResponseCode getByCode(int code) {
        ResponseCode s = FAIL;
        for (ResponseCode e : values()) {
            if (e.code == code) {
                s = e;
                break;
            }
        }
        return s;
    }
}
