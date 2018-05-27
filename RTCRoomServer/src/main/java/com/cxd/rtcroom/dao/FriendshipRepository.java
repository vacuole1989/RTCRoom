package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Friendship;
import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    @Query("select u from UserInfo u,Friendship f where (f.ownerSeqId = ?1 and f.friendSeqId = u.seqId) or (f.friendSeqId = ?2 and f.ownerSeqId = u.seqId)")
    List<UserInfo> findFriends(long seqId,long seqId2);

    @Query("from Friendship where (friendSeqId = ?1 and ownerSeqId = ?2) or (friendSeqId = ?3 and ownerSeqId = ?4)")
    List<Friendship> findIfFriend(long userSeqId,long friendSeqId,long friendSeqId2,long userSeqId2);
}
