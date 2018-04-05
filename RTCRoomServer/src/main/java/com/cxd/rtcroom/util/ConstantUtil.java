package com.cxd.rtcroom.util;

import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.dao.AppTagRepository;
import org.springframework.util.StringUtils;

public class ConstantUtil {


    private String appId;
    private String appSecret;
    private String messageTemplate;
    private String mchId;
    private String apiKey;
    private String key3023;
    private String payDesc;
    private String api3023 = "https://api.3023.com/apple/";
    private String notifyUrl = "https://applebaoxiu.wang/SNQuery/app/notify";
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
        this.key3023= StringUtils.isEmpty(one.getKey3023())?"bb5a29dd9977c8f254f363b0ddb2bb3a":one.getKey3023();
        this.payDesc=StringUtils.isEmpty(one.getPayDesc())?"序列号查询/IMEI码":one.getPayDesc();
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

    public String getKey3023() {
        return key3023;
    }

    public ConstantUtil setKey3023(String key3023) {
        this.key3023 = key3023;
        return this;
    }

    public String getApi3023() {
        return api3023;
    }

    public ConstantUtil setApi3023(String api3023) {
        this.api3023 = api3023;
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
