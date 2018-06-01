package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.ChatAsk;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatAskRepository extends CrudRepository<ChatAsk, Long> {
    @Query("from ChatAsk t where t.reciveUserId = ?1 and t.modifyTime >= ?2 and t.agree=0")
    List<ChatAsk> findIFAsked(long reciveSeqId,String time);

}
