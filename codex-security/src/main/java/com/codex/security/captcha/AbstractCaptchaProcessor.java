package com.codex.security.captcha;

import cn.hutool.core.util.StrUtil;
import com.codex.security.exception.CaptchaException;
import com.codex.security.properties.SecurityCacheKey;
import com.codex.security.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author guo_wei
 * @date 2023-01-13
 * 抽象验证码处理器，实现了一些共用的逻辑
 */
public abstract class AbstractCaptchaProcessor<C extends Captcha> implements CaptchaProcessor {

    /**
     * 收集系统中所有的 {@link CaptchaGenerator} 接口的实现。
     */
    @Autowired
    private Map<String, CaptchaGenerator> captchaGeneratorMap;

    /**
     * 发送验证码的接口方法，需要具体子类实现
     * @param account   账号
     * @param validateCode 验证码实例
     */
    protected abstract void send(String account, C validateCode);

    @Override
    public void create(String account) {
        C captcha = this.generate();
        this.save(account, captcha);
        this.send(account, captcha);
    }

    @Override
    public void validate(String account, String code) {
        if (StrUtil.isBlank(code)) {
            throw new CaptchaException("验证码的值不能为空");
        }
        // 查询验证码
        Captcha captcha = this.get(account);
        if (captcha == null) {
            throw new CaptchaException("验证码不存在");
        }
        if (captcha.isExpired()) {
            throw new CaptchaException("验证码已过期");
        }
        if (!StrUtil.equals(captcha.getCode(), code)) {
            throw new CaptchaException("验证码不匹配");
        }
        // 删除验证码
        this.del(account);
    }

    /**
     * 生成验证码
     */
    @SuppressWarnings("unchecked")
    private C generate() {
        String captchaType = StrUtil.subBefore(getClass().getSimpleName(), "CaptchaProcessor", true);
        String generatorName = captchaType + CaptchaGenerator.class.getSimpleName();
        CaptchaGenerator captchaGenerator = captchaGeneratorMap.get(generatorName);
        if (captchaGenerator == null) {
            throw new CaptchaException("验证码生成器" + generatorName + "不存在");
        }
        return (C) captchaGenerator.generate();
    }

    /**
     * 保存验证码
     * @param account 账号
     * @param validateCode  验证码
     */
    private void save(String account, C validateCode) {
        String captchaType = StrUtil.subBefore(getClass().getSimpleName(), "CaptchaProcessor", true);
        RedisUtil.hset(SecurityCacheKey.SECURITY_CAPTCHA + captchaType, account, validateCode);
    }

    /**
     * 查询验证码
     * @param account   账号
     * @return 验证码
     */
    private C get(String account) {
        String captchaType = StrUtil.subBefore(getClass().getSimpleName(), "CaptchaProcessor", true);
        return RedisUtil.hget(SecurityCacheKey.SECURITY_CAPTCHA + captchaType, account);
    }

    /**
     * 删除验证码
     * @param account   账号
     */
    private void del(String account) {
        String captchaType = StrUtil.subBefore(getClass().getSimpleName(), "CaptchaProcessor", true);
        RedisUtil.hdel(SecurityCacheKey.SECURITY_CAPTCHA + captchaType, account);
    }

}
