package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.Category;
import com.cxd.rtcroom.bean.Video;
import com.cxd.rtcroom.dao.CategoryRepository;
import com.cxd.rtcroom.dao.VideoRepository;
import com.cxd.rtcroom.dto.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/{appId}")
public class VideoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping("/getVideoList")
    public JSONResult getArticleList(@PathVariable String appId) {
        List<Video> swiperVideo = videoRepository.findSwiperVideo(0L, 3);
        List<Video> indexVideo = videoRepository.findIndexVideo(0L, 8);
        List<Category> byTypeAndVideo = categoryRepository.findByTypeAndVideo(0, true);
        Map<String, Object> map = new HashMap<>(3);
        map.put("swiper", swiperVideo);
        map.put("index", indexVideo);
        map.put("category", byTypeAndVideo);
        return new JSONResult(true, "查找成功", map);

    }

    @RequestMapping("/getVideoById")
    public JSONResult getVideoById(@PathVariable String appId, long seqId) {
        Video one = videoRepository.findOne(seqId);
        List<Video> byCategorySeqId = videoRepository.findByCategorySeqId(one.getCategorySeqId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("video",one);
        map.put("list",byCategorySeqId);
        return new JSONResult(true, "查找成功", map);

    }


    @RequestMapping("/getVideoByCategory")
    public JSONResult getVideoByCategory(@PathVariable String appId, long categorySeqId) {
        List<Video> byCategorySeqId = videoRepository.findByCategorySeqId(categorySeqId);
        return new JSONResult(true, "查找成功", byCategorySeqId);

    }



}
