package com.cxd.rtcroom.util;


import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.dao.AppTagRepository;

public class ConstantUtil {


    private String appId;
    private String appSecret;
    private String messageTemplate;
    private String mchId;
    private String apiKey;
    private String payDesc;
    private String notifyUrl = "https://wujieshidai.com/RTCRoomServer/app/notify";
    private String getSessionKeyUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";


    public ConstantUtil(String appId, AppTagRepository appTagRepository) {
        AppTag one = appTagRepository.findOne(appId);
        this.appId = one.getAppId();
        this.appSecret = one.getAppSecret();
        this.messageTemplate = one.getAppTemplate();
        this.mchId = one.getMchId();
        this.apiKey = one.getApiKey();
        this.payDesc=one.getPayDesc();
    }

    public String getAppId() {
        return appId;
    }

    public ConstantUtil setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public ConstantUtil setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public ConstantUtil setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public ConstantUtil setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public ConstantUtil setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getGetSessionKeyUrl() {
        return getSessionKeyUrl;
    }

    public ConstantUtil setGetSessionKeyUrl(String getSessionKeyUrl) {
        this.getSessionKeyUrl = getSessionKeyUrl;
        return this;
    }

    public String getUnifiedorderUrl() {
        return unifiedorderUrl;
    }

    public ConstantUtil setUnifiedorderUrl(String unifiedorderUrl) {
        this.unifiedorderUrl = unifiedorderUrl;
        return this;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public ConstantUtil setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public ConstantUtil setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }


    public String getPayDesc() {
        return payDesc;
    }

    public ConstantUtil setPayDesc(String payDesc) {
        this.payDesc = payDesc;
        return this;
    }
}
