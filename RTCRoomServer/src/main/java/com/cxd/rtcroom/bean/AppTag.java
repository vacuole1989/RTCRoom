package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author cxd
 */
@Entity
public class AppTag implements Serializable {

    private static final long serialVersionUID = 6748058389294881460L;
    @Id
    private String appId;
    private String appSecret;
    private String pushKey;
    private String pushBizId;
    private String appTemplate;
    private String mchId;
    private String apiKey;
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

    public String getPushKey() {
        return pushKey;
    }

    public AppTag setPushKey(String pushKey) {
        this.pushKey = pushKey;
        return this;
    }

    public String getPushBizId() {
        return pushBizId;
    }

    public AppTag setPushBizId(String pushBizId) {
        this.pushBizId = pushBizId;
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

    public String getPayDesc() {
        return payDesc;
    }

    public AppTag setPayDesc(String payDesc) {
        this.payDesc = payDesc;
        return this;
    }
}
