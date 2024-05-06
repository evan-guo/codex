//package com.codex.admin.user.entity;
//
//import com.codex.admin.base.entity.BaseEntity;
//import com.codex.annotation.Codex;
//import com.codex.annotation.CodexField;
//import com.mybatisflex.annotation.Table;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//
///**
// * 用户
// *
// * @author evan guo
// */
//@Codex
//@Table("sys_user")
//@EqualsAndHashCode(callSuper = true)
//@Data
//public class User extends BaseEntity implements UserDetails {
//
//    @CodexField(name = "用户姓名")
//    private String name;
//
//    @CodexField(name = "用户账号")
//    private String username;
//
//    @CodexField(name = "用户密码")
//    private String password;
//
//    @CodexField(name = "手机号码")
//    private String mobile;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
