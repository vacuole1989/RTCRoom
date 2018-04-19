package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query("from UserInfo t where t.online = true and t.seqId <> ?1 and t.onlineStatusTime >= ?2")
    UserInfo findOnlineUser(long seqId,String time);


    UserInfo findUserInfoByOpenId(String openId);

}
