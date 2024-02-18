package com.codex.core.api.docs;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.codex.annotation.sub_codex.Power;
import com.codex.annotation.Codex;
import com.codex.core.config.CodexConstants;
import com.codex.core.scan.CodexFieldModel;
import com.codex.core.scan.CodexModel;
import com.codex.core.scan.CodexScanner;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对Springdoc的OpenAPI进行处理
 *
 * @author evan guo
 */
@RequiredArgsConstructor
@Configuration
public class CodexOpenApiCustomizer implements OpenApiCustomiser {

    private final GenericResponseService genericResponseService;

    @Override
    public void customise(OpenAPI openApi) {
        Paths paths = openApi.getPaths();
        // 过滤得到通用接口的PathItem
        List<Map.Entry<String, PathItem>> pathItemList = filterPath(paths);
        // 获取所有Schemas
        Map<String, Schema> schemas = openApi.getComponents().getSchemas();
        List<CodexModel> modelList = CodexScanner.getCodexModelList();
        for (CodexModel codexModel : modelList) {
            Schema schema = schemas.get(codexModel.getClazzName());
            if (schema == null) {
                genericResponseService.buildContent(openApi.getComponents(), new Annotation[0], new String[]{"*/*"}, null, codexModel.getClazz());
                schema = schemas.get(codexModel.getClazzName());
            }
            setSchemaTitle(schema, codexModel);
            createSchemas(schemas, schema, codexModel);
            for (Map.Entry<String, PathItem> pathItemEntry : pathItemList) {
                PathItem pathItem = createPathItem(pathItemEntry, codexModel);
                if (pathItem == null) {
                    continue;
                }
                // 重写path路径
                String replacePathVariable = pathItemEntry.getKey().replace("{codex}", codexModel.getClazzName());
                paths.put(replacePathVariable, pathItem);
            }
        }
        pathItemList.forEach(path -> paths.remove(path.getKey()));
    }

