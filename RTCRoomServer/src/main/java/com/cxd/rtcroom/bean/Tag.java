package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 文章表标签表
 */
@Entity
public class Tag implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 视频标签还是文章标签
     */
    private int type;

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public long getSeqId() {
        return seqId;
    }

    public Tag setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public int getType() {
        return type;
    }

    public Tag setType(int type) {
        this.type = type;
        return this;
    }
}
