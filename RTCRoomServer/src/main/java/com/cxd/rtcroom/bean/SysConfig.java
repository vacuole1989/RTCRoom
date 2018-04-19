package com.cxd.rtcroom.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cxd
 */
@Entity
public class SysConfig implements Serializable {

    private static final long serialVersionUID = -7730402329489131926L;
    @Id
    private long seqId;
    @NotNull
    private String value;

    public String getValue() {
        return value;
    }

    public SysConfig setValue(String value) {
        this.value = value;
        return this;
    }


    public long getSeqId() {
        return seqId;
    }

    public SysConfig setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }
}
