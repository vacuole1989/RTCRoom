package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 聊天内容表
 */
@Entity
public class ChatMsg implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    /**
     * 发布用户主键
     */
    private long fromUserSeqId;
    /**
     * 接收用户主键
     */
    private long toUserSeqId;
    /**
     * 聊天内容
     */
    private String content="";
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否已读
     */
    private boolean iread;

    /**
     * 0是普通消息，1是礼物消息
     */
    private int itype=0;

    public long getSeqId() {
        return seqId;
    }

    public ChatMsg setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getFromUserSeqId() {
        return fromUserSeqId;
    }

    public ChatMsg setFromUserSeqId(long fromUserSeqId) {
        this.fromUserSeqId = fromUserSeqId;
        return this;
    }

    public long getToUserSeqId() {
        return toUserSeqId;
    }

    public ChatMsg setToUserSeqId(long toUserSeqId) {
        this.toUserSeqId = toUserSeqId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatMsg setContent(String content) {
        this.content = content;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ChatMsg setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public boolean isIread() {
        return iread;
    }

    public ChatMsg setIread(boolean iread) {
        this.iread = iread;
        return this;
    }

    public int getItype() {
        return itype;
    }

    public ChatMsg setItype(int itype) {
        this.itype = itype;
        return this;
    }
}
