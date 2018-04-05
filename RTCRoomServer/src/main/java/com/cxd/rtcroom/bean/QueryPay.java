package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class QueryPay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String totalFee;
    private String description;
    private String code;
    private String openid;
    private String outTradeNo;
    private String ip;
    @Column(length = 4000)
    private String sendXml;
    @Column(length = 4000)
    private String reciveXml;
    private String returnCode;
    private String returnMsg;
    private String nonceStr;
    private String resultCode;
    private String prepayId;
    private String timeStamp;
    private String crtTim;

    public long getSeqId() {
        return seqId;
    }

    public QueryPay setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public QueryPay setTotalFee(String totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public QueryPay setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCode() {
        return code;
    }

    public QueryPay setCode(String code) {
        this.code = code;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public QueryPay setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public QueryPay setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public QueryPay setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getSendXml() {
        return sendXml;
    }

    public QueryPay setSendXml(String sendXml) {
        this.sendXml = sendXml;
        return this;
    }

    public String getReciveXml() {
        return reciveXml;
    }

    public QueryPay setReciveXml(String reciveXml) {
        this.reciveXml = reciveXml;
        return this;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public QueryPay setReturnCode(String returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public QueryPay setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public QueryPay setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public QueryPay setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public QueryPay setPrepayId(String prepayId) {
        this.prepayId = prepayId;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public QueryPay setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public QueryPay setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }
}
