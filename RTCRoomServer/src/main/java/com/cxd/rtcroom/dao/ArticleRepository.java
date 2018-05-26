package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {

    @Query(nativeQuery = true,value = "select * from article where user_Seq_Id = ?1 order by create_Time desc,seq_Id desc limit ?2")
    List<Article> findArticleList(long userSeqId, long count);

    @Query(nativeQuery = true,value = "select * from Article where user_Seq_Id=?1 and seq_Id > ?2 and title like ?3 order by create_Time desc,seq_Id desc limit ?4")
    List<Article> findNewArticleList(long userSeqId, long seqId,String title,long count);

    @Query(nativeQuery = true,value = "select * from Article where user_Seq_Id=?1 and seq_Id < ?2 and title like ?3 order by create_Time desc,seq_Id desc limit ?4")
    List<Article> findPreArticleList(long userSeqId,long seqId, String title,long count);

    @Query(nativeQuery = true,value = "select * from Article where user_seq_id=?1 and title like ?2 order by create_Time desc,seq_Id desc limit ?3")
    List<Article> findByTitle(long userSeqId,String title,long count);
}
