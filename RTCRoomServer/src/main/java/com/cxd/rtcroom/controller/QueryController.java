package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.OnlineUser;
import com.cxd.rtcroom.bean.SysConfig;
import com.cxd.rtcroom.bean.UserInfo;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/app")
public class QueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);

    private static final String APPID = "wx30dde37837561559";
    private static final String PUSH_KEY = "db9af79969137a69357befb62a27924f";
    private static final String PUSH_BIZ_ID = "22043";
    private static final String sessionKey = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String SECRET = "ed201c3998d2fb9ecb32f9c2a6c42aec";
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

    @RequestMapping("/onLogin")
    public JSONResult onLogin(@RequestParam String code, @RequestBody UserInfo userInfo) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        System.out.println(gson.toJson(userInfo));
        Map map = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Map.class, sessionKey, APPID, SECRET, code);
        System.out.println(map);

        if (null != map.get("openid")) {
            String openid = map.get("openid") + "";
            String sessionKey = map.get("session_key") + "";
            userInfo.setSessionKey(sessionKey);
            userInfo.setOpenId(openid);
            userInfo.setOnline(false);
            userInfo.setOnlineStatusTime(DateUtil.format(new Date()));
            userInfo.setLastLoginTime(DateUtil.format(new Date()));
            Long txTime = DateUtil.format(DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() / 1000;
            String pushUrl = "rtmp://" + PUSH_BIZ_ID + ".livepush.myqcloud.com/live/" + PUSH_BIZ_ID + "_" + userInfo.getOpenId() + "?bizid=" + PUSH_BIZ_ID + "&" + getSafeUrl(PUSH_KEY, PUSH_BIZ_ID + "_" + userInfo.getOpenId(), txTime);

            UserInfo userInfoByOpenId = userInfoRepository.findUserInfoByOpenId(openid);
            if (null != userInfoByOpenId) {
                userInfoByOpenId.setLastLoginTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
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
    public JSONResult onlineUser(@RequestParam long seqId) {

        OnlineUser onlineUser = onlineUserRepository.findOne(seqId);
        if (null != onlineUser) {
            if(DateUtil.format(onlineUser.getExpireTime()).getTime() < System.currentTimeMillis()){
                deleteExpireUser(onlineUser);
            }else{
                String playUrl = "rtmp://" + PUSH_BIZ_ID + ".liveplay.myqcloud.com/live/" + PUSH_BIZ_ID + "_" + onlineUser.getContactOpenId();
                UserInfo one = userInfoRepository.findOne(seqId);
                one.setOnline(false);
                userInfoRepository.save(one);
                Map<String,String> map=new HashMap<>();
                map.put("time",(int)(DateUtil.format(onlineUser.getExpireTime()).getTime() - System.currentTimeMillis())+"");
                map.put("playUrl",playUrl);
                return new JSONResult(true, "已经在房间里面", map);
            }

        }

        UserInfo userInfo = userInfoRepository.findOnlineUser(seqId, DateUtil.format(System.currentTimeMillis() - 1000));
        if (null != userInfo) {
            //  找到了，保存用户信息到在线用户表。
            saveOnlineuser(userInfo, seqId);

            String playUrl = "rtmp://" + PUSH_BIZ_ID + ".liveplay.myqcloud.com/live/" + PUSH_BIZ_ID + "_" + userInfo.getOpenId();
            SysConfig one = sysConfigRepository.findOne(1L);
            Map<String,String> map=new HashMap<>();
            map.put("time",null!=one?one.getValue():"60");
            map.put("playUrl",playUrl);


            return new JSONResult(true, "找到在线用户", map);
        } else {
            //未找到，更新心跳
            UserInfo one = userInfoRepository.findOne(seqId);
            one.setOnlineStatusTime(DateUtil.format(new Date()));
            userInfoRepository.save(one);
            return new JSONResult(false, "未找到");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    void saveOnlineuser(UserInfo userInfo, long seqId) {
        SysConfig sysConfig = sysConfigRepository.findOne(1L);
        long expireTime=Long.parseLong(null!=sysConfig?sysConfig.getValue():"60");
        UserInfo one = userInfoRepository.findOne(seqId);
        OnlineUser onlineUser = new OnlineUser().setContactOpenId(userInfo.getOpenId()).setContactSeqId(userInfo.getSeqId()).setLastFlushTime(DateUtil.format(new Date())).setSeqId(one.getSeqId()).setExpireTime(DateUtil.format(System.currentTimeMillis() + expireTime * 1000));
        OnlineUser onlineUser1 = new OnlineUser().setContactOpenId(one.getOpenId()).setContactSeqId(one.getSeqId()).setLastFlushTime(DateUtil.format(new Date())).setSeqId(userInfo.getSeqId()).setExpireTime(DateUtil.format(System.currentTimeMillis() + expireTime * 1000));
        List<OnlineUser> onlineUsers = new ArrayList<>();
        onlineUsers.add(onlineUser);
        onlineUsers.add(onlineUser1);
        onlineUserRepository.save(onlineUsers);

        userInfo.setOnline(false);
        userInfoRepository.save(userInfo);

        one.setOnline(false);
        userInfoRepository.save(one);

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


    @RequestMapping("/heartbeat")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult heartbeat(@RequestParam long seqId) {
        OnlineUser one = onlineUserRepository.findOne(seqId);
        if (null != one && DateUtil.format(one.getExpireTime()).getTime() < System.currentTimeMillis()) {
            deleteExpireUser(one);
        }
        return new JSONResult(true, "心跳成功");
    }

    private void deleteExpireUser(OnlineUser one) {
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
