package com.codex.core.api.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.core.service.CodexService;
import com.codex.core.scan.CodexScanner;
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
@RequestMapping("/codex-api/modify")
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
    @PostMapping({"/save/{codex}"})
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
    @PostMapping({"/batch/save/{codex}"})
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
    @PostMapping({"/update/{codex}"})
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
    @PostMapping({"/batch/update/{codex}"})
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
    @PostMapping({"/delete/{codex}"})
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
    @PostMapping({"/batch/delete/{codex}"})
    public Object batchDelete(@PathVariable("codex") String codexName,
                              @RequestBody List<Serializable> ids) {
        return codexService.batchDelete(CodexScanner.getCodexModel(codexName).getClazz(), ids);
    }


}
