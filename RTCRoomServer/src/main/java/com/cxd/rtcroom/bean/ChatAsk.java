package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 朋友消息表
 */
@Entity
public class ChatAsk implements Serializable {

    private static final long serialVersionUID = -5512682677765090740L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private long askUserSeqId;
    private long reciveUserId;
    private String modifyTime;
    private String askTime;
    private String recTime;
    private int agree;
    private String nickName;

    @Transient
    private String playUrl;

    public long getSeqId() {
        return seqId;
    }

    public ChatAsk setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getAskUserSeqId() {
        return askUserSeqId;
    }

    public ChatAsk setAskUserSeqId(long askUserSeqId) {
        this.askUserSeqId = askUserSeqId;
        return this;
    }

    public long getReciveUserId() {
        return reciveUserId;
    }

    public ChatAsk setReciveUserId(long reciveUserId) {
        this.reciveUserId = reciveUserId;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public ChatAsk setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ChatAsk setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public ChatAsk setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
        return this;
    }

    public int getAgree() {
        return agree;
    }

    public ChatAsk setAgree(int agree) {
        this.agree = agree;
        return this;
    }

    public String getRecTime() {
        return recTime;
    }

    public ChatAsk setRecTime(String recTime) {
        this.recTime = recTime;
        return this;
    }

    public String getAskTime() {
        return askTime;
    }

    public ChatAsk setAskTime(String askTime) {
        this.askTime = askTime;
        return this;
    }
}
