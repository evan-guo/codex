package com.codex.core.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.codex.core.api.query.PageQueryVo;
import com.codex.core.service.CodexService;
import com.codex.core.scan.CodexScanner;
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
@RequestMapping("/codex-api/data")
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
    @PostMapping({"/page/{codex}"})
    public Page<?> page(@PathVariable("codex") String codexName,
                        @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                        @RequestBody PageQueryVo queryVo) {
        if (withRelation) {
            return codexService.pageWithRelation(CodexScanner.getCodexModel(codexName).getClazz(), queryVo);
        } else {
            return codexService.page(CodexScanner.getCodexModel(codexName).getClazz(), queryVo);
        }
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
    @PostMapping({"/list/{codex}"})
    public List<?> list(@PathVariable("codex") String codexName,
                        @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                        @RequestBody JSONObject conditions) {
        if (withRelation) {
            return codexService.listWithRelation(CodexScanner.getCodexModel(codexName).getClazz(), conditions);
        } else {
            return codexService.list(CodexScanner.getCodexModel(codexName).getClazz(), conditions);
        }
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
    @GetMapping({"/{codex}/{id}"})
    public Object getDataById(@PathVariable("codex") String codexName,
                              @PathVariable("id") Serializable id,
                              @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation) {
        if (withRelation) {
            return codexService.getByIdWithRelation(CodexScanner.getCodexModel(codexName).getClazz(), id);
        } else {
            return codexService.getById(CodexScanner.getCodexModel(codexName).getClazz(), id);
        }
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
    @PostMapping({"/get/{codex}"})
    public Object getDataByWrapper(@PathVariable("codex") String codexName,
                                   @Parameter(name = "是否关联查询") @RequestParam(required = false) boolean withRelation,
                                   @RequestBody JSONObject conditions) {
        List<?> list;
        if (withRelation) {
            list = codexService.listWithRelation(CodexScanner.getCodexModel(codexName).getClazz(), conditions);
        } else {
            list = codexService.list(CodexScanner.getCodexModel(codexName).getClazz(), conditions);
        }
        if (!list.isEmpty()) {
            return MapperUtil.getSelectOneResult(list);
        }
        return null;
    }


}
