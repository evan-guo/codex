package com.codex.api.constant;

/**
 * API路径常量
 *
 * @author evan guo
 */
public class ApiPathConstants {

    // DataController
    public static final String API_DATA = "/codex-api/data";
    public static final String PAGE = "/page/{codex}";
    public static final String LIST = "/list/{codex}";
    public static final String GET = "/get/{codex}";
    public static final String GET_ID = "/{codex}/{id}";

    // ModifyController
    public static final String API_MODIFY = "/codex-api/modify";
    public static final String SAVE = "/save/{codex}";
    public static final String BATCH_SAVE = "/batch/save/{codex}";
    public static final String UPDATE = "/update/{codex}";
    public static final String BATCH_UPDATE = "/batch/update/{codex}";
    public static final String DELETE = "/delete/{codex}";
    public static final String BATCH_DELETE = "batch/delete/{codex}";

    // FileController
    public static final String API_FILE = "/codex-api/file";
    public static final String IMPORT = "/import/{codex}";
    public static final String EXPORT = "/export/{codex}";

}
