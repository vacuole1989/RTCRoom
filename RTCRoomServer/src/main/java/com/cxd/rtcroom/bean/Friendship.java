package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class Friendship implements Serializable {

    private static final long serialVersionUID = -5512682677765090740L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private long ownerSeqId;
    private long friendSeqId;
    private String createTime;
    private String modifyTime;


    public long getSeqId() {
        return seqId;
    }

    public Friendship setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getOwnerSeqId() {
        return ownerSeqId;
    }

    public Friendship setOwnerSeqId(long ownerSeqId) {
        this.ownerSeqId = ownerSeqId;
        return this;
    }


    public long getFriendSeqId() {
        return friendSeqId;
    }

    public Friendship setFriendSeqId(long friendSeqId) {
        this.friendSeqId = friendSeqId;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Friendship setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Friendship setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }
}
