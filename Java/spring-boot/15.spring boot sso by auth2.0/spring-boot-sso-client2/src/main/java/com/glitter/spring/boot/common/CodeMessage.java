package com.glitter.spring.boot.common;

import java.io.Serializable;

/**
 * @author limengjun
 */
public class CodeMessage implements Serializable {

    private static final long serialVersionUID = 4999034267851088169L;


    private String code;

    private String message;


    public CodeMessage() {
    }

    public CodeMessage(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
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

}
