package com.codex.security.config;

import com.codex.security.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author guowei
 * Redis配置类
 */
@Configuration
public class RedisConfig {
    @Lazy
    @Resource
    private RedisTemplate<String, Object> template;

    /**
     * 初始化了RedisManager，可以通过静态方式调用里面的方法
     */
    @Bean
    @DependsOn("redisTemplate")
    public RedisUtil redisManager() {
        return new RedisUtil(template);
    }

    /**
     * 定义了Redis的序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(lettuceConnectionFactory);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 采用JSON序列化
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.afterPropertiesSet();
        return template;
    }
}
