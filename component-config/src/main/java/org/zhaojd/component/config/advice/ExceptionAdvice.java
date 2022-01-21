package org.zhaojd.component.config.advice;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.zhaojd.component.common.exception.DefaultBusinessException;
import org.zhaojd.component.common.resp.ResponseCode;
import org.zhaojd.component.common.resp.ResponseResult;

@RestControllerAdvice
public class ExceptionAdvice {
    
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);
    
    /**
     * 未列举处的异常处理
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> errorHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ResponseCode.FAIL.getCode(), "系统异常: " + ex.getClass() + ": " + ex.getMessage());
    }
    
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult<Object> pageNotFoundHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ResponseCode.PAGE_NOT_FOUND);
    }
    
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, HttpMediaTypeException.class,
            TypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> paramsErrorHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ResponseCode.PARAMS_ERROR);
    }
    
    @ExceptionHandler(value = SpelEvaluationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> spelEvaluationHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ResponseCode.SPEL_FAIL);
    }
    
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Object> sqlExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ResponseCode.FAIL.getCode(), ex.getMessage());
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseResult<Object> bodyValidExceptionHandler(Exception exception) {
        log.error(exception.getMessage(), exception);
        
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) exception;
            List<FieldError> fieldErrors = validException.getBindingResult().getFieldErrors();
            log.error(fieldErrors.get(0).getField() + fieldErrors.get(0).getDefaultMessage());
            return ResponseResult.fail(ResponseCode.PARAMS_ERROR.getCode(), fieldErrors.get(0).getDefaultMessage());
        }
        return ResponseResult.fail(ResponseCode.PARAMS_ERROR.getCode(), exception.getMessage());
    }
    
    /**
     * 项目自定义的异常处理
     */
    @ExceptionHandler(value = DefaultBusinessException.class)
    public ResponseResult<Object> businessExceptionHandler(DefaultBusinessException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseResult.fail(ex.getResponseCode(), ex.getMessage());
    }
}
