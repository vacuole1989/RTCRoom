package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("from UserInfo t where t.online = ?1 and t.seqId <> ?2 and t.onlineStatusTime >= ?3")
    UserInfo findOnlineUser(boolean online,long seqId,String time);


    UserInfo findUserInfoByOpenId(String openId);

}
