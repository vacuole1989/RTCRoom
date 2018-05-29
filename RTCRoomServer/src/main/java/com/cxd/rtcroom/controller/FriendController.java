package com.cxd.rtcroom.controller;


import com.cxd.rtcroom.bean.ChatAsk;
import com.cxd.rtcroom.bean.Friendship;
import com.cxd.rtcroom.bean.FriendshipTip;
import com.cxd.rtcroom.bean.UserInfo;
import com.cxd.rtcroom.dao.*;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    private ChatMsgRepository chatMsgRepository;
    @Autowired
    private ChatAskRepository chatAskRepository;


    @RequestMapping("/getFriends")
    public JSONResult getFriends(@PathVariable String appId, long seqId) {
        List<UserInfo> friends = friendshipRepository.findFriends(seqId, seqId);
        for (UserInfo friend : friends) {
            long unReadChatMsgCount = chatMsgRepository.findUnReadChatMsgCount(friend.getSeqId(), seqId);
            friend.setUnRead(unReadChatMsgCount);
        }
        return new JSONResult(true, "查找成功", friends);
    }

    @RequestMapping("/getUserInfoById")
    public JSONResult getFriendById(@PathVariable String appId, long seqId) {
        UserInfo userInfo = userInfoRepository.findOne(seqId);
        return new JSONResult(true, "查找成功", userInfo);

    }

    @RequestMapping("/getFriendTipCount")
    public JSONResult getFriendTipCount(@PathVariable String appId, long seqId) {
        long l = friendshipTipRepository.countByUserSeqIdAndIread(seqId, false);
        long unReadChatMsgCount = chatMsgRepository.findAllUnReadChatMsgCount(seqId);
        Map<String, Object> map = new HashMap<>();
        map.put("tip", l);
        map.put("read", unReadChatMsgCount);
        map.put("all", unReadChatMsgCount + l);


        List<ChatAsk> ifAsked = chatAskRepository.findIFAsked(seqId, DateUtil.format(System.currentTimeMillis() - 1000));
        if(ifAsked.size()>0){
            map.put("asked",ifAsked.get(0));
        }else{
            map.put("asked",false);
        }


        return new JSONResult(true, "查找成功", map);

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


    @RequestMapping("/agreeFriend")
    public JSONResult AgreeFriend(@PathVariable String appId, long seqId, boolean agree) {

        FriendshipTip friendshipTip = friendshipTipRepository.findOne(seqId);
        Friendship friendships = null;
        if (agree) {
            List<Friendship> ifFriend = friendshipRepository.findIfFriend(friendshipTip.getUserSeqId(), friendshipTip.getFromUserId(), friendshipTip.getFromUserId(), friendshipTip.getUserSeqId());
            if (ifFriend.size() > 0) {
                friendshipTip.setIread(true);
                friendshipTip.setAgree(agree);
                friendshipTip.setModifyTime(DateUtil.format(new Date()));
                friendshipTipRepository.save(friendshipTip);
                return new JSONResult(false, "你们已经是好友", null);
            }

            if (friendshipTip.getUserSeqId() <= friendshipTip.getFromUserId()) {
                friendships = new Friendship().setFriendSeqId(friendshipTip.getUserSeqId()).setOwnerSeqId(friendshipTip.getFromUserId());
            } else {
                friendships = new Friendship().setFriendSeqId(friendshipTip.getFromUserId()).setOwnerSeqId(friendshipTip.getUserSeqId());
            }
            friendshipRepository.save(friendships.setCreateTime(DateUtil.format(new Date())).setModifyTime(DateUtil.format(new Date())));
        }

        friendshipTip.setIread(true);
        friendshipTip.setAgree(agree);
        friendshipTip.setModifyTime(DateUtil.format(new Date()));
        friendshipTipRepository.save(friendshipTip);

        return new JSONResult(true, "查找成功", friendships);

    }

}
