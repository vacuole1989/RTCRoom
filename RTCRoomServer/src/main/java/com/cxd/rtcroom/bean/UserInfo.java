package com.cxd.rtcroom.bean;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author cxd
 */
@Entity
public class UserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long seqId;

    private String openId;
    private String sessionKey;
    private String nickName;
    private String avatarUrl;
    private String gender;
    private String province;
    private String city;
    private String country;
    private String lastLoginTime;
    private boolean inited;
    @Transient
    private String pushUrl;
    @Transient
    private String playUrl;

    public long getSeqId() {
        return seqId;
    }

    public UserInfo setSeqId(long seqId) {
        this.seqId = seqId;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public UserInfo setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public UserInfo setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public UserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserInfo setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public UserInfo setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public UserInfo setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserInfo setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserInfo setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public UserInfo setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public boolean isInited() {
        return inited;
    }

    public UserInfo setInited(boolean inited) {
        this.inited = inited;
        return this;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public UserInfo setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
        return this;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public UserInfo setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
        return this;
    }
}
