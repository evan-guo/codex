package com.codex.admin.base.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * 基础类
 *
 * @author evan guo
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private String id;
    /**
     * 创建人
     */
//    @CodexField(name = "创建人")
    @Column
    private String createdBy;
    /**
     * 创建时间
     */
//    @CodexField(name = "创建时间")
    @Column(onInsertValue = "now()")
    private Date createdTime;
    /**
     * 更新人
     */
//    @CodexField(name = "更新人")
    @Column
    private String updatedBy;
    /**
     * 更新时间
     */
//    @CodexField(name = "更新时间")
    @Column(onUpdateValue = "now()")
    private Date updatedTime;

}
