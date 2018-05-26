package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Friendship;
import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    @Query("from UserInfo u,Friendship f where f.friendSeqId = u.seqId and f.ownerSeqId = ?1 ")
    List<UserInfo> findFriends(long seqId);

    @Query("from Friendship where (friendSeqId = ?1 and ownerSeqId = ?2) or (friendSeqId = ?3 and ownerSeqId = ?4)")
    List<Friendship> findIfFriend(long userSeqId,long friendSeqId,long friendSeqId2,long userSeqId2);
}
