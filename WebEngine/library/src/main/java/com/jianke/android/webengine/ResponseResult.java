package com.jianke.android.webengine;

/**
 *
 * Web接口返回结果
 *
 */
public class ResponseResult<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus(){
        return code;
    }

    public void setStatus(int status) {
        this.code = status;
    }

    public int getErrorcode(){
        return code;
    }

    public void setErrorcode(int errorcode) {
        this.code = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
