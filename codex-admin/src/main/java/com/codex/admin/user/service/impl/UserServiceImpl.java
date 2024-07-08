//package com.codex.admin.user.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.codex.admin.user.entity.User;
//import com.codex.admin.user.service.UserService;
//import com.codex.mapper.CodexMapper;
//import com.codex.security.form.UserRegisterForm;
//import com.codex.security.service.RegisterCustomizer;
//import com.mybatisflex.core.query.QueryWrapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * 用户相关服务实现
// *
// * @author evan guo
// */
//@RequiredArgsConstructor
//@Service
//public class UserServiceImpl implements UserService, UserDetailsService, RegisterCustomizer {
//
//    private final CodexMapper codexMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper queryWrapper = new QueryWrapper().eq(User::getUsername, username);
//        User user = codexMapper.selectOneByQuery(User.class, queryWrapper);
//        if (user == null) {
//            throw new SecurityException("账号密码错误");
//        }
//        return user;
//    }
//
//    @Override
//    public void customise(UserRegisterForm form) {
//        User user = new User();
//        BeanUtil.copyProperties(form, user);
//        BeanUtil.copyProperties(form.getExtraData(), user);
//        codexMapper.insert(User.class, user);
//    }
//
//
//}
