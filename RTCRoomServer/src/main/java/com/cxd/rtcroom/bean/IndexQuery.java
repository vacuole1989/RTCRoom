package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class IndexQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;
    private String model;
    private String capacity;
    private String color;
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

    public IndexQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public IndexQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public IndexQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public IndexQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public IndexQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getCapacity() {
        return capacity;
    }

    public IndexQuery setCapacity(String capacity) {
        this.capacity = capacity;
        return this;
    }

    public String getColor() {
        return color;
    }

    public IndexQuery setColor(String color) {
        this.color = color;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public IndexQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public IndexQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public IndexQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public IndexQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public IndexQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public IndexQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
