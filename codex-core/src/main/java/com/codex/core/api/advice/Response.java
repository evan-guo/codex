package com.codex.core.api.advice;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Schema(title = "msg", description = "响应信息")
    private String msg;

    /**
     * 响应的具体数据
     */
    @Schema(title = "data", description = "响应数据")
    private T data;

    public Response(T data) {
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        this.msg = ErrorCodeEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}