package com.cxd.rtcroom.dto;

import java.io.Serializable;

/**
 * @author ws03089
 */
public class JSONResult implements Serializable {



    private boolean success;
    private Object message;
    private Object data;

    public JSONResult(boolean success, Object message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public JSONResult(boolean success, Object message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public JSONResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Object getMessage() {
        return message;
    }

    public JSONResult setMessage(Object message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JSONResult setData(Object data) {
        this.data = data;
        return this;
    }
}