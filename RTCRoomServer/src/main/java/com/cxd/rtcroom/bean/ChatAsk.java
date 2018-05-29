package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private boolean agree;
    private String playUrl;
    private String nickName;

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

    public boolean isAgree() {
        return agree;
    }

    public ChatAsk setAgree(boolean agree) {
        this.agree = agree;
        return this;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public ChatAsk setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ChatAsk setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
