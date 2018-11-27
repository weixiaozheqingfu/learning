package com.glitter.spring.boot.common;

/**
 * Created by MaJingcao on 2018/3/9.
 * Copyright by syswin
 */
public class ResponseResult<T> extends BaseBean {
    /** 返回成功码 */
    private static final String CODE_SUCCESS = "0";
    /** 返回成功信息 */
    private static final String MESSAGE_SUCCESS = "操作成功！";


    private String code;

    private String message;

    private T data;

    public ResponseResult() {

    }

    public ResponseResult(T data) {
        this(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public ResponseResult(String code, String message) {
        this(code, message, null);
    }

    public ResponseResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(data);
    }

    public static <T> ResponseResult<T> fail(String code, String message, T data) {
        return new ResponseResult<T>(code, message, data);
    }

    public static <T> ResponseResult<T> fail(String code, String message) {
        return new ResponseResult<T>(code, message);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean success() {
        return this.code == CODE_SUCCESS;
    }
}