package com.codex.admin.codegen;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.core.datasource.FlexDataSource;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * @author evan guo
 */
@Configuration
public class CodegenConfig {

    public static void main(String[] args) {
        //配置数据源
        URL location = CodegenConfig.class.getProtectionDomain().getCodeSource().getLocation();
        String rootDirectory = location.getPath();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://47.100.36.143:13306/codex_admin?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("72Vf^xXgc$&4Xh4K");
        GlobalConfig globalConfig = getCodegenGlobalConfig();
        globalConfig.setGenerateTable("tenant");
        globalConfig.setSourceDir(rootDirectory.substring(0, rootDirectory.indexOf("/target/classes")) + "/src/main/java");
        globalConfig.setBasePackage("com.codex.admin.tenant");
        Generator generator = new Generator(dataSource, globalConfig);
        generator.generate();
    }

    // @Bean
    public static GlobalConfig getCodegenGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置生成 entity 并启用 Lombok
        globalConfig.getEntityConfig()
                .setOverwriteEnable(true)
                .setWithSwagger(true)
                .setSwaggerVersion(EntityConfig.SwaggerVersion.DOC);
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
        // 设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        // 设置生成 service
        globalConfig.setServiceGenerateEnable(true);
        // 设置生成 serviceImpl
        globalConfig.setServiceImplGenerateEnable(true);
        globalConfig.setServiceImplCacheExample(true);
        // 设置生成controller
        globalConfig.setControllerGenerateEnable(true);
        globalConfig.setControllerOverwriteEnable(false);
        // 设置SpringDoc
        globalConfig.getJavadocConfig()
                .setAuthor("evan guo")
                .setSince("1.0.0");
        return globalConfig;
    }

}
