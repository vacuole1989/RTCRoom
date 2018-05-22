package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoRepository extends CrudRepository<Video, Long> {
    @Query(nativeQuery = true,value = "select * from video where category_seq_id = ?1 order by modify_time desc limit ?2")
    List<Video> findSwiperVideo(long categorySeqId, int count);

    @Query(nativeQuery = true,value = "select * from video where category_seq_id <> ?1 order by modify_time desc limit ?2")
    List<Video> findIndexVideo(long categorySeqId, int count);


    List<Video> findByCategorySeqId(long categorySeqId);

}
