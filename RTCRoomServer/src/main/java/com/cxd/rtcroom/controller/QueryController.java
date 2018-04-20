package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.bean.OnlineUser;
import com.cxd.rtcroom.bean.SysConfig;
import com.cxd.rtcroom.bean.UserInfo;
import com.cxd.rtcroom.dao.AppTagRepository;
import com.cxd.rtcroom.dao.OnlineUserRepository;
import com.cxd.rtcroom.dao.SysConfigRepository;
import com.cxd.rtcroom.dao.UserInfoRepository;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@RestController
@RequestMapping("/app/{appId}")
public class QueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    private static final String SESSION_KEY = "https://api.weixin.qq.com/sns/jscode2session";
    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private OnlineUserRepository onlineUserRepository;
    @Autowired
    private SysConfigRepository sysConfigRepository;
    @Autowired
    private AppTagRepository appTagRepository;

    @RequestMapping("/onLogin")
    public JSONResult onLogin(@PathVariable String appId, @RequestParam String code, @RequestBody UserInfo userInfo) {

        AppTag appTag = appTagRepository.findOne(appId);


        Map map = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Map.class, SESSION_KEY, appTag.getAppId(), appTag.getAppSecret(), code);

        if (null != map.get("openid")) {
            String openid = map.get("openid") + "";
            String sessionKey = map.get("session_key") + "";
            userInfo.setSessionKey(sessionKey);
            userInfo.setOpenId(openid);
            userInfo.setOnline(false);
            userInfo.setOnlineStatusTime(DateUtil.format(new Date()));
            userInfo.setLastLoginTime(DateUtil.format(new Date()));
            Long txTime = DateUtil.format(DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() / 1000;
            String pushUrl = "rtmp://" + appTag.getPushBizId() + ".livepush.myqcloud.com/live/" + appTag.getPushBizId() + "_" + userInfo.getOpenId() + "?bizid=" + appTag.getPushBizId() + "&" + getSafeUrl(appTag.getPushKey(), appTag.getPushBizId() + "_" + userInfo.getOpenId(), txTime);

            UserInfo userInfoByOpenId = userInfoRepository.findUserInfoByOpenId(openid);
            if (null != userInfoByOpenId) {
                userInfoByOpenId.setLastLoginTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                userInfoByOpenId.setAvatarUrl(userInfo.getAvatarUrl());
                userInfoByOpenId.setCity(userInfo.getCity());
                userInfoByOpenId.setCountry(userInfo.getCountry());
                userInfoByOpenId.setGender(userInfo.getGender());
                userInfoByOpenId.setNickName(userInfo.getNickName());
                userInfoByOpenId.setProvince(userInfo.getProvince());
                userInfoRepository.save(userInfoByOpenId);
                BeanUtils.copyProperties(userInfoByOpenId, userInfo);
            } else {
                userInfoRepository.save(userInfo);
            }
            userInfo.setPushUrl(pushUrl);
            return new JSONResult(true, "用户登陆成功", userInfo);
        } else {
            return new JSONResult(false, "用户登录失败");
        }
    }


    @RequestMapping("/initPage")
    public JSONResult onLogin(@RequestParam long seqId) {
        UserInfo one = userInfoRepository.findOne(seqId);
        one.setInited(true);
        userInfoRepository.save(one);

        return new JSONResult(true, "初始化成功");

    }


    @RequestMapping("/onlineUser")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult onlineUser(@PathVariable String appId,@RequestParam long seqId) {
        AppTag appTag = appTagRepository.findOne(appId);
        OnlineUser onlineUser = onlineUserRepository.findOne(seqId);
        if (null != onlineUser) {
            if (DateUtil.format(onlineUser.getExpireTime()).getTime() < System.currentTimeMillis()) {
                deleteExpireUser(onlineUser);
            } else {
                String playUrl = "rtmp://" + appTag.getPushBizId() + ".liveplay.myqcloud.com/live/" + appTag.getPushBizId() + "_" + onlineUser.getContactOpenId();
                UserInfo one = userInfoRepository.findOne(seqId);
                one.setOnline(false);
                userInfoRepository.save(one);
                Map<String, Object> map = new HashMap<>();
                map.put("stopTime", (int) ((DateUtil.format(onlineUser.getExpireTime()).getTime() - System.currentTimeMillis()) / 1000) + "");
                map.put("playUrl", playUrl);
                map.put("contactUserInfo", userInfoRepository.findOne(onlineUser.getContactSeqId()));
                return new JSONResult(true, "已经在房间里面", map);
            }

        }

        UserInfo contactUserInfo = userInfoRepository.findOnlineUser(seqId, DateUtil.format(System.currentTimeMillis() - 1000));
        if (null != contactUserInfo) {
            //  找到了，保存用户信息到在线用户表。

            SysConfig sysConfig = sysConfigRepository.findOne(1L);
            long expireTime = Long.parseLong(null != sysConfig ? sysConfig.getValue() : "60");
            UserInfo myUser = userInfoRepository.findOne(seqId);
            OnlineUser myOnlineUser = new OnlineUser()
                    .setContactOpenId(contactUserInfo.getOpenId())
                    .setContactSeqId(contactUserInfo.getSeqId())
                    .setLastFlushTime(DateUtil.format(new Date()))
                    .setSeqId(myUser.getSeqId())
                    .setExpireTime(DateUtil.format(System.currentTimeMillis() + expireTime * 1000));
            OnlineUser contactOnlineUser = new OnlineUser()
                    .setContactOpenId(myUser.getOpenId())
                    .setContactSeqId(myUser.getSeqId())
                    .setLastFlushTime(DateUtil.format(new Date()))
                    .setSeqId(contactUserInfo.getSeqId())
                    .setExpireTime(DateUtil.format(System.currentTimeMillis() + expireTime * 1000));
            List<OnlineUser> onlineUsers = new ArrayList<>();
            onlineUsers.add(myOnlineUser);
            onlineUsers.add(contactOnlineUser);
            onlineUserRepository.save(onlineUsers);

            contactUserInfo.setOnline(false);
            myUser.setOnline(false);
            List<UserInfo> userInfos = new ArrayList<>();
            userInfos.add(contactUserInfo);
            userInfos.add(myUser);

            userInfoRepository.save(userInfos);

            String playUrl = "rtmp://" + appTag.getPushBizId() + ".liveplay.myqcloud.com/live/" + appTag.getPushBizId() + "_" + contactUserInfo.getOpenId();
            Map<String, Object> map = new HashMap<>();
            map.put("stopTime", expireTime);
            map.put("playUrl", playUrl);
            map.put("contactUserInfo", contactUserInfo);
            return new JSONResult(true, "找到在线用户", map);
        } else {
            //未找到，更新心跳
            UserInfo one = userInfoRepository.findOne(seqId);
            one.setOnlineStatusTime(DateUtil.format(new Date()));
            userInfoRepository.save(one);
            return new JSONResult(false, "未找到");
        }
    }

    @RequestMapping("/online")
    public JSONResult online(@RequestParam long seqId) {
        UserInfo one = userInfoRepository.findOne(seqId);
        one.setOnline(true);
        userInfoRepository.save(one);
        return new JSONResult(true, "在线成功");
    }

    @RequestMapping("/offline")
    public JSONResult offline(@RequestParam long seqId) {
        UserInfo one = userInfoRepository.findOne(seqId);
        one.setOnline(false);
        userInfoRepository.save(one);
        return new JSONResult(true, "离线成功");
    }


    @RequestMapping("/heartBeat")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult heartbeat(@RequestParam long seqId) {
        boolean online;
        OnlineUser one = onlineUserRepository.findOne(seqId);
        if (null != one && DateUtil.format(one.getExpireTime()).getTime() >= System.currentTimeMillis()) {
            online = true;
        } else {
            online = false;
        }
        if (null != one && DateUtil.format(one.getExpireTime()).getTime() < System.currentTimeMillis()) {
            deleteExpireUser(one);
        }
        return new JSONResult(true, "心跳成功", online);
    }

    @RequestMapping("/closeTalk")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult closeTalk(@RequestParam long seqId) {
        OnlineUser one = onlineUserRepository.findOne(seqId);
        if (null != one) {
            deleteExpireUser(one);
        }
        return new JSONResult(true, "删除成功");
    }

    @Transactional(rollbackOn = Exception.class)
    void deleteExpireUser(OnlineUser one) {
        OnlineUser onlineUser = onlineUserRepository.findOne(one.getContactSeqId());
        List<OnlineUser> onlineUsers = new ArrayList<>();
        onlineUsers.add(one);
        onlineUsers.add(onlineUser);
        onlineUserRepository.delete(onlineUsers);
    }

    @RequestMapping("/notify")
    public JSONResult heartbeat(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream ris = request.getInputStream();
            StringBuilder content = new StringBuilder();
            byte[] b = new byte[1024];
            int lens;
            while ((lens = ris.read(b)) > 0) {
                content.append(new String(b, 0, lens));
            }
            String strcont = content.toString();
            LOGGER.info("直播流心跳数据：" + strcont);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONResult(true, "心跳成功");
    }


    private static String getSafeUrl(String key, String streamId, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }


}
