package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 文章表
 */
@Entity
public class Article implements Serializable {
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
     * 文章标题
     */
    private String title;
    /**
     * 简介
     */
    private String memo;
    /**
     * 文章内容
     */
    @Column(length = 4000)
    private String content;
    /**
     * 点击次数
     */
    private int clickTimes;
    /**
     * 评论数
     */
    private int commentCounts;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 文章分类主键
     */
    private long categorySeqId;


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

    public long getUserSeqId() {
        return userSeqId;
    }

    public Article setUserSeqId(long userSeqId) {
        this.userSeqId = userSeqId;
        return this;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public Article setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
        return this;
    }

    public long getCategorySeqId() {
        return categorySeqId;
    }

    public Article setCategorySeqId(long categorySeqId) {
        this.categorySeqId = categorySeqId;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public Article setMemo(String memo) {
        this.memo = memo;
        return this;
    }
}
