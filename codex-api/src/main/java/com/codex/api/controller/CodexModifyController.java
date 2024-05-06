package com.codex.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.api.constant.ApiPathConstants;
import com.codex.api.scan.CodexScanner;
import com.codex.api.service.CodexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 通用的数据处理接口
 *
 * @author evan guo
 */
@Tag(name = "通用数据处理接口")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.API_MODIFY)
@RestController
public class CodexModifyController {

    private final CodexService codexService;

    /**
     * 保存
     *
     * @param codexName Codex类名
     * @param data      数据
     */
    @Operation(summary = "新增数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.SAVE})
    public int save(@PathVariable("codex") String codexName, @RequestBody JSONObject data) {
        return codexService.save(CodexScanner.getCodexModel(codexName).getClazz(), data);
    }

    /**
     * 批量保存
     *
     * @param codexName Codex类名
     * @param dataList  数据列表
     */
    @Operation(summary = "批量新增数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.BATCH_SAVE})
    public int batchSave(@PathVariable("codex") String codexName, @RequestBody List<JSONObject> dataList) {
        Class<Object> clazz = (Class<Object>) CodexScanner.getCodexModel(codexName).getClazz();
        List<Object> entities = BeanUtil.copyToList(dataList, clazz);
        return codexService.batchSave(clazz, entities);
    }

    /**
     * 修改
     *
     * @param codexName Codex类名
     * @param data      数据
     */
    @Operation(summary = "修改数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.UPDATE})
    public Object update(@PathVariable("codex") String codexName, @RequestBody JSONObject data) {
        return codexService.update(CodexScanner.getCodexModel(codexName).getClazz(), data);
    }

    /**
     * 批量修改
     *
     * @param codexName Codex类名
     * @param dataList  数据列表
     */
    @Operation(summary = "批量修改数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.BATCH_UPDATE})
    public Object batchUpdate(@PathVariable("codex") String codexName, @RequestBody List<JSONObject> dataList) {
        Class<Object> clazz = (Class<Object>) CodexScanner.getCodexModel(codexName).getClazz();
        List<Object> entities = BeanUtil.copyToList(dataList, clazz);
        return codexService.batchUpdate(clazz, entities);
    }

    /**
     * 删除
     *
     * @param codexName Codex类名
     * @param id        数据ID
     */
    @Operation(summary = "删除数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.DELETE})
    public Object delete(@PathVariable("codex") String codexName, @RequestParam Serializable id) {
        return codexService.delete(CodexScanner.getCodexModel(codexName).getClazz(), id);
    }

    /**
     * 批量删除
     *
     * @param codexName Codex类名
     * @param ids       数据ID列表
     */
    @Operation(summary = "批量删除数据")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping({ApiPathConstants.BATCH_DELETE})
    public Object batchDelete(@PathVariable("codex") String codexName,
                              @RequestBody List<Serializable> ids) {
        return codexService.batchDelete(CodexScanner.getCodexModel(codexName).getClazz(), ids);
    }


}
