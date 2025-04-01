package com.example.demo.entity;

import lombok.Data;

@Data
public class ResponseInfo {
    private int code;
    private String msg;
    private Object data;

    public ResponseInfo() {
    }

    public ResponseInfo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseInfo success(Object data) {
    	return new ResponseInfo(200, "success", data);
    }
    public static ResponseInfo error(int code, String msg) {
    	return new ResponseInfo(code, msg, null);
    }
}
