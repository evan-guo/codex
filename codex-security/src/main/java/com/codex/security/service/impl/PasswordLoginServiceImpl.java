package com.codex.security.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.security.constant.constant.CacheConstant;
import com.codex.security.model.form.UserLoginForm;
import com.codex.security.properties.LoginTokenProperties;
import com.codex.security.service.LoginService;
import com.codex.security.util.RedisUtil;
import com.codex.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author guowei
 * @since 2023-01-16
 * 账号密码登录实现
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordLoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final LoginTokenProperties loginTokenProperties;

    @Override
    public String login(UserLoginForm form) {
        ValidationUtil.validateThrow(form, UserLoginForm.LoginByPassword.class);
        // 进行用户认证, 获取认证对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword());
        // 认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证失败
        if (Objects.isNull(authenticate)){
            throw new SecurityException("登录失败");
        }
        // 准备生成Token的参数
        JSONObject payload  = new JSONObject();
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, (int) loginTokenProperties.getDuration().toMinutes());
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 自定义参数
        UserDetails user = (UserDetails) authenticate.getPrincipal();
        String username = user.getUsername();
        payload.put("username", username);
        // 生成Token
        String token = JWTUtil.createToken(payload, loginTokenProperties.getSecret().getBytes());
        // 相关信息存入Redis
        RedisUtil.set(CacheConstant.LOGIN_TOKEN + token, username);
        RedisUtil.set(CacheConstant.LOGIN_USER + username, user);
        return token;
    }
}
