package com.codex.security.model.form;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author guowei
 * @since 2023-01-16
 * 用户注册参数
 */
@Data
public class UserRegisterForm {

    @NotBlank(message = "账号不能为空", groups = {UserLoginForm.LoginByValidateCode.class, UserLoginForm.LoginByPassword.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {UserLoginForm.LoginByPassword.class})
    private String password;

    @NotBlank(message = "验证码不能为空", groups = {UserLoginForm.LoginByValidateCode.class})
    private String captcha;

    private JSONObject extraData;

}
