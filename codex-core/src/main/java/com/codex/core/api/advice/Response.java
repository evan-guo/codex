package com.codex.core.api.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gw
 * 定义数据返回格式
 */
@Data
@Schema(description = "响应返回数据对象")
public class Response<T> implements Serializable {
    /**
     * 状态码，比如0代表响应成功
     */
    @Schema(title = "code", description = "响应码", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    /**
     * 响应信息，用来说明响应情况
     */
    @Schema(title = "message", description = "响应信息")
    private String message;

    /**
     * 响应的具体数据
     */
    @Schema(title = "result", description = "响应数据")
    private T result;

    public Response(T result) {
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        this.message = ErrorCodeEnum.SUCCESS.getMsg();
        this.result = result;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

}