package com.crm.common;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("ok");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(String msg){
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
// getter & setter
}
