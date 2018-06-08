package com.cxd.rtcroom.bean;


import com.cxd.rtcroom.dao.FeeEnumRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FeeEnum implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private String appId;
    private String type;
    private String name;
    private String imgUrl;
    private int diamondNum;
    private int orderNo;
    @JsonIgnore
    @Transient
    private List<FeeEnum> list = new ArrayList<>();


    public FeeEnum() {
    }

    public FeeEnum(String appId, FeeEnumRepository feeEnumRepository) {
        List<FeeEnum> feeEnums = feeEnumRepository.findFeeEnumsByAppIdOrderByOrderNoAsc(appId);
        if (feeEnums.size() <= 0) {

        } else {
            this.list = feeEnums;
        }
    }

    public FeeEnum getFeeByType(String type) {
        for (FeeEnum fee : this.getList()) {
            if (fee.getType().equalsIgnoreCase(type)) {
                return fee;
            }
        }
        return null;

    }


    public String getName() {
        return name;
    }

    public FeeEnum setName(String name) {
        this.name = name;
        return this;
    }

    public long getSeqId() {
        return seqId;
    }

    public FeeEnum setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public List<FeeEnum> getList() {
        return list;
    }

    public FeeEnum setList(List<FeeEnum> list) {
        this.list = list;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public FeeEnum setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getType() {
        return type;
    }

    public FeeEnum setType(String type) {
        this.type = type;
        return this;
    }


    public int getOrderNo() {
        return orderNo;
    }

    public FeeEnum setOrderNo(int orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public int getDiamondNum() {
        return diamondNum;
    }

    public FeeEnum setDiamondNum(int diamondNum) {
        this.diamondNum = diamondNum;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public FeeEnum setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
}
