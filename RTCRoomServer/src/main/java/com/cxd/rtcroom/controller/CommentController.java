package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.AppTag;
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

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/{appId}")
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
    private static final String SESSION_KEY = "https://api.weixin.qq.com/sns/jscode2session";
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

    private String stream2String(ServletInputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuffer sb = new StringBuffer("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        return sb.toString();
    }

    @RequestMapping("/sendComment")
    public JSONResult sendComment(@PathVariable String appId,  @RequestBody Map getmap) {
        long articleSeqId = Long.parseLong(getmap.get("articleSeqId") + "");
        String content = getmap.get("content") + "";
        String code = getmap.get("code") + "";

        System.out.println(content);

        AppTag appTag = appTagRepository.findOne(appId);
        Map map = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Map.class, SESSION_KEY, appTag.getAppId(), appTag.getAppSecret(), code);

        if (null != map.get("openid")) {
            String openid = map.get("openid") + "";
            UserInfo userInfoByOpenId = userInfoRepository.findUserInfoByOpenId(openid);
            Comment comment = new Comment();
            comment.setArticleSeqId(articleSeqId);
            comment.setAvatarUrl(userInfoByOpenId.getAvatarUrl());
            comment.setContent(content);
            comment.setCreateTime(DateUtil.format(new Date()));
            comment.setModifyTime(DateUtil.format(new Date()));
            comment.setNickName(userInfoByOpenId.getNickName());
            comment.setUserSeqId(userInfoByOpenId.getSeqId());
            commentRepository.save(comment);
            return new JSONResult(true, "保存成功", comment);
        } else {
            return new JSONResult(false, "保存失败");
        }


    }


}
