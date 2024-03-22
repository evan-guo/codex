//package com.codex.core.codegen;
//
//import com.mybatisflex.codegen.config.GlobalConfig;
//import com.mybatisflex.core.service.IService;
//import com.mybatisflex.spring.service.impl.ServiceImpl;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author evan guo
// */
//@Configuration
//public class CodegenConfig {
//
//    @Bean
//    public GlobalConfig getCodegenGlobalConfig() {
//        GlobalConfig globalConfig = new GlobalConfig();
//        // 设置生成 entity 并启用 Lombok
//        globalConfig.setEntityGenerateEnable(true);
//        globalConfig.setEntityWithLombok(true);
//        // 设置生成 mapper
//        globalConfig.setMapperGenerateEnable(false);
//        // 设置生成 service
//        globalConfig.setServiceGenerateEnable(true);
//        // 设置生成 serviceImpl
//        globalConfig.setServiceImplGenerateEnable(true);
//        globalConfig.setServiceImplCacheExample(true);
//        // 设置生成controller
//        globalConfig.setControllerGenerateEnable(true);
//        globalConfig.setControllerOverwriteEnable(false);
//        return globalConfig;
//    }
//
//}
