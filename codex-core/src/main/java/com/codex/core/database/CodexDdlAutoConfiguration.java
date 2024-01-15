package com.codex.core.database;

import com.codex.core.scan.CodexScannerConfigurer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 根据配置决定是否要自动建表
 * @author evan guo
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(CodexScannerConfigurer.class)
@ConditionalOnProperty(name = "codex.db.ddl-auto", havingValue = "true")
public class CodexDdlAutoConfiguration implements EnvironmentAware, ApplicationRunner {

    @Setter
    private Environment environment;

    private final CodexTableAutoScanner codexTableAutoScanner;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        codexTableAutoScanner.doScan();
    }

}
