package com.codex.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.core.scan.CodexModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件资源操作服务
 *
 * @author evan guo
 */
@RequiredArgsConstructor
@Service
public class CodexFileService {

    private final CodexService codexService;

    public void importData(CodexModel codexModel, List<JSONObject> list) {
        Class<Object> clazz = (Class<Object>) codexModel.getClazz();
        List<Object> entities = BeanUtil.copyToList(list, clazz);
        codexService.batchSave(clazz, entities);
    }


}
