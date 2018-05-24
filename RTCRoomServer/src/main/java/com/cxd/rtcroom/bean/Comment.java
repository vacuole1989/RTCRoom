package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 评论表
 */
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    /**
     * 发布用户主键
     */
    private long userSeqId;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 文章主键
     */
    private long articleSeqId;
    /**
     * 评论内容
     */
    @Column(length = 4000)
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;
    @Transient
    private String showTime;


    public long getSeqId() {
        return seqId;
    }

    public Comment setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getUserSeqId() {
        return userSeqId;
    }

    public Comment setUserSeqId(long userSeqId) {
        this.userSeqId = userSeqId;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Comment setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Comment setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public long getArticleSeqId() {
        return articleSeqId;
    }

    public Comment setArticleSeqId(long articleSeqId) {
        this.articleSeqId = articleSeqId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Comment setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Comment setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getShowTime() {
        return showTime;
    }

    public Comment setShowTime(String showTime) {
        this.showTime = showTime;
        return this;
    }
}
