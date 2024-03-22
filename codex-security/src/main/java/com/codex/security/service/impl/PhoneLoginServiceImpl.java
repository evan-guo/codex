package com.codex.security.service.impl;

import com.codex.security.model.form.UserLoginForm;
import com.codex.security.service.LoginService;
import com.codex.security.util.ValidationUtil;
import org.springframework.stereotype.Service;

/**
 * @author guowei
 * @since 2023-01-16
 * 手机验证码登录实现
 */

@Service
public class PhoneLoginServiceImpl implements LoginService {

    @Override
    public String login(UserLoginForm form) {
        ValidationUtil.validateThrow(form, UserLoginForm.LoginByValidateCode.class);
        return null;
    }

}
