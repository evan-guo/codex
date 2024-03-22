package com.codex.annotation.sub_codex;

import com.alibaba.fastjson2.JSONObject;
import com.codex.annotation.config.Comment;
import com.mybatisflex.core.query.QueryWrapper;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.Serializable;
import java.util.List;

/**
 * 数据代理行为
 *
 * @author evan guo
 */
public interface DataProxy<T> {

    @Comment("自定义路由")
    default void addRouter() {
    }

    /**
     * 新增前事件
     *
     * @param source 源数据
     * @param entity 数据
     */
    @Comment("新增前")
    default void beforeAdd(Object source, T entity) {
    }

    /**
     * 新增后事件
     *
     * @param entity 数据
     */
    @Comment("新增后")
    default void afterAdd(T entity) {
    }

    /**
     * 修改前事件
     *
     * @param source 源数据
     * @param entity 数据
     */
    @Comment("修改前")
    default void beforeEdit(Object source, T entity) {
    }

    /**
     * 修改后事件
     *
     * @param entity 数据
     */
    @Comment("修改后")
    default void afterEdit(T entity) {
    }

    /**
     * 删除前事件
     *
     * @param id 数据ID
     */
    @Comment("删除前")
    default void beforeDelete(Serializable id) {
    }

    /**
     * 删除后事件
     *
     * @param entity 被删除数据
     */
    @Comment("删除后")
    default void afterDelete(T entity) {
    }

    /**
     * 查询前事件
     *
     * @param conditions   条件
     * @param queryWrapper 条件转换后的QueryWrapper
     */
    @Comment("查询前")
    default void beforeFetch(JSONObject conditions, QueryWrapper queryWrapper) {
    }

    /**
     * 查询后事件
     * @param entities 数据列表
     */
    @Comment("查询后")
    default void afterFetch(List<T> entities) {
    }

    @Comment("导入前")
    default void beforeImport(List<JSONObject> list, Workbook workbook) {
    }

    @Comment("导出前")
    default void beforeExport(List<JSONObject> list, Workbook workbook) {
    }

    @Comment("下载前")
    default void beforeDownload() {
    }

    @Comment("上传后")
    default void afterUpload() {
    }


}
