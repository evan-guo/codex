package com.codex.api.docs;

import com.codex.api.scan.CodexScanner;
import com.codex.api.scan.CodexModel;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SpringDoc的OpenAPI的自定义处理事件
 *
 * @author evan guo
 */
@RequiredArgsConstructor
@Configuration
@SuppressWarnings({"rawtypes"})
public class CodexOpenApiCustomizer implements OpenApiCustomizer {

    private final GenericResponseService genericResponseService;

    @Override
    public void customise(OpenAPI openApi) {
        Paths paths = openApi.getPaths();
        // 过滤得到通用接口的PathItem
        List<Map.Entry<String, PathItem>> pathItemList = filterPath(paths);
        // 获取所有Schemas
        Map<String, Schema> schemas = openApi.getComponents().getSchemas();
        // 获取所有CodexModel
        List<CodexModel> modelList = CodexScanner.getCodexModelList();
        for (CodexModel codexModel : modelList) {
            Schema schema = schemas.get(codexModel.getClazzName());
            // 如果未构建Schema，调用SpringDoc方法进行构建
            if (schema == null) {
                genericResponseService.buildContent(openApi.getComponents(), new Annotation[0], new String[]{"*/*"}, null, codexModel.getClazz());
                schema = schemas.get(codexModel.getClazzName());
            }
            ApiPathBuilder.build(openApi, codexModel, schema, pathItemList);
        }
        pathItemList.forEach(path -> paths.remove(path.getKey()));
    }

    /**
     * 过滤得到通用接口的PathItem
     */
    private List<Map.Entry<String, PathItem>> filterPath(Paths paths) {
        return paths.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("/codex-api"))
                .collect(Collectors.toList());
    }

}
