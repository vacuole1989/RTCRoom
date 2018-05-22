package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.bean.Friendship;
import com.cxd.rtcroom.bean.UserInfo;
import com.cxd.rtcroom.dao.AppTagRepository;
import com.cxd.rtcroom.dao.FriendshipRepository;
import com.cxd.rtcroom.dao.UserInfoRepository;
import com.cxd.rtcroom.dto.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/app/{appId}")
public class FriendshipController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipController.class);
    private static final String SESSION_KEY = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AppTagRepository appTagRepository;

    @RequestMapping("/getFriends")
    public JSONResult getFriends(@PathVariable String appId, String code) {
        AppTag appTag = appTagRepository.findOne(appId);
        Map map = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Map.class, SESSION_KEY, appTag.getAppId(), appTag.getAppSecret(), code);

        if (null != map.get("openid")) {
            String openid = map.get("openid") + "";
            UserInfo userInfoByOpenId = userInfoRepository.findUserInfoByOpenId(openid);
            List<UserInfo> friends = friendshipRepository.findFriends(userInfoByOpenId.getSeqId());
            return new JSONResult(true, "查找成功", friends);
        } else {
            return new JSONResult(false, "查找失败");
        }


    }


}
