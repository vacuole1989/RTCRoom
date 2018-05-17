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
public class TagRelationship implements Serializable {
    private static final long serialVersionUID = 7452723379712667522L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private long articleSeqId;

    private long tagSeqId;

    public long getSeqId() {
        return seqId;
    }

    public TagRelationship setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public long getArticleSeqId() {
        return articleSeqId;
    }

    public TagRelationship setArticleSeqId(long articleSeqId) {
        this.articleSeqId = articleSeqId;
        return this;
    }


    public long getTagSeqId() {
        return tagSeqId;
    }

    public TagRelationship setTagSeqId(long tagSeqId) {
        this.tagSeqId = tagSeqId;
        return this;
    }
}
