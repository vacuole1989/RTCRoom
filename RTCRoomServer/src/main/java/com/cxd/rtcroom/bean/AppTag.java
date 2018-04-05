package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class AppTag implements Serializable {
    @Id
    private String appId;
    private String appSecret;
    private String appTemplate;
    private String mchId;
    private String apiKey;
    @NotNull
    private String loginName;
    @NotNull
    private String userPass;
    private String searchSwitch;
    private String key3023;
    private String payDesc;



    public String getAppId() {
        return appId;
    }

    public AppTag setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public AppTag setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public String getAppTemplate() {
        return appTemplate;
    }

    public AppTag setAppTemplate(String appTemplate) {
        this.appTemplate = appTemplate;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public AppTag setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public AppTag setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public String getSearchSwitch() {
        return searchSwitch;
    }

    public AppTag setSearchSwitch(String searchSwitch) {
        this.searchSwitch = searchSwitch;
        return this;
    }

    public String getUserPass() {
        return userPass;
    }

    public AppTag setUserPass(String userPass) {
        this.userPass = userPass;
        return this;
    }

    public String getLoginName() {
        return loginName;
    }

    public AppTag setLoginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public String getKey3023() {
        return key3023;
    }

    public AppTag setKey3023(String key3023) {
        this.key3023 = key3023;
        return this;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public AppTag setPayDesc(String payDesc) {
        this.payDesc = payDesc;
        return this;
    }
}
