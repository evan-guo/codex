package com.codex.security.service;

import com.codex.security.model.form.UserLoginForm;

/**
 * @author guowei
 * @since 2023-01-05
 * 登录服务
 */
public interface LoginService {

    /**
     * 登录接口
     * @param form  参数
     * @return 返回Token
     */
    String login(UserLoginForm form);

}
