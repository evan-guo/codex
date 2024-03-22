package com.codex.security.controller;

import com.codex.security.captcha.CaptchaProcessor;
import com.codex.security.captcha.CaptchaProcessorFactory;
import com.codex.security.exception.CaptchaException;
import com.codex.security.model.form.UserLoginForm;
import com.codex.security.model.form.UserRegisterForm;
import com.codex.security.service.LoginService;
import com.codex.security.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author guowei
 * @since 2023-01-05
 * 用户相关接口
 */
@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final Map<String, LoginService> loginServiceMap;
    private final CaptchaProcessorFactory captchaProcessorFactory;
    private final Map<String, RegisterService> registerServiceMap;

    /**
     * 登录
     * @param mode 登录模式
     * @param form 登录表单参数
     */
    @PostMapping("/login/{mode}")
    public String login(@PathVariable String mode, @RequestBody UserLoginForm form) {
        String className = mode.toLowerCase() + LoginService.class.getSimpleName() + "Impl";
        LoginService loginService = loginServiceMap.get(className);
        if (loginService == null) {
            throw new CaptchaException("登录接口实现类" + className + "不存在");
        }
        return loginService.login(form);
    }

    /**
     * 发送验证码
     * @param type    验证码类型
     * @param account 账号
     */
    @GetMapping("/captcha/{type}")
    public void captcha(@PathVariable String type, @RequestParam String account) {
        CaptchaProcessor captchaProcessor = captchaProcessorFactory.findCaptchaProcessor(type);
        captchaProcessor.create(account);
    }

    /**
     * 注册
     * @param mode 注册模式
     * @param form 注册表单参数
     */
    @PostMapping("/register")
    public void register(@RequestParam(required = false) String mode, @RequestBody UserRegisterForm form) {
        RegisterService registerService;
        if (StringUtils.hasText(mode)) {
            String className = mode.toLowerCase() + RegisterService.class.getSimpleName() + "Impl";
            registerService = registerServiceMap.get(className);
            if (registerService == null) {
                throw new CaptchaException("注册接口实现类" + className + "不存在");
            }
        } else {
            registerService = registerServiceMap.get("default" + RegisterService.class.getSimpleName() + "Impl");
        }
        registerService.register(form);
    }

    /**
     * 注销
     */
    @GetMapping("/logout")
    public void logout() {
    }

}
