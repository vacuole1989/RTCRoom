package com.cxd.rtcroom.bean;

import java.io.Serializable;

/**
 * @author cxd
 */
public class UserInfo implements Serializable {


    private String nickName;
    private String avatarUrl;
    private String gender;
    private String province;
    private String city;
    private String country;

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
}
