package org.zhaojd.component.common.resp;


import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.zhaojd.component.common.exception.AbstractBusinessException;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author jdzhao2
 */
@ApiModel("统一返回数据结果")
public class ResponseResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("返回码，1000表示请求成功")
    private int code;
    
    @ApiModelProperty("返回信息，请求失败时为失败原因")
    private String message;
    
    @ApiModelProperty("返回数据")
    private T data;
    
    public ResponseResult() {
        this.code = ResponseCode.SUCCESS.getCode();
        this.message = ResponseCode.SUCCESS.getDesc();
    }
    
    public ResponseResult(AbstractBusinessException exception) {
        this.code = exception.getResponseCode();
        this.message = exception.getMessage();
    }
    
    public ResponseResult(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public static <T> ResponseResult<T> success() {
        return success(null);
    }
    
    public static <T> ResponseResult<T> success(T t) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setData(t);
        result.setMessage(ResponseCode.SUCCESS.getDesc());
        result.setCode(ResponseCode.SUCCESS.getCode());
        return result;
    }
    
    public static <T> ResponseResult<T> fail(String message) {
        return fail(ResponseCode.BUSINESS_EXCEPTION.getCode(), message, null);
    }
    
    public static <T> ResponseResult<T> fail(ResponseCode responseCode) {
        return fail(responseCode.getCode(), responseCode.getDesc(), null);
    }
    
    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return fail(code, message, null);
    }
    
    public static <T> ResponseResult<T> fail(Integer code, String message, T t) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(t);
        return result;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    @JsonIgnore
    public boolean isSuccess() {
        return Objects.equals(this.getCode(), ResponseCode.SUCCESS.getCode());
    }
}
