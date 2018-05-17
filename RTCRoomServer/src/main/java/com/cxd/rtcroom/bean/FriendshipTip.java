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
public class FriendshipTip implements Serializable {

    private static final long serialVersionUID = -5512682677765090740L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private long userSeqId;
    private long msg;
    private String createTime;

    public long getSeqId() {
        return seqId;
    }

    public FriendshipTip setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getUserSeqId() {
        return userSeqId;
    }

    public FriendshipTip setUserSeqId(long userSeqId) {
        this.userSeqId = userSeqId;
        return this;
    }

    public long getMsg() {
        return msg;
    }

    public FriendshipTip setMsg(long msg) {
        this.msg = msg;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public FriendshipTip setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }
}
