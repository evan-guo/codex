package com.codex.core.api.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.codex.annotation.sub_codex.PowerObject;
import com.codex.core.api.advice.ApiException;
import com.codex.core.proxy.DataProxyInvoke;
import com.codex.core.scan.CodexModel;
import com.codex.core.scan.CodexScanner;
import com.codex.core.service.CodexFileService;
import com.codex.core.service.CodexService;
import com.codex.core.util.PowerUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 通用文件处理Controller
 *
 * @author evan guo
 */
@Tag(name = "通用文件处理接口")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/codex-api/file")
@RestController
public class CodexFileController {

    private final CodexService codexService;
    private final CodexFileService codexFileService;

    @Operation(summary = "导入数据")
    @PostMapping({"/import/{codex}"})
    public void importData(@PathVariable("codex") String codexName, @RequestParam("file") MultipartFile file) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexName);
        PowerUtil.powerLegal(codexModel, PowerObject::isImportable);
        if (file.isEmpty() || null == file.getOriginalFilename()) {
            throw new ApiException("上传失败，请选择文件");
        }
        List<JSONObject> list;
        int i = 1;
        try {
            i++;
            Workbook wb;
            if (file.getOriginalFilename().endsWith(".xls")) {
                wb = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                throw new ApiException("上传文件格式必须为Excel");
            }
            list = ExcelUtil.getReader(file.getInputStream()).readAll(JSONObject.class);
            List<JSONObject> finalList = list;
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeImport(finalList, wb)));
            wb.close();
        } catch (Exception e) {
            log.error("Excel解析异常，出错行数：" + i + "，原因：" + e.getMessage(), e);
            throw new ApiException("Excel解析异常，出错行数：" + i + "，原因：" + e.getMessage());
        }
        try {
            codexFileService.importData(codexModel, list);
        } catch (Exception e) {
            throw new ApiException("数据导入异常，原因：" + e.getMessage());
        }
    }

    @Operation(summary = "导出数据")
    @PostMapping({"/export/{codex}"})
    public void exportData(@PathVariable("codex") String codexName, @RequestBody JSONObject conditions, HttpServletRequest request, HttpServletResponse response) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexName);
        List<?> list = codexService.list(codexModel.getClazz(), conditions);
        List<JSONObject> jsonList = JSONArray.parseArray(JSONObject.toJSONString(list), JSONObject.class);
        try (ExcelWriter writer = ExcelUtil.getWriter().write(list)) {
            Workbook workbook = writer.getWorkbook();
            DataProxyInvoke.invoke(codexModel, (dataProxy -> dataProxy.beforeExport(jsonList, workbook)));
            writer.flush(response.getOutputStream());
        } catch (IOException e) {
            throw new ApiException("导出excel失败");
        }
    }

}
