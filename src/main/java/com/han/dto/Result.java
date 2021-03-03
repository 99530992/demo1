package com.han.dto;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private boolean flag;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(String code, boolean flag, String msg, T data) {
        this.code = code;
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public Result(String code, boolean flag, String msg) {
        this.code = code;
        this.flag = flag;
        this.msg = msg;
    }
}
