package com.codex.api.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.codex.api.annotation.PowerObject;
import com.codex.api.constant.ApiPathConstants;
import com.codex.api.exception.ApiException;
import com.codex.api.proxy.DataProxyInvoke;
import com.codex.api.scan.CodexScanner;
import com.codex.api.scan.CodexModel;
import com.codex.api.service.CodexFileService;
import com.codex.api.service.CodexService;
import com.codex.api.util.PowerUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping(ApiPathConstants.API_FILE)
@RestController
public class CodexFileController {

    private final CodexService codexService;
    private final CodexFileService codexFileService;

    @Operation(summary = "导入数据")
    @PostMapping({ApiPathConstants.IMPORT})
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
    @PostMapping({ApiPathConstants.EXPORT})
    public void exportData(@PathVariable("codex") String codexName, @RequestBody JSONObject conditions, HttpServletRequest request, HttpServletResponse response) {
        CodexModel codexModel = CodexScanner.getCodexModel(codexName);
        List<?> list = codexService.list(codexModel.getClazz(), conditions, false);
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
