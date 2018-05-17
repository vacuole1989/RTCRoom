package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 文章分类表
 */
@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 视频分类还是文章分类
     */
    private int type;

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public long getSeqId() {
        return seqId;
    }

    public Category setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public int getType() {
        return type;
    }

    public Category setType(int type) {
        this.type = type;
        return this;
    }
}
