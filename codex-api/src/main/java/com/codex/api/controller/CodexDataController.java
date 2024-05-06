package com.codex.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.codex.api.constant.ApiPathConstants;
import com.codex.api.model.vo.PageQueryVo;
import com.codex.api.scan.CodexScanner;
import com.codex.api.service.CodexService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.util.MapperUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;


/**
 * 通用的数据查询Controller
 *
 * @author evan guo
 */
@Tag(name = "通用数据查询接口")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.API_DATA)
@RestController
public class CodexDataController {

    private final CodexService codexService;

    /**
     * 分页查询
     *
     * @param codexName    Codex类名
     * @param withRelation 是否查询关联表
     * @param queryVo      分页查询条件
     * @return 分页查询结果
     */
    @Operation(summary = "分页查询")
    @PostMapping({ApiPathConstants.PAGE})
    public Page<?> page(@PathVariable("codex") String codexName,
                        @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                        @RequestBody PageQueryVo queryVo) {
        return codexService.page(CodexScanner.getCodexModel(codexName).getClazz(), queryVo, withRelation);
    }

    /**
     * 列表查询
     *
     * @param codexName    Codex类名
     * @param withRelation 是否查询关联表
     * @param conditions   查询条件
     * @return 列表查询结果
     */
    @Operation(summary = "列表查询")
    @PostMapping({ApiPathConstants.LIST})
    public List<?> list(@PathVariable("codex") String codexName,
                        @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                        @RequestBody JSONObject conditions) {
        return codexService.list(CodexScanner.getCodexModel(codexName).getClazz(), conditions, withRelation);
    }

    /**
     * 根据ID查询
     *
     * @param codexName    Codex类名
     * @param id           ID
     * @param withRelation 是否查询关联表
     * @return 查询结果
     */
    @Operation(summary = "id查询")
    @GetMapping({ApiPathConstants.GET_ID})
    public Object getDataById(@PathVariable("codex") String codexName,
                              @PathVariable("id") Serializable id,
                              @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation) {
        return codexService.getById(CodexScanner.getCodexModel(codexName).getClazz(), id, withRelation);
    }

    /**
     * 条件查询单个
     *
     * @param codexName    Codex类名
     * @param withRelation 是否查询关联表
     * @param conditions   查询条件
     * @return 查询结果
     */
    @Operation(summary = "单个查询")
    @PostMapping({ApiPathConstants.GET})
    public Object getDataByWrapper(@PathVariable("codex") String codexName,
                                   @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                                   @RequestBody JSONObject conditions) {
        List<?> list = codexService.list(CodexScanner.getCodexModel(codexName).getClazz(), conditions, withRelation);
        if (!list.isEmpty()) {
            return MapperUtil.getSelectOneResult(list);
        }
        return null;
    }


}
