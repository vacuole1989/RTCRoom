package com.cxd.rtcroom.controller;


import com.cxd.rtcroom.bean.Comment;
import com.cxd.rtcroom.bean.UserInfo;
import com.cxd.rtcroom.dao.AppTagRepository;
import com.cxd.rtcroom.dao.CommentRepository;
import com.cxd.rtcroom.dao.FriendshipRepository;
import com.cxd.rtcroom.dao.UserInfoRepository;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/{appId}")
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AppTagRepository appTagRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;


    @RequestMapping("/getComment")
    public JSONResult getComment(@PathVariable String appId, long seqId) {
        List<Comment> comment = commentRepository.findComment(seqId);
        for (Comment comment1 : comment) {
            long now = System.currentTimeMillis();
            long crt = DateUtil.format(comment1.getCreateTime()).getTime();
            if ((now - crt) < (60 * 1000L)) {
                comment1.setShowTime((int) ((now - crt) / 1000) + "秒前");
            } else if ((now - crt) < (60 * 60 * 1000L)) {
                comment1.setShowTime((int) ((now - crt) / 60 / 1000) + "分钟前");
            } else if ((now - crt) < (24 * 60 * 60 * 1000L)) {
                comment1.setShowTime((int) ((now - crt) / 60 / 60 / 1000) + "小时前");
            } else if ((now - crt) < (365 * 24 * 60 * 60 * 1000L)) {
                comment1.setShowTime((int) ((now - crt) / 24 / 60 / 60 / 1000) + "天前");
            }
        }
        return new JSONResult(true, "查找成功", comment);

    }

    @RequestMapping("/sendComment")
    public JSONResult sendComment(@PathVariable String appId, @RequestBody Map getmap) {
        long articleSeqId = Long.parseLong(getmap.get("articleSeqId") + "");
        String content = getmap.get("content") + "";
        long userSeqId = Long.parseLong(getmap.get("userSeqId") + "");

        UserInfo userInfo = userInfoRepository.findOne(userSeqId);
        Comment comment = new Comment();
        comment.setArticleSeqId(articleSeqId);
        comment.setAvatarUrl(userInfo.getAvatarUrl());
        comment.setContent(content);
        comment.setCreateTime(DateUtil.format(new Date()));
        comment.setModifyTime(DateUtil.format(new Date()));
        comment.setNickName(userInfo.getNickName());
        comment.setUserSeqId(userInfo.getSeqId());
        commentRepository.save(comment);
        return new JSONResult(true, "保存成功", comment);

    }


}
