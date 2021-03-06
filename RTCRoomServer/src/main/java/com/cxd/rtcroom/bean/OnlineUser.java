package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cxd
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "contactSeqId"), @UniqueConstraint(columnNames = "contactOpenId")})
public class OnlineUser implements Serializable {


    private static final long serialVersionUID = 3288526458815166860L;
    @Id
    private long seqId;
    @NotNull
    private long contactSeqId;
    @NotNull
    private String contactOpenId;
    @NotNull
    private long expireTime;


    public long getSeqId() {
        return seqId;
    }

    public OnlineUser setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getContactOpenId() {
        return contactOpenId;
    }

    public OnlineUser setContactOpenId(String contactOpenId) {
        this.contactOpenId = contactOpenId;
        return this;
    }


    public long getContactSeqId() {
        return contactSeqId;
    }

    public OnlineUser setContactSeqId(long contactSeqId) {
        this.contactSeqId = contactSeqId;
        return this;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public OnlineUser setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

}
