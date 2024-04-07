package com.codex.security.captcha;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author evan guo
 * @since 2023-01-13
 * 验证码抽象类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Captcha implements Serializable {

    private static final long serialVersionUID = 1588203828504660915L;

    private String code;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expireTime;

    public Captcha(String code, Duration duration) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(duration.getSeconds());
    }

    public Captcha(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
