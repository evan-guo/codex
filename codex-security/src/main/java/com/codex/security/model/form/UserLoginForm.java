package com.codex.security.model.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author guowei
 * @since 2023-01-05
 * 用户登录表单参数
 */
@Data
public class UserLoginForm {

    @NotBlank(message = "账号不能为空", groups = {LoginByValidateCode.class, LoginByPassword.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {LoginByPassword.class})
    private String password;

    @NotBlank(message = "验证码不能为空", groups = {LoginByValidateCode.class})
    private String validateCode;

    /**
     * 账号密码登录
     */
    public interface LoginByPassword {}

    /**
     * 验证码登录
     */
    public interface LoginByValidateCode {}

}
