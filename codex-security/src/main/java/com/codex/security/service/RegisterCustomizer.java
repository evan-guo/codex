package com.codex.security.service;

import com.codex.security.model.form.UserRegisterForm;

/**
 * 用户注册自定义处理类
 *
 * @author evan guo
 */
public interface RegisterCustomizer {

    void customise(UserRegisterForm form);

}
