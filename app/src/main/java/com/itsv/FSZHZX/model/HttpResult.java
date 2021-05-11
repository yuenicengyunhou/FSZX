package com.itsv.FSZHZX.model;

public class HttpResult<T> {

    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : [{"id":1390233511271067650,"typeName":"\u201c世界无烟日\u201d专项答题","typeTime":"2021-05-31 00:00:00"}]
     */

    private boolean success;
    private int code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
