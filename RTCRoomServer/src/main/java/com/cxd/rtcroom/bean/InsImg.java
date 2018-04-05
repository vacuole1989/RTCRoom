package com.cxd.rtcroom.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class InsImg implements Serializable {
    @Id
    private String appId;
    @Column(length = Integer.MAX_VALUE)
    private String baseStr;

    public String getAppId() {
        return appId;
    }

    public InsImg setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getBaseStr() {
        return baseStr;
    }

    public InsImg setBaseStr(String baseStr) {
        this.baseStr = baseStr;
        return this;
    }
}
