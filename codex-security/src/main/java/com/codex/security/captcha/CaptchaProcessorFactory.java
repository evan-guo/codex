package com.codex.security.captcha;

import com.codex.security.exception.CaptchaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author guo_wei
 * @date 2023-01-13
 * 验证码处理器工厂
 */
@RequiredArgsConstructor
@Component
public class CaptchaProcessorFactory {

    private final Map<String, CaptchaProcessor> captchaProcessors;

    /**
     * 获取验证码处理器
     * @param type  处理器类型
     * @return  验证码处理器
     */
    public CaptchaProcessor findCaptchaProcessor(CaptchaType type) {
        return this.findCaptchaProcessor(type.toString());
    }

    /**
     * 获取验证码处理器
     * @param type  处理器类型
     * @return  验证码处理器
     */
    public CaptchaProcessor findCaptchaProcessor(String type) {
        String className = type.toLowerCase() + CaptchaProcessor.class.getSimpleName();
        CaptchaProcessor captchaProcessor = captchaProcessors.get(className);
        if (captchaProcessor == null) {
            throw new CaptchaException("验证码处理器" + className + "不存在");
        }
        return captchaProcessor;
    }

}
