package com.codex.api.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 创建事件
 *
 * @author wei.guo
 */
@Slf4j
@Component
public class StartupEvent implements ApplicationRunner {

    @Value("${server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String docHtml = "doc.html";
        StringBuilder stringBuilder = new StringBuilder("http://");
        stringBuilder.append(inetAddress.getHostAddress()).append(":").append(port).append(contextPath);
        if (!contextPath.endsWith("/")) {
            stringBuilder.append("/");
        }
        stringBuilder.append(docHtml);
        log.info("项目启动完成，接口地址：{}", stringBuilder);
    }
}
