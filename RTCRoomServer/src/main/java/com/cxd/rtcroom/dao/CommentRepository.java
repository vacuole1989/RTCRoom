package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("from Comment where articleSeqId = ?1 order by createTime desc")
    List<Comment> findComment(long articleSeqId);



}
