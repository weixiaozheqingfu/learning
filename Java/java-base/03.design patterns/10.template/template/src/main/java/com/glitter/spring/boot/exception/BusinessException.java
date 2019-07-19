package com.glitter.spring.boot.exception;

import com.glitter.spring.boot.common.CodeMessage;

import java.io.Serializable;

public class BusinessException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
    private String message;
 
    public BusinessException() {
       
    }
    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public BusinessException(CodeMessage codeMessage) {
        this.code = codeMessage==null?"":codeMessage.getCode();
        this.message = codeMessage==null?"":codeMessage.getMessage();
    }

    public void setCode(String code) {
        this.code = code;
    }

	public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
