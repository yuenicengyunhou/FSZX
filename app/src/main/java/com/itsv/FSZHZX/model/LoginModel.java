package com.itsv.FSZHZX.model;

import java.io.Serializable;

public class LoginModel implements Serializable {


    /**
     * success : true
     * code : 200
     * msg : “请求成功”
     * data : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNTg3OTkxMzM4LCJ1c2VySWQiOjEsImlhdCI6MTU4NzkwNDkzOCwiYWNjb3VudCI6ImFkbWluIiwidXNlcktleSI6ImNsaWVudCJ9.wdxyLjnPOmn6e5GxJzgyLL31FlAylfq19SAR7XuoLhVFR6Ykp11GoPpCd42hVlhPcnnVRH4VmDMdqdUv83PhIA
     */

    private boolean success;
    private int code;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
