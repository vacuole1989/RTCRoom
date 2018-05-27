package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.ChatMsg;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ChatMsgRepository extends CrudRepository<ChatMsg, Long> {

    @Query("from ChatMsg where (fromUserSeqId = ?1 and toUserSeqId = ?2) or (fromUserSeqId = ?3 and toUserSeqId = ?4) order by createTime asc ")
    List<ChatMsg> findChatMsg(long fromUserSeqId, long toUserSeqId, long toUserSeqId1, long fromUserSeqId1);

    @Query("select count(seqId) from ChatMsg where fromUserSeqId = ?1 and toUserSeqId = ?2 and iread=false")
    long findUnReadChatMsgCount(long toUserSeqId1, long fromUserSeqId1);

    @Query("select count(seqId) from ChatMsg where toUserSeqId = ?1 and iread=false")
    long findAllUnReadChatMsgCount(long toUserSeqId1);

    @Query("from ChatMsg where fromUserSeqId = ?1 and toUserSeqId = ?2 and seqId > ?3")
    List<ChatMsg> findNewChatMsg(long toUserSeqId, long fromUserSeqId,long seqId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ChatMsg set iread=true where fromUserSeqId = ?1 and toUserSeqId = ?2 and iread = false")
    int updateChatMsgRead(long toUserSeqId, long fromUserSeqId);
}
