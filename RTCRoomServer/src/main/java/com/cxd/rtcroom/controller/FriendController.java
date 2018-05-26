package com.cxd.rtcroom.controller;


import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.bean.Friendship;
import com.cxd.rtcroom.bean.FriendshipTip;
import com.cxd.rtcroom.bean.UserInfo;
import com.cxd.rtcroom.dao.AppTagRepository;
import com.cxd.rtcroom.dao.FriendshipRepository;
import com.cxd.rtcroom.dao.FriendshipTipRepository;
import com.cxd.rtcroom.dao.UserInfoRepository;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.tls.tls_sigature.tls_sigature;
import com.cxd.rtcroom.util.Config;
import com.cxd.rtcroom.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/app/{appId}")
public class FriendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendController.class);
    private static final String SESSION_KEY = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private FriendshipTipRepository friendshipTipRepository;
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

    @RequestMapping("/getFriendById")
    public JSONResult getFriends(@PathVariable String appId, long seqId) {
        UserInfo userInfo = userInfoRepository.findOne(seqId);

        Map<String, Object> map = new HashMap<>();

        map.put("friend", userInfo);
        return new JSONResult(true, "查找成功", map);

    }

    @RequestMapping("/getFriendTipCount")
    public JSONResult getFriendTipCount(@PathVariable String appId, long seqId) {
        long l = friendshipTipRepository.countByUserSeqIdAndIread(seqId, false);

        return new JSONResult(true, "查找成功", l);

    }

    @RequestMapping("/getIfFriend")
    public JSONResult getIfFriend(@PathVariable String appId, long userSeqId, long friendSeqId) {
        List<Friendship> ifFriend = friendshipRepository.findIfFriend(userSeqId, friendSeqId, friendSeqId, userSeqId);
        if (ifFriend.size() > 0) {
            return new JSONResult(true, "查找成功", null);
        } else {
            return new JSONResult(false, "查找成功", null);

        }
    }

    @RequestMapping("/getFriendTip")
    public JSONResult getFriendTip(@PathVariable String appId, long seqId) {
        List<FriendshipTip> friendshipTips = friendshipTipRepository.findByUserSeqIdOrderByCreateTimeDesc(seqId);
        return new JSONResult(true, "查找成功", friendshipTips);

    }

    @RequestMapping("/askFriend")
    public JSONResult askFriend(@PathVariable String appId, long userSeqId, long friendSeqId) {


        UserInfo userInfo = userInfoRepository.findOne(userSeqId);

        List<FriendshipTip> byUserSeqIdAndFromUserId = friendshipTipRepository.findByUserSeqIdAndFromUserId(friendSeqId, userSeqId);
        if (byUserSeqIdAndFromUserId.size() > 0) {
            friendshipTipRepository.delete(byUserSeqIdAndFromUserId);
        }

        FriendshipTip friendshipTip = new FriendshipTip();
        friendshipTip.setCreateTime(DateUtil.format(new Date()));
        friendshipTip.setModifyTime(DateUtil.format(new Date()));
        friendshipTip.setFromUserId(userSeqId);
        friendshipTip.setUserSeqId(friendSeqId);
        friendshipTip.setAvatarUrl(userInfo.getAvatarUrl());
        friendshipTip.setNickName(userInfo.getNickName());
        friendshipTip.setAgree(false);
        friendshipTip.setIread(false);
        FriendshipTip save = friendshipTipRepository.save(friendshipTip);

        return new JSONResult(true, "查找成功", save);

    }


    @RequestMapping("/AgreeFriend")
    public JSONResult AgreeFriend(@PathVariable String appId, long seqId, boolean agree) {

        FriendshipTip friendshipTip = friendshipTipRepository.findOne(seqId);
        if (agree) {

            List<Friendship> ifFriend = friendshipRepository.findIfFriend(friendshipTip.getUserSeqId(), friendshipTip.getFromUserId(), friendshipTip.getFromUserId(), friendshipTip.getUserSeqId());
            if(ifFriend.size()>0){
                friendshipTip.setIread(true);
                friendshipTip.setAgree(agree);
                friendshipTip.setModifyTime(DateUtil.format(new Date()));
                 friendshipTipRepository.save(friendshipTip);
                return new JSONResult(false, "你们已经是好友", null);
            }

            List<Friendship> friendships = new ArrayList<>();
            friendships.add(new Friendship()
                    .setFriendSeqId(friendshipTip.getUserSeqId())
                    .setOwnerSeqId(friendshipTip.getFromUserId())
                    .setCreateTime(DateUtil.format(new Date()))
                    .setModifyTime(DateUtil.format(new Date())));
            friendships.add(new Friendship()
                    .setFriendSeqId(friendshipTip.getFromUserId())
                    .setOwnerSeqId(friendshipTip.getUserSeqId())
                    .setCreateTime(DateUtil.format(new Date()))
                    .setModifyTime(DateUtil.format(new Date())));
            friendshipRepository.save(friendships);
        }

        friendshipTip.setIread(true);
        friendshipTip.setAgree(agree);
        friendshipTip.setModifyTime(DateUtil.format(new Date()));
        FriendshipTip save = friendshipTipRepository.save(friendshipTip);

        return new JSONResult(true, "查找成功", save);

    }


    @RequestMapping("/IMLogin")
    public JSONResult IMLogin(@PathVariable String appId, long mySeqId) {
        Map<String, Object> map = new HashMap<>();
        tls_sigature.GenTLSSignatureResult result;
        try {
            result = tls_sigature.GenTLSSignatureEx(Config.IM.IM_SDKAPPID, "wjsd_user_" + mySeqId, Config.IM.PRIVATEKEY);
            map.put("userSig", result.urlSig);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return new JSONResult(true, "查找成功", map);

    }

}
