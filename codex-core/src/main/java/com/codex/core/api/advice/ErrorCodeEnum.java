package com.codex.core.api.advice;

/**
 * @author gw
 * 错误码枚举,根据需要随时添加错误信息
 */
public enum ErrorCodeEnum {
  /**
   * 成功
   */
  SUCCESS(0, "成功"),
  /**
   * 未知错误
   */
  ERROR(1, "未知错误"),
  /**
   * 未知错误
   */
  SYSTEM_ERROR(2, "系统异常"),
  /**
   * 响应失败
   */
  FAILED(3, "响应失败"),
  /**
   * 无权限访问该用户数据
   */
  NO_DATA_PERMISSION(6, "无权限访问该用户数据"),
  /**
   * 请求无访问权限
   */
  NO_REQUEST_PERMISSION(7, "请求无访问权限"),
  /**
   * 参数校验失败
   */
  VALIDATE_FAILED(100, "参数校验失败"),
  /**
   * 参数校验失败
   */
  NO_APP_PERMISSION(-10, "无应用使用权限"),


  ;
  private Integer code;
  private String msg;

  ErrorCodeEnum(int code, String msg) {
    this.setCode(code);
    this.setMsg(msg);
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
  
}