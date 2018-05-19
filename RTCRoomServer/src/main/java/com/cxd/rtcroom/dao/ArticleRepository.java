package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
