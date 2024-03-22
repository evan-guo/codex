package com.codex.security.captcha.image;

import com.codex.security.captcha.Captcha;
import com.codex.security.captcha.CaptchaGenerator;
import com.codex.security.properties.SecurityCaptchaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author guowei
 * @since 2023-01-13
 * 图形验证码生成器
 */
@RequiredArgsConstructor
@Component("imageCaptchaGenerator")
public class ImageCaptchaGenerator implements CaptchaGenerator {

    private final SecurityCaptchaProperties captchaProperties;

    @Override
    public Captcha generate() {
        // 取出图片宽高参数
        int width = captchaProperties.getImage().getWidth();
        int height = captchaProperties.getImage().getHeight();
        int length = captchaProperties.getImage().getLength();
        // 生成图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(getRandomColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        graphics.setColor(getRandomColor(160, 200));
        Random random = new Random();
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            randomStr.append(rand);
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            graphics.drawString(rand, 13 * i + 6, 16);
        }
        graphics.dispose();
        return new ImageCaptcha(image, randomStr.toString(), captchaProperties.getImage().getDuration());
    }

    /**
     * 生成随机背景条纹
     * @param frontColor 前置色
     * @param backColor  后置色
     * @return  随机背景颜色
     */
    private Color getRandomColor(int frontColor, int backColor) {
        Random random = new Random();
        int maxColor = 255;
        if (frontColor > maxColor) {
            frontColor = maxColor;
        }
        if (backColor > maxColor) {
            backColor = maxColor;
        }
        int r = frontColor + random.nextInt(backColor - frontColor);
        int g = frontColor + random.nextInt(backColor - frontColor);
        int b = frontColor + random.nextInt(backColor - frontColor);
        return new Color(r, g, b);
    }

}
