package com.codex.admin.tenant.entity;

import com.codex.api.annotation.CodexApi;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户 实体类。
 *
 * @author evan guo
 * @since 1.0.0
 */
@CodexApi(name = "租户")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String createdBy;

    @Column
    private Date createdTime;

    
    private String updatedBy;

    
    private Date updatedTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

}
