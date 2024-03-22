package com.codex.core.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

/**
 * 用于拦截所有的@RestController,增加对APIException的拦截，并修改异常时返回的数据格式。
 * 需要注意的是：基于@ControllerAdvice注解的全局异常统一处理只能针对于Controller层的异常，意思是只能捕获到Controller层的异常，在service层或者其他层面的异常都不能捕获。
 *
 * @author GW
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice {

    /**
     * 参数校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = "参数异常";
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            errorMessage = fieldErrors.get(0).getDefaultMessage();
        }
        log.error("参数校验异常: {}, from: {}", errorMessage, Objects.requireNonNull(e.getParameter().getMethod()));
        return new Response<>(ErrorCodeEnum.VALIDATE_FAILED.getCode(), errorMessage);
    }

    /**
     * 业务异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ApiException.class})
    public Response<Object> apiExceptionHandler(ApiException e) {
        log.error("handle ApiException", e);
        return new Response<>(e.getCode(), e.getMsg(), e.getData());
    }

    /**
     * 其他不可知异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Response<String> runtimeExceptionHandler(Exception e) {
        log.error("handle Exception, message: " + e.getMessage(), e);
        return new Response<>(ErrorCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}