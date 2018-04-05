package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ActivationlockQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

    private String locked;

    private String time;

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

    public ActivationlockQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public ActivationlockQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public ActivationlockQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ActivationlockQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ActivationlockQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getLocked() {
        return locked;
    }

    public ActivationlockQuery setLocked(String locked) {
        this.locked = locked;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ActivationlockQuery setTime(String time) {
        this.time = time;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public ActivationlockQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public ActivationlockQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public ActivationlockQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ActivationlockQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ActivationlockQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public ActivationlockQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
