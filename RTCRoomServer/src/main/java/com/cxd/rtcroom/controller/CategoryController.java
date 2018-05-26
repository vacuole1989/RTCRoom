package com.cxd.rtcroom.controller;


import com.cxd.rtcroom.bean.Category;
import com.cxd.rtcroom.dao.CategoryRepository;
import com.cxd.rtcroom.dto.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/app/{appId}")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping("/getIndexCategory")
    public JSONResult getArticleList(@PathVariable String appId) {
        List<Category> byTypeAndVideo = categoryRepository.findByTypeAndVideo(0, true);
        return new JSONResult(true, "查找成功", byTypeAndVideo);

    }



}
