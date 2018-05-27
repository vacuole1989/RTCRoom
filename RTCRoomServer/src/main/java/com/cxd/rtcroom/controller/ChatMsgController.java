package com.cxd.rtcroom.controller;


import com.cxd.rtcroom.bean.ChatMsg;
import com.cxd.rtcroom.dao.ChatMsgRepository;
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

@RestController
@RequestMapping("/app/{appId}")
public class ChatMsgController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatMsgController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ChatMsgRepository chatMsgRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;


    @RequestMapping("/sendChatMsg")
    public JSONResult sendChatMsg(@PathVariable String appId, @RequestBody ChatMsg chatMsg) {
        chatMsg.setCreateTime(DateUtil.format(new Date()));
        chatMsg.setIread(false);
        chatMsgRepository.save(chatMsg);
        return new JSONResult(true, "发表成功", chatMsg);
    }

    @RequestMapping("/getChatMsgList")
    public JSONResult getChatMsgList(@PathVariable String appId, @RequestBody ChatMsg chatMsg) {
        int count = chatMsgRepository.updateChatMsgRead(chatMsg.getToUserSeqId(), chatMsg.getFromUserSeqId());
        List<ChatMsg> chatMsgs = chatMsgRepository.findChatMsg(chatMsg.getFromUserSeqId(), chatMsg.getToUserSeqId(), chatMsg.getToUserSeqId(), chatMsg.getFromUserSeqId());
        return new JSONResult(true, "查询成功", chatMsgs);
    }

    @RequestMapping("/getNewChatMsgList")
    public JSONResult getNewChatMsgList(@PathVariable String appId, @RequestBody ChatMsg chatMsg) {
        if (chatMsg.getSeqId() > 0) {
            List<ChatMsg> chatMsgs = chatMsgRepository.findNewChatMsg(chatMsg.getToUserSeqId(), chatMsg.getFromUserSeqId(), chatMsg.getSeqId());
            if (chatMsgs.size() <= 0) {
                return new JSONResult(false, "", null);
            }
            int count = chatMsgRepository.updateChatMsgRead(chatMsg.getToUserSeqId(), chatMsg.getFromUserSeqId());
            return new JSONResult(true, "查询成功", chatMsgs);
        } else {
            return new JSONResult(false, "", null);

        }
    }


}
