package com.codex.core.api.advice;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author evan guo
 */
@Setter
@Getter
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 错误数据
     */
    private Object data;

    public ApiException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMsg());
        this.setCode(errorCodeEnum.getCode());
        this.setMsg(errorCodeEnum.getMsg());
    }

    public ApiException(String msg) {
        super(msg);
        this.setCode(ErrorCodeEnum.ERROR.getCode());
        this.setMsg(msg);
    }

    public ApiException(Integer code, String msg) {
        super(msg);
        this.setCode(code);
        this.setMsg(msg);
    }

    public ApiException(ErrorCodeEnum errorCodeEnum, Object data) {
        super(errorCodeEnum.getMsg());
        this.setCode(errorCodeEnum.getCode());
        this.setMsg(errorCodeEnum.getMsg());
        this.setData(data);
    }

    public ApiException(Integer code, String msg, Object data) {
        super(msg);
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

}