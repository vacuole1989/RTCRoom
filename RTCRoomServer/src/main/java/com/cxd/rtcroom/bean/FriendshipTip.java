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
    private long fromUserId;
    private String createTime;
    private String modifyTime;
    private boolean agree;
    private boolean iread;
    private String avatarUrl;
    private String nickName;

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


    public String getCreateTime() {
        return createTime;
    }

    public FriendshipTip setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public FriendshipTip setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }

    public boolean isAgree() {
        return agree;
    }

    public FriendshipTip setAgree(boolean agree) {
        this.agree = agree;
        return this;
    }


    public boolean isIread() {
        return iread;
    }

    public FriendshipTip setIread(boolean iread) {
        this.iread = iread;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public FriendshipTip setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public FriendshipTip setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public FriendshipTip setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
