package com.zhsj.api.exception;


public class ApiException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7771051225403402687L;
    private int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
