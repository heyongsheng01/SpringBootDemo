package com.example.demo.model;

public class Result<T> {

    /**
     * 错误码.
     */
    private String resCode;

    /**
     * 提示信息.
     */
    private String resMsg;

    /**
     * 具体的内容.
     */
    private T data;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
