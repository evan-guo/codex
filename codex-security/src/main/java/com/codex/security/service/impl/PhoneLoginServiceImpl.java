package com.codex.security.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.security.captcha.sms.SmsCaptchaProcessor;
import com.codex.security.model.form.UserLoginForm;
import com.codex.security.properties.CodexSecurityProperties;
import com.codex.security.properties.SecurityCacheKey;
import com.codex.security.service.LoginService;
import com.codex.security.authentication.sms.SmsAuthenticationToken;
import com.codex.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author evan guo
 * @since 2023-01-16
 * 手机验证码登录实现
 */
@RequiredArgsConstructor
@Service
public class PhoneLoginServiceImpl implements LoginService {

    private final SmsCaptchaProcessor smsCaptchaProcessor;
    private final AuthenticationManager authenticationManager;
    private final CodexSecurityProperties codexSecurityProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String login(UserLoginForm form) {
        ValidationUtil.validateThrow(form, UserLoginForm.LoginByValidateCode.class);
        smsCaptchaProcessor.validate(form.getUsername(), form.getCode());
        // 进行用户认证, 获取认证对象
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(form.getUsername());
        // 认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证失败
        if (Objects.isNull(authenticate)){
            throw new SecurityException("登录认证失败");
        }
        // 准备生成Token的参数
        JSONObject payload  = new JSONObject();
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, (int) codexSecurityProperties.getToken().getDuration().toMinutes());
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 自定义参数
        UserDetails user = (UserDetails) authenticate.getPrincipal();
        String username = user.getUsername();
        payload.put(JWTPayload.SUBJECT, username);
        // 生成Token
        String token = JWTUtil.createToken(payload, codexSecurityProperties.getToken().getSecret().getBytes());
        // 相关信息存入Redis
        redisTemplate.opsForValue().set(SecurityCacheKey.OAUTH_TOKEN + token, username, codexSecurityProperties.getToken().getDuration());
        redisTemplate.opsForValue().set(SecurityCacheKey.OAUTH_USER + username, user, codexSecurityProperties.getToken().getDuration());
        return token;
    }

}
