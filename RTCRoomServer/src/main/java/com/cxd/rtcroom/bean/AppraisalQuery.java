package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AppraisalQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

    private String storage;

    private String purchaseDate;
    private String support;
    private String daysleft;
    private String applecare;
    private String status;
    private String manufactureFactory;

    @Column(length = 4000)
    private String returnBody;

    private String ipAddr;

    private String crtTim;

    private String code;

    private String message;

    private String nonceStr;


    public long getSeqId() {
        return seqId;
    }

    public AppraisalQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public AppraisalQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public AppraisalQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AppraisalQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public AppraisalQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getStorage() {
        return storage;
    }

    public AppraisalQuery setStorage(String storage) {
        this.storage = storage;
        return this;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public AppraisalQuery setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public String getSupport() {
        return support;
    }

    public AppraisalQuery setSupport(String support) {
        this.support = support;
        return this;
    }

    public String getDaysleft() {
        return daysleft;
    }

    public AppraisalQuery setDaysleft(String daysleft) {
        this.daysleft = daysleft;
        return this;
    }

    public String getApplecare() {
        return applecare;
    }

    public AppraisalQuery setApplecare(String applecare) {
        this.applecare = applecare;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AppraisalQuery setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getManufactureFactory() {
        return manufactureFactory;
    }

    public AppraisalQuery setManufactureFactory(String manufactureFactory) {
        this.manufactureFactory = manufactureFactory;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public AppraisalQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public AppraisalQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public AppraisalQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AppraisalQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AppraisalQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public AppraisalQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
