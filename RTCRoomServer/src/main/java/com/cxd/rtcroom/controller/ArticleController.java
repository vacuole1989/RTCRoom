package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.dto.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/app/{appId}")
public class ArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/getArticleList")
    public JSONResult getArticleList(@PathVariable String appId) {

        return null;

    }



}
