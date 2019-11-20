package com.glitter.spring.boot.common;

import com.glitter.spring.boot.exception.BusinessException;

/**
 * @author limengjun
 * @param <T>
 */
public class CommonResult<T> {

	/** -----------------------------------------------定义类内部常量----------------------------------------------------- */
	/** 返回成功码 */
	private static final String CODE_SUCCESS = "0";
	 /** 返回失败码 */
	private static final String CODE_ERROR = "-1";
	 /** 返回成功信息 */
	private static final String MESSAGE_SUCCESS = "操作成功！";
	 /** 返回失败信息 */
	private static final String MESSAGE_ERROR = "操作失败！";


	/** --------------------------------------------------定义属性------------------------------------------------------ */
	private CodeMessage meta;
	private T data;


	/** ---------------------------构造方法,必须包含无参构造函数,否则有些地方会处理失败,比如反json-------------------------------- */
	public CommonResult(){
	}
	private CommonResult(String code, String message) {
		this(code, message, null);
	}
	private CommonResult(String code, String message, T data) {
		CodeMessage meta = new CodeMessage();
		meta.setCode(code);
		meta.setMessage(message);
		this.meta = meta;
		this.data = data;
	}
	private CommonResult(BusinessException businessException) {
		this(businessException.getCode(), businessException.getMessage(), null);
	}
	private CommonResult(BusinessException businessException, T data) {
		this(businessException.getCode(), businessException.getMessage(), data);
	}


	/** -------------------------------------------------异常方法处理----------------------------------------------------- */
	public static <T> CommonResult<T> exception(BusinessException businessException) {
		return new CommonResult(businessException);
	}
	public static <T> CommonResult<T> exception(BusinessException businessException, T data) {
		return new CommonResult(businessException, data);
	}

	/** -------------------------------------------------成功方法处理----------------------------------------------------- */
	public static <T> CommonResult<T> success(){
		return new CommonResult(CODE_SUCCESS,MESSAGE_SUCCESS,null);
	}
	public static <T> CommonResult<T> success(String msg){
		return new CommonResult(CODE_SUCCESS,msg,null);
	}
	public static <T> CommonResult<T> success(T data) {
		return new CommonResult(CODE_SUCCESS,MESSAGE_SUCCESS,data);
	}
	public static <T> CommonResult<T> success(String msg, T data){
		return new CommonResult(CODE_SUCCESS,msg,data);
	}
	public static <T> CommonResult<T> success(String code, String msg, T data){
		return new CommonResult(code,msg,data);
	}


	/** ------------------------------------------------失败方法处理------------------------------------------------------ */
	public static <T> CommonResult<T> fail(){
		return new CommonResult(CODE_ERROR,MESSAGE_ERROR,null);
	}
	public static <T> CommonResult<T> fail(String msg){
		return new CommonResult(CODE_ERROR,msg,null);
	}
	public static <T> CommonResult<T> fail(T data) {
		return new CommonResult(CODE_ERROR,MESSAGE_ERROR,data);
	}
	public static <T> CommonResult<T> fail(String msg, T data){
		return new CommonResult(CODE_ERROR,msg,data);
	}
	public static <T> CommonResult<T> fail(String code, String msg, T data){
		return new CommonResult(code,msg,data);
	}
	public static <T> CommonResult<T> fail(CodeMessage codeMessage) {
		return new CommonResult(codeMessage.getCode(), codeMessage.getMessage());
	}

    /**  ---------------------------------------------getter/setter----------------------------------------------------- */
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public CodeMessage getMeta() {
		return this.meta;
	}

	public void setMeta(CodeMessage meta) {
		this.meta = meta;
	}

}
