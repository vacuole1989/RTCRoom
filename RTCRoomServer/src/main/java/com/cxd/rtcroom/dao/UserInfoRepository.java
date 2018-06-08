package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("from UserInfo t where t.online = true and t.seqId <> ?1 and t.onlineStatusTime >= ?2 and t.gender= ?3 and t.askGender <> ?4")
    UserInfo findOnlineUser(long seqId,long time,int gender,int notAskGender);

    @Query("from UserInfo t where t.online = true and t.seqId <> ?1 and t.onlineStatusTime >= ?2 and t.askGender <> ?3")
    UserInfo findOnlineUserAll(long seqId,long time,int notAskGender);


    UserInfo findUserInfoByOpenId(String openId);

}
