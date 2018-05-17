package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 视频表
 */
@Entity
public class Video implements Serializable {
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
     * 视频标题
     */
    private String title;
    /**
     * 视频图片
     */
    private String headImg;
    /**
     * 视频简介
     */
    private String memo;
    /**
     * 视频地址
     */
    private String videoUrl;
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
     * 视频分类主键
     */
    private long categorySeqId;


    public long getSeqId() {
        return seqId;
    }

    public Video setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getUserSeqId() {
        return userSeqId;
    }

    public Video setUserSeqId(long userSeqId) {
        this.userSeqId = userSeqId;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Video setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Video setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Video setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getHeadImg() {
        return headImg;
    }

    public Video setHeadImg(String headImg) {
        this.headImg = headImg;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public Video setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Video setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public Video setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
        return this;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public Video setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Video setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Video setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public long getCategorySeqId() {
        return categorySeqId;
    }

    public Video setCategorySeqId(long categorySeqId) {
        this.categorySeqId = categorySeqId;
        return this;
    }
}
