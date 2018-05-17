package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Article implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private long userId;
    private String avatarUrl;
    private String nickName;
    private String title;
    @Column(length = 4000)
    private String content;
    private int clickTimes;
    private String createTime;
    private String modifyTime;

    public long getUserId() {
        return userId;
    }

    public Article setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getSeqId() {
        return seqId;
    }

    public Article setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Article setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Article setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Article setContent(String content) {
        this.content = content;
        return this;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public Article setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Article setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Article setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }
}
