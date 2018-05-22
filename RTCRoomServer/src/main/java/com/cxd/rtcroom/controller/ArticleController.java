package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.Article;
import com.cxd.rtcroom.dao.ArticleRepository;
import com.cxd.rtcroom.dto.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/app/{appId}")
public class ArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ArticleRepository articleRepository;


    @RequestMapping("/getLastArticleList")
    public JSONResult getArticleList(@PathVariable String appId) {
        List<Article> byUserSeqId = articleRepository.findArticleList(0L, 5);
        return new JSONResult(true, "查找成功", byUserSeqId);

    }

    @RequestMapping("/getNewArticleList")
    public JSONResult getNewArticleList(@PathVariable String appId, @RequestBody Article article) {

        List<Article> byUserSeqId = articleRepository.findNewArticleList(0L, article.getSeqId(),"%"+article.getTitle()+"%", 5);
        return new JSONResult(true, "查找成功", byUserSeqId);

    }

    @RequestMapping("/getPreArticleList")
    public JSONResult getPreArticleList(@PathVariable String appId, @RequestBody Article article) {
        List<Article> byUserSeqId = articleRepository.findPreArticleList(0L, article.getSeqId(), "%"+article.getTitle()+"%",5);
        return new JSONResult(true, "查找成功", byUserSeqId);

    }

    @RequestMapping("/getArticleById")
    public JSONResult getArticleById(@PathVariable String appId, long seqId) {
        Article one = articleRepository.findOne(seqId);
        return new JSONResult(true, "查找成功", one);

    }

    @RequestMapping("/searchArticleList")
    public JSONResult searchArticleList(@PathVariable String appId, @RequestBody Article article) {
        List<Article> byTitle = articleRepository.findByTitle(0L, "%"+article.getTitle()+"%",5);
        return new JSONResult(true, "查找成功", byTitle);

    }


}
