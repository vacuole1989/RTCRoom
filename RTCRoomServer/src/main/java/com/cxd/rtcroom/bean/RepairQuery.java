package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class RepairQuery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String sn;

    private String imei;

    private String url;

    private String model;

    private String sales;
    private String status;

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

    public RepairQuery setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getSn() {
        return sn;
    }

    public RepairQuery setSn(String sn) {
        this.sn = sn;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public RepairQuery setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RepairQuery setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getModel() {
        return model;
    }

    public RepairQuery setModel(String model) {
        this.model = model;
        return this;
    }

    public String getSales() {
        return sales;
    }

    public RepairQuery setSales(String sales) {
        this.sales = sales;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RepairQuery setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public RepairQuery setReturnBody(String returnBody) {
        this.returnBody = returnBody;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public RepairQuery setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getCrtTim() {
        return crtTim;
    }

    public RepairQuery setCrtTim(String crtTim) {
        this.crtTim = crtTim;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RepairQuery setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RepairQuery setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public RepairQuery setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }
}
