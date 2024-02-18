package com.codex.core.api.advice;

import com.codex.core.CodexAutoConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author gw
 * RestControllerAdvice 既可以全局拦截异常也可拦截指定包下正常的返回值，可以对返回值进行修改。<p>
 * basePackages 指定要拦截那个package下的Controller
 */
@RestControllerAdvice(basePackageClasses = {SpringApplication.class, CodexAutoConfiguration.class})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 对那些方法需要包装，如果接口直接返回Response就没有必要再包装了
     *
     * @param returnType
     * @param aClass
     * @return 如果为true才会执行beforeBodyWrite
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        NotResponseWrap classNoResponseWrap = returnType.getDeclaringClass().getAnnotation(NotResponseWrap.class);
        return !(returnType.getParameterType().equals(Response.class) || returnType.hasMethodAnnotation(NotResponseWrap.class) || classNoResponseWrap != null) ;
    }

    @ResponseStatus(HttpStatus.OK)
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        //不包装MediaType是application/xml类型的
        if (mediaType.equals(MediaType.APPLICATION_XML)) {
            return data;
        }
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在Response里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new Response<>(data));
            } catch (JsonProcessingException e) {
                throw new ApiException(ErrorCodeEnum.ERROR);
            }
        }
        // 这里统一包装
        return new Response<>(data);
    }

}