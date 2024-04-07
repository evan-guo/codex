package com.codex.security.service;

import com.codex.security.form.form.UserRegisterForm;

/**
 * @author evan guo
 * @since 2023-03-25
 * 注册服务
 */
public interface RegisterService {

    /**
     * 注册
     * @param form  注册参数
     */
    void register(UserRegisterForm form);

}
