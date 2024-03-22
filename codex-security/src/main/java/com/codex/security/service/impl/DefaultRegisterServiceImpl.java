package com.codex.security.service.impl;

import com.codex.security.model.form.UserLoginForm;
import com.codex.security.model.form.UserRegisterForm;
import com.codex.security.service.RegisterCustomizer;
import com.codex.security.service.RegisterService;
import com.codex.security.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author guowei
 * @since 2023-03-25
 */
@RequiredArgsConstructor
@Service
public class DefaultRegisterServiceImpl implements RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final Optional<List<RegisterCustomizer>> registerCustomizers;

    @Override
    public void register(UserRegisterForm form) {
        ValidationUtil.validateThrow(form, UserLoginForm.LoginByPassword.class);
        String encodePassword = passwordEncoder.encode(form.getPassword());
        form.setPassword(encodePassword);
        registerCustomizers.ifPresent(customizers -> customizers.forEach((customizer) -> customizer.customise(form)));
    }

}
