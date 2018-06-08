package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Diamond implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private long userSeqId;
    private int diamondNum;
    private int fee;
    private String tradeTime;
    private long paySeqId;


    public long getSeqId() {
        return seqId;
    }

    public Diamond setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getUserSeqId() {
        return userSeqId;
    }

    public Diamond setUserSeqId(long userSeqId) {
        this.userSeqId = userSeqId;
        return this;
    }

    public int getDiamondNum() {
        return diamondNum;
    }

    public Diamond setDiamondNum(int diamondNum) {
        this.diamondNum = diamondNum;
        return this;
    }

    public int getFee() {
        return fee;
    }

    public Diamond setFee(int fee) {
        this.fee = fee;
        return this;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public Diamond setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
        return this;
    }

    public long getPaySeqId() {
        return paySeqId;
    }

    public Diamond setPaySeqId(long paySeqId) {
        this.paySeqId = paySeqId;
        return this;
    }

}