    /**
     * 过滤得到通用接口的PathItem
     */
    private List<Map.Entry<String, PathItem>> filterPath(Paths paths) {
        return paths.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("/codex-api/data") || entry.getKey().startsWith("/codex-api/modify"))
                .collect(Collectors.toList());
    }

    /**
     * 设置Schema各个字段的标题
     */
    private void setSchemaTitle(Schema schema, CodexModel codexModel) {
        if (schema != null) {
            Map<String, Schema> properties = schema.getProperties();
            List<CodexFieldModel> codexFieldModels = codexModel.getCodexFieldModels();
            for (CodexFieldModel fieldModel : codexFieldModels) {
                Schema fieldSchema = properties.get(fieldModel.getFieldName());
                fieldSchema.setTitle(fieldModel.getCodexField().name());
                fieldSchema.setDescription(fieldModel.getCodexField().desc());
            }
        }
    }

    private void createSchemas(Map<String, Schema> schemas, Schema schema, CodexModel codexModel) {
        // 查询条件
        Schema searchSchema = createQuerySchema(schema, codexModel);
        schemas.put(searchSchema.getName(), searchSchema);
        // 分页查询条件
        Schema pageQuerySchema = createPageQuerySchema(schemas.get("PageQueryVo"), codexModel);
        schemas.put(pageQuerySchema.getName(), pageQuerySchema);
        // 响应对象
        Schema responseSchema = createResponseSchema(schemas.get("ResponseObject"), codexModel);
        schemas.put(responseSchema.getName(), responseSchema);
        // 分页查询响应对象
        //Schema listResponseSchema = createPageResponseSchema(schemas.get("ResponseObject"), codexModel);
        //schemas.put(listResponseSchema.getName(), listResponseSchema);
        // 分页查询响应对象
        Schema pageResponseSchema = createPageResponseSchema(schemas.get("PageObject"), codexModel);
        schemas.put(pageResponseSchema.getName(), pageResponseSchema);
    }

    /**
     * 创建查询条件
     */
    private Schema createQuerySchema(Schema schema, CodexModel codexModel) {
        Map<String, Schema> properties = schema.getProperties();
        List<CodexFieldModel> searchFieldModels = codexModel.getSearchFieldModels();
        Map<String, CodexFieldModel> searchFieldMap = searchFieldModels.stream().collect(Collectors.toMap(CodexFieldModel::getFieldName, fieldModel -> fieldModel));
        Schema searchSchema = new Schema();
        searchSchema.setName(codexModel.getClazzName() + "QuerySchema");
        searchSchema.setType(schema.getType());
        searchSchema.setFormat(schema.getFormat());
        Map<String, Schema> copyProperties = new HashMap<>();
        properties.entrySet().stream()
                .filter(entry -> searchFieldMap.containsKey(entry.getKey()))
                .forEach(entry -> copyProperties.put(entry.getKey(), entry.getValue()));
        searchSchema.setProperties(copyProperties);
        return searchSchema;
    }

    /**
     * 创建分页查询条件
     */
    private Schema createPageQuerySchema(Schema schema, CodexModel codexModel) {
        Schema pageSchema = new Schema();
        pageSchema.setName(codexModel.getClazzName() + "PageQuerySchema");
        pageSchema.setType(schema.getType());
        pageSchema.setFormat(schema.getFormat());
        pageSchema.setProperties(new HashMap<String, Schema>(schema.getProperties()));
        Schema conditionsSchema = (Schema) schema.getProperties().get("conditions");
        Schema newConditionsSchema = new Schema();
        newConditionsSchema.setType(conditionsSchema.getType());
        newConditionsSchema.setFormat(conditionsSchema.getFormat());
        newConditionsSchema.set$ref("#/components/schemas/" + codexModel.getClazzName() + "QuerySchema");
        pageSchema.getProperties().put("conditions", newConditionsSchema);
        return pageSchema;
    }

    /**
     * 创建单个查询响应对象
     */
    private Schema createResponseSchema(Schema schema, CodexModel codexModel) {
        Schema pageSchema = new Schema();
        pageSchema.setName(codexModel.getClazzName() + "ResponseSchema");
        pageSchema.setType(schema.getType());
        pageSchema.setFormat(schema.getFormat());
        pageSchema.setProperties(new HashMap<String, Schema>(schema.getProperties()));
        Schema dataSchema = (Schema) schema.getProperties().get("data");
        Schema newDataSchema = new Schema();
        newDataSchema.setType(dataSchema.getType());
        newDataSchema.setFormat(dataSchema.getFormat());
        newDataSchema.set$ref("#/components/schemas/" + codexModel.getClazzName());
        pageSchema.getProperties().put("data", newDataSchema);
        return pageSchema;
    }


    /**
     * 创建分页查询响应对象
     */
    private Schema createPageResponseSchema(Schema schema, CodexModel codexModel) {
        Schema pageSchema = new Schema();
        pageSchema.setName(codexModel.getClazzName() + "PageResponseSchema");
        pageSchema.setType(schema.getType());
        pageSchema.setFormat(schema.getFormat());
        pageSchema.setProperties(new HashMap<String, Schema>(schema.getProperties()));
        Schema recordsSchema1 = (Schema) schema.getProperties().get("records");
        Schema recordsSchema = JSONObject.parseObject(JSONObject.toJSONString(recordsSchema1), Schema.class);
        recordsSchema.setProperties(null);
        recordsSchema.getItems().$ref("#/components/schemas/" + codexModel.getClazzName());
        pageSchema.getProperties().put("records", recordsSchema);
        return pageSchema;
    }

    /**
     * 创建接口路径PathItem
     */
    private PathItem createPathItem(Map.Entry<String, PathItem> pathItemEntry, CodexModel codexModel) {
        Codex codex = codexModel.getCodex();
        // 深拷贝
        PathItem copyPathItem = JSONObject.parseObject(JSONObject.toJSONString(pathItemEntry.getValue()), PathItem.class);
        Operation operation = getOperation(copyPathItem);
        if (operation == null) {
            return null;
        }
        // 校验接口权限
        Power power = codexModel.getCodex().power();
        if (!checkPathPower(power, operation)) {
            return null;
        }
        // 重写RequestBody
        setRequestBody(operation, codexModel);
        // 重写ResponseBody
        setResponseBody(operation, codexModel);
        // 设置tag
        List<String> tags = operation.getTags();
        tags.clear();
        tags.add(codex.name());
        // 设置描述
        operation.setDescription(codex.name() + codex.desc());
        // 重写operationId
        operation.setOperationId(operation.getOperationId() + codexModel.getClazzName());
        // 去掉codex参数
        operation.getParameters().stream()
                .filter(parameter -> parameter.getName().equals(CodexConstants.DEFAULT_CODEX_NAME))
                .findAny()
                .ifPresent(parameter -> operation.getParameters().remove(parameter));
        return copyPathItem;
    }

    /**
     * 校验接口权限
     */
    private boolean checkPathPower(Power power, Operation operation) {
        if (!power.enable()) {
            return true;
        }
        switch (operation.getOperationId()) {
            case CodexConstants.API_SAVE:
            case CodexConstants.API_BATCH_SAVE:
                return power.add();
            case CodexConstants.API_UPDATE:
            case CodexConstants.API_BATCH_UPDATE:
                return power.edit();
            case CodexConstants.API_DELETE:
            case CodexConstants.API_BATCH_DELETE:
                return power.delete();
            case CodexConstants.API_PAGE:
            case CodexConstants.API_LIST:
            case CodexConstants.API_GET_ID:
            case CodexConstants.API_GET:
                return power.query();
            default:
                return false;
        }
    }

    /**
     * 重写RequestBody
     */
    private static void setRequestBody(Operation operation, CodexModel codexModel) {
        RequestBody requestBody = operation.getRequestBody();
        if (requestBody == null) {
            return;
        }
        MediaType mediaType = requestBody.getContent().get("application/json");
        if (mediaType == null) {
            return;
        }
        Schema schema = mediaType.getSchema();
        if (schema != null) {
            switch (operation.getOperationId()) {
                case CodexConstants.API_SAVE:
                case CodexConstants.API_UPDATE:
                    schema.$ref("#/components/schemas/" + codexModel.getClazzName());
                    break;
                case CodexConstants.API_BATCH_SAVE:
                case CodexConstants.API_BATCH_UPDATE:
                    schema.getItems().$ref("#/components/schemas/" + codexModel.getClazzName());
                    break;
                case CodexConstants.API_LIST:
                case CodexConstants.API_GET:
                    schema.$ref("#/components/schemas/" + codexModel.getClazzName() + "QuerySchema");
                    break;
                case CodexConstants.API_PAGE:
                    schema.$ref("#/components/schemas/" + codexModel.getClazzName() + "PageQuerySchema");
                    break;
                case CodexConstants.API_BATCH_DELETE:
                    schema.getItems().type("string");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 重写ResponseBody
     */
    private void setResponseBody(Operation operation, CodexModel codexModel) {
        ApiResponses apiResponses = operation.getResponses();
        if (apiResponses == null) {
            return;
        }
        Content content = apiResponses.get("200").getContent();
        if (content == null || !content.containsKey("*/*")) {
            return;
        }
        MediaType mediaType = content.get("*/*");
        Schema schema = mediaType.getSchema();
        if (schema != null) {
            if (StrUtil.startWithAny(operation.getOperationId(), "list")) {

                //schema.setType("object");
                //schema.set$ref("#/components/schemas/" + codexModel.getClazzName() + "ResponseSchema");
                //schema.getItems().$ref("#/components/schemas/" + codexModel.getClazzName() + "ResponseSchema");
            } else if (StrUtil.startWithAny(operation.getOperationId(), "page")) {
                schema.$ref("#/components/schemas/" + codexModel.getClazzName() + "PageResponseSchema");
            } else if (StrUtil.startWithAny(operation.getOperationId(), "getDataByWrapper")) {
                schema.$ref("#/components/schemas/" + codexModel.getClazzName() + "ResponseSchema");
            } else if (StrUtil.startWithAny(operation.getOperationId(), "getDataById")) {
                schema.$ref("#/components/schemas/" + codexModel.getClazzName() + "ResponseSchema");
            }
        }
    }

    private static Operation getOperation(PathItem copyPathItem) {
        Operation operation = null;
        if (copyPathItem.getGet() != null) {
            operation = copyPathItem.getGet();
        } else if (copyPathItem.getPost() != null) {
            operation = copyPathItem.getPost();
        } else if (copyPathItem.getPut() != null) {
            operation = copyPathItem.getPut();
        } else if (copyPathItem.getDelete() != null) {
            operation = copyPathItem.getDelete();
        }
        return operation;
    }

}
