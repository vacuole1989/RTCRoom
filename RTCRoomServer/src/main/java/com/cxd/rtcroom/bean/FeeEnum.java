package com.cxd.rtcroom.bean;

import com.cxd.rtcroom.dao.FeeEnumRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FeeEnum implements Serializable {

//    APPRAISAL("appraisal", "1", "序列号/IMEI码深度查询"),
//    CN("cn", "1", "鉴别行货正品（国行）"),
//    ACTIVATIONLOCK("activationlock", "1.5", "苹果激活锁查询"),
//    ICLOUD("icloud", "1.8", "ID黑白名单查询"),
//    REPAIR("repair", "1.5", "苹果维修记录查询"),
//    SOLD("sold", "5", "国行、GSX销售地查询"),
//    SERIAL("serial", "1", "鉴定翻新机、妖机、黑机查询"),
//    SIMLOCK("simlock", "3", "运营商SIM卡网络锁查询");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;
    private String appId;
    private String type;
    private String name;
    private String fee;
    @Transient
    private boolean iSwitch;
    private String showTitle;
    @JsonIgnore
    @Transient
    private List<FeeEnum> list = new ArrayList<>();

    private FeeEnum() {
    }

    public FeeEnum(String appId, FeeEnumRepository feeEnumRepository) {
        List<FeeEnum> feeEnums = feeEnumRepository.findFeeEnumsByAppId(appId);
        if (feeEnums.size() <= 0) {
            List<FeeEnum> feeEnumList = new ArrayList<>();
            FeeEnum fee = new FeeEnum().setAppId(appId).setType("appraisal").setName("序列号/IMEI码深度查询").setFee("1");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("cn").setName("鉴别行货正品（国行）").setFee("1");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("activationlock").setName("苹果激活锁查询").setFee("1.5");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("icloud").setName("ID黑白名单查询").setFee("1.8");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("repair").setName("苹果维修记录查询").setFee("1.5");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("sold").setName("国行、GSX销售地查询").setFee("5");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("serial").setName("鉴定翻新机、妖机、黑机查询").setFee("1");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("simlock").setName("运营商SIM卡网络锁查询").setFee("3");
            feeEnumList.add(fee.setShowTitle(fee.getName() + " " + fee.getFee() + "元/次"));
            fee = new FeeEnum().setAppId(appId).setType("instructions").setName("查询说明书鉴定指南").setFee("0");
            feeEnumList.add(fee.setShowTitle("查询说明书鉴定指南"));
            feeEnumRepository.save(feeEnumList);
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

    public  List<FeeEnum> validateSwitch(AppTag appTag) {
        List<FeeEnum> feeEnums = new ArrayList<>();
        String[] str = (null == appTag.getSearchSwitch() ? "" : appTag.getSearchSwitch()).split(",");
        for (FeeEnum fee : this.getList()) {
            boolean f = false;
            for (String s : str) {
                if (fee.getType().equalsIgnoreCase(s)) {
                    f = true;
                }
            }
            feeEnums.add(fee.setiSwitch(f));
        }

        return feeEnums;
    }

    public List<FeeEnum> validateSwitchNotAll(AppTag appTag) {
        List<FeeEnum> feeEnums = new ArrayList<>();
        String[] str = (null == appTag.getSearchSwitch() ? "" : appTag.getSearchSwitch()).split(",");
        for (FeeEnum fee : this.getList()) {
            for (String s : str) {
                if (fee.getType().equalsIgnoreCase(s)) {
                    feeEnums.add(fee.setiSwitch(true));
                }
            }
        }
        return feeEnums;
    }


    public String getName() {
        return name;
    }

    public FeeEnum setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isiSwitch() {
        return iSwitch;
    }

    public FeeEnum setiSwitch(boolean iSwitch) {
        this.iSwitch = iSwitch;
        return this;
    }


    public String getShowTitle() {
        return showTitle;
    }

    public FeeEnum setShowTitle(String showTitle) {
        this.showTitle = showTitle;
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

    public String getFee() {
        return fee;
    }

    public FeeEnum setFee(String fee) {
        this.fee = fee;
        return this;
    }


}
