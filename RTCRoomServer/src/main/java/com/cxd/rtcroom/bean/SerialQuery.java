package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SerialQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

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

    public SerialQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public SerialQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public SerialQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SerialQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public SerialQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public SerialQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public SerialQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public SerialQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SerialQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SerialQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public SerialQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
