package com.codex.core.api.query;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @author evan guo
 */
@Data
public class PageQueryVo {

    private long pageNumber = 1L;

    private long pageSize = 10;

    private JSONObject conditions;

}
