package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.FriendshipTip;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipTipRepository extends CrudRepository<FriendshipTip, Long> {


    long countByUserSeqIdAndIread(long userSeqId,boolean iread);
    List<FriendshipTip> findByUserSeqIdOrderByCreateTimeDesc(long userSeqId);

    List<FriendshipTip> findByUserSeqIdAndFromUserId(long userSeqId,long fromUserId);

}
