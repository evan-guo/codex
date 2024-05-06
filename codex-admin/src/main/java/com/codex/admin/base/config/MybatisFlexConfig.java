package com.codex.admin.base.config;

import com.codex.admin.base.entity.BaseEntity;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;
import com.mybatisflex.core.FlexGlobalConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author evan guo
 */
@Configuration
public class MybatisFlexConfig implements InitializingBean {


    @Override
    public void afterPropertiesSet() {
        MyInsertListener insertListener = new MyInsertListener();
        MyUpdateListener updateListener = new MyUpdateListener();
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(insertListener, BaseEntity.class);
        config.registerUpdateListener(updateListener, BaseEntity.class);
    }

    /**
     * 全局InsertListener
     */
    public static class MyInsertListener implements InsertListener {
        @Override
        public void onInsert(Object entity) {
            if (entity instanceof BaseEntity) {
                ((BaseEntity) entity).setCreatedBy("admin");
            }
        }
    }

    /**
     * 全局UpdateListener
     */
    public static class MyUpdateListener implements UpdateListener {
        @Override
        public void onUpdate(Object entity) {
            if (entity instanceof BaseEntity) {
                ((BaseEntity) entity).setUpdatedBy("admin");
            }
        }
    }

}
