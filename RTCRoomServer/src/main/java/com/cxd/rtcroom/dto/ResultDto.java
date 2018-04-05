package com.cxd.rtcroom.dto;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class ResultDto implements Serializable{
    private Object code;
    private JSONObject data;

    public Object getCode() {
        return code;
    }

    public ResultDto setCode(Object code) {
        this.code = code;
        return this;
    }

    public JSONObject getData() {
        return data;
    }

    public ResultDto setData(JSONObject data) {
        this.data = data;
        return this;
    }



}
