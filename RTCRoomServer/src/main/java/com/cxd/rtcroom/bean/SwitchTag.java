package com.cxd.rtcroom.bean;

import java.io.Serializable;

public class SwitchTag implements Serializable{

    private boolean icheck;
    private String tag;

    public boolean isIcheck() {
        return icheck;
    }

    public SwitchTag setIcheck(boolean icheck) {
        this.icheck = icheck;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public SwitchTag setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
