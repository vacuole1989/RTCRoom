package com.cxd.rtcroom.controller;


import com.alibaba.fastjson.JSON;
import com.cxd.rtcroom.BizException;
import com.cxd.rtcroom.bean.*;
import com.cxd.rtcroom.dao.*;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@RestController
@RequestMapping("/app/{appId}")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String SESSION_KEY = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String SUCCESS = "SUCCESS";
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
    @Autowired
    private FriendshipTipRepository friendshipTipRepository;
    @Autowired
    private ChatAskRepository chatAskRepository;
    @Autowired
    private FeeEnumRepository feeEnumRepository;
    @Autowired
    private QueryPayRepository queryPayRepository;
    @Autowired
    private DiamondRepository diamondRepository;

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
            userInfo.setOnlineStatusTime(System.currentTimeMillis());
            userInfo.setLastLoginTime(DateUtil.format(new Date()));
            Long txTime = DateUtil.format(DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime() / 1000;
            String pushUrl = "rtmp://" + appTag.getPushBizId() + ".livepush.myqcloud.com/live/" + appTag.getPushBizId() + "_" + userInfo.getOpenId() + "?bizid=" + appTag.getPushBizId() + "&" + getSafeUrl(appTag.getPushKey(), appTag.getPushBizId() + "_" + userInfo.getOpenId(), txTime);

            UserInfo userInfoByOpenId = userInfoRepository.findUserInfoByOpenId(openid);
            if (null != userInfoByOpenId) {
                BeanUtils.copyProperties(userInfoByOpenId, userInfo);
            } else {
                if(userInfo.getGender()==0){
                    userInfo.setGender(1);
                }
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

    @RequestMapping("/unifiedorder")
    @ResponseBody
    public Object querypay(HttpServletRequest request, @PathVariable String appId, String cny, String code) {
        ConstantUtil constantUtil = new ConstantUtil(appId, appTagRepository);
        String totalFee = cny;
        String description = constantUtil.getPayDesc();
        String ip = IpUtil.getIpAddress(request);
        Object obj = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Object.class, constantUtil.getGetSessionKeyUrl(), constantUtil.getAppId(), constantUtil.getAppSecret(), code);
        String openid = (String) JSON.parseObject(JSON.toJSONString(obj), Map.class).get("openid");

        String outTradeNo = DateUtil.format(new Date(), "yyyyMMddHHmmssSS") + UUID.randomUUID().toString().substring(0, 5);

        String url = constantUtil.getUnifiedorderUrl();
        String xml = CommonUtil.WXParamGenerate(description, outTradeNo, totalFee, ip, openid, constantUtil);
        String res = HttpUtil.httpsRequest(url, "POST", xml);

        Map<String, String> data;
        try {
            data = CommonUtil.doXMLParse(res);
        } catch (Exception e) {
            throw new BizException("订单提交失败，请重试");
        }

        String noneceStr = CommonUtil.GetNonceStr();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        try {
            queryPayRepository.save(new QueryPay()
                    .setCode(code)
                    .setDescription(description)
                    .setIp(ip)
                    .setNonceStr(noneceStr)
                    .setOpenid(openid)
                    .setOutTradeNo(outTradeNo)
                    .setPrepayId(data.get("prepay_id"))
                    .setResultCode(data.get("result_code"))
                    .setReturnCode(data.get("return_code"))
                    .setReturnMsg(data.get("return_msg"))
                    .setTimeStamp(timeStamp)
                    .setCrtTim(DateUtil.format(new Date()))
                    .setTotalFee(totalFee)
                    .setPlayFlag(0));


        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        if (data.get("return_code").equals(SUCCESS) && data.get("result_code").equals(SUCCESS)) {
            Map<String, String> param = new HashMap<>();
            param.put("appId", constantUtil.getAppId());
            param.put("timeStamp", timeStamp);
            param.put("nonceStr", noneceStr);
            param.put("package", "prepay_id=" + data.get("prepay_id"));
            param.put("signType", "MD5");

            String sign = CommonUtil.GetSign(param, constantUtil);

            param.put("sign", sign);
            param.put("outTradeNo", outTradeNo);

            return new JSONResult(true, param);
        } else {
            return new JSONResult(false, data);
        }


    }


    @RequestMapping("/getDiamondNum")
    public JSONResult getDiamondNum(@PathVariable String appId, @RequestParam long seqId) {
        UserInfo userInfo = userInfoRepository.findOne(seqId);


        return new JSONResult(true, "查询成功", userInfo);
    }
    @RequestMapping("/getGifts")
    public JSONResult getGifts(@PathVariable String appId) {
        FeeEnum feeEnum = new FeeEnum(appId, feeEnumRepository);

        List<FeeEnum> list = feeEnum.getList();
        List<FeeEnum> feeEnums=new ArrayList<>();

        for (FeeEnum anEnum : list) {
            if(anEnum.getType().contains("gift")){
                feeEnums.add(anEnum);
            }
        }

        return new JSONResult(true, "查询成功", feeEnums);
    }

    @RequestMapping("/paySuccess")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult paySuccess(@PathVariable String appId, @RequestParam String nonceStr) {
        QueryPay queryPay = queryPayRepository.findQueryPayByNonceStr(nonceStr);
        SysConfig one = sysConfigRepository.findOne(2L);
        if (null != queryPay) {
            queryPay.setPlayFlag(1);
            UserInfo userInfo = userInfoRepository.findUserInfoByOpenId(queryPay.getOpenid());
            userInfo.setTotalDiamond(new BigDecimal(userInfo.getTotalDiamond()).add(new BigDecimal(one.getValue()).multiply(new BigDecimal(queryPay.getTotalFee()))).intValue());


            diamondRepository.save(new Diamond()
                    .setFee(new BigDecimal(queryPay.getTotalFee()).intValue())
                    .setTradeTime(DateUtil.format(new Date()))
                    .setPaySeqId(queryPay.getSeqId())
                    .setUserSeqId(userInfo.getSeqId())
                    .setDiamondNum(new BigDecimal(one.getValue()).multiply(new BigDecimal(queryPay.getTotalFee())).intValue()));

            queryPayRepository.save(queryPay);
            userInfoRepository.save(userInfo);
        }
        return new JSONResult(true, "设置成功");
    }

    @RequestMapping("/payDiamond")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult payDiamond(@PathVariable String appId, @RequestParam String itype,@RequestParam long seqId) {

        FeeEnum feeEnum = new FeeEnum(appId, feeEnumRepository);
        int diamondNum = feeEnum.getFeeByType(itype).getDiamondNum();

        UserInfo userInfo = userInfoRepository.findOne(seqId);

        if(new BigDecimal(userInfo.getTotalDiamond()).compareTo(new BigDecimal(diamondNum))<0){
            return new JSONResult(false, "对不起，钻石不足，请先充值！");
        }else{
            diamondRepository.save(new Diamond()
                    .setFee(0)
                    .setTradeTime(DateUtil.format(new Date()))
                    .setPaySeqId(0)
                    .setUserSeqId(userInfo.getSeqId())
                    .setDiamondNum(-new BigDecimal(diamondNum).intValue()));
            userInfo.setTotalDiamond(new BigDecimal(userInfo.getTotalDiamond()).subtract(new BigDecimal(diamondNum)).intValue());
            userInfoRepository.save(userInfo);
            return new JSONResult(true, "设置成功",itype);
        }



    }


    @RequestMapping("/onlineUser")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult onlineUser(@PathVariable String appId, @RequestParam long seqId, @RequestParam String type) {
        AppTag appTag = appTagRepository.findOne(appId);
        OnlineUser onlineUser = onlineUserRepository.findOne(seqId);
        UserInfo one = userInfoRepository.findOne(seqId);
        if (null != onlineUser) {
            if (onlineUser.getExpireTime() < System.currentTimeMillis()) {
                //如果存在在线列表中，但是时间已经过期。就删除在线用户
                deleteExpireUser(onlineUser);
            } else {
                //存在在线列表中，那么表示被连线了，就加入房间
                String playUrl = "rtmp://" + appTag.getPushBizId() + ".liveplay.myqcloud.com/live/" + appTag.getPushBizId() + "_" + onlineUser.getContactOpenId();

                one.setOnline(false);
                userInfoRepository.save(one);
                Map<String, Object> map = new HashMap<>();
                map.put("stopTime", (int) ((onlineUser.getExpireTime() - System.currentTimeMillis()) / 1000) + "");
                map.put("playUrl", playUrl);
                map.put("contactUserInfo", userInfoRepository.findOne(onlineUser.getContactSeqId()));
                return new JSONResult(true, "已经在房间里面", map);
            }

        }
        UserInfo contactUserInfo;
        if ("girl".equalsIgnoreCase(type)) {
            if(one.getGender()==1){
                contactUserInfo = userInfoRepository.findOnlineUser(seqId, System.currentTimeMillis() - 1000, 2,2);
            }else{
                contactUserInfo = userInfoRepository.findOnlineUser(seqId, System.currentTimeMillis() - 1000, 2,1);
            }
        } else if ("boy".equalsIgnoreCase(type)) {
            if(one.getGender()==1){
                contactUserInfo = userInfoRepository.findOnlineUser(seqId, System.currentTimeMillis() - 1000, 1,2);
            }else {
                contactUserInfo = userInfoRepository.findOnlineUser(seqId, System.currentTimeMillis() - 1000, 1,1);
            }
        } else {
            if(one.getGender()==1){
                contactUserInfo = userInfoRepository.findOnlineUserAll(seqId, System.currentTimeMillis() - 1000,2);
            }else{
                contactUserInfo = userInfoRepository.findOnlineUserAll(seqId, System.currentTimeMillis() - 1000,1);
            }
        }

        if (null != contactUserInfo) {
            //  找到了，保存用户信息到在线用户表。
            SysConfig sysConfig = sysConfigRepository.findOne(1L);
            long expireTime = Long.parseLong(null != sysConfig ? sysConfig.getValue() : "60");
            UserInfo myUser = userInfoRepository.findOne(seqId);
            OnlineUser myOnlineUser = new OnlineUser()
                    .setContactOpenId(contactUserInfo.getOpenId())
                    .setContactSeqId(contactUserInfo.getSeqId())
                    .setSeqId(myUser.getSeqId())
                    .setExpireTime(System.currentTimeMillis() + expireTime * 1000);
            OnlineUser contactOnlineUser = new OnlineUser()
                    .setContactOpenId(myUser.getOpenId())
                    .setContactSeqId(myUser.getSeqId())
                    .setSeqId(contactUserInfo.getSeqId())
                    .setExpireTime(System.currentTimeMillis() + expireTime * 1000);
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
            one.setOnlineStatusTime(System.currentTimeMillis());
            if ("girl".equalsIgnoreCase(type)) {
                one.setAskGender(2);
            } else if ("boy".equalsIgnoreCase(type)) {
                one.setAskGender(1);
            }else{
                one.setAskGender(0);
            }
            userInfoRepository.save(one);
            return new JSONResult(false, "未找到");
        }
    }


    @RequestMapping("/sendVideo")
    public JSONResult getPlayUrl(@PathVariable String appId, @RequestParam long seqId, @RequestParam long userSeqId) {
        UserInfo userInfo = userInfoRepository.findOne(userSeqId);

        ChatAsk chatAsk = new ChatAsk();
        chatAsk.setAgree(0);
        chatAsk.setAskUserSeqId(userSeqId);
        chatAsk.setModifyTime(DateUtil.format(new Date()));
        chatAsk.setNickName(userInfo.getNickName());
        chatAsk.setReciveUserId(seqId);
        ChatAsk save = chatAskRepository.save(chatAsk);

        return new JSONResult(true, "在线成功", save);
    }

    @RequestMapping("/onCharVideoCycle")
    public JSONResult onCharVideoCycle(@PathVariable String appId, @RequestParam long seqId) {
        AppTag appTag = appTagRepository.findOne(appId);
        ChatAsk chatAsk = chatAskRepository.findOne(seqId);

        if (chatAsk.getAgree() == 1) {
            UserInfo one = userInfoRepository.findOne(chatAsk.getReciveUserId());
            String playUrl = "rtmp://" + appTag.getPushBizId() + ".liveplay.myqcloud.com/live/" + appTag.getPushBizId() + "_" + one.getOpenId();

            Map<String, Object> map = new HashMap<>();
            map.put("playUrl", playUrl);
            map.put("contactUserInfo", one);
            map.put("askUser", chatAsk);
            chatAsk.setAskTime(DateUtil.format(new Date()));
            chatAskRepository.save(chatAsk);
            return new JSONResult(true, "在线成功", map);
        } else {
            chatAsk.setAskTime(DateUtil.format(new Date()));
            chatAsk.setModifyTime(DateUtil.format(new Date()));
            chatAskRepository.save(chatAsk);
        }
        return new JSONResult(false, "没有接收", null);

    }

    @RequestMapping("/agreeAsk")
    public JSONResult agreeAsk(@PathVariable String appId, @RequestParam long seqId, int agree) {
        AppTag appTag = appTagRepository.findOne(appId);
        ChatAsk chatAsk = chatAskRepository.findOne(seqId);

        chatAsk.setAgree(agree);
        Map<String, Object> map = new HashMap<>();
        map.put("askUser", chatAsk);
        map.put("agree", agree);
        if (chatAsk.getAgree() == 1) {
            UserInfo one = userInfoRepository.findOne(chatAsk.getAskUserSeqId());
            String playUrl = "rtmp://" + appTag.getPushBizId() + ".liveplay.myqcloud.com/live/" + appTag.getPushBizId() + "_" + one.getOpenId();
            map.put("playUrl", playUrl);
            map.put("contactUserInfo", one);
        }
        chatAsk.setRecTime(DateUtil.format(new Date()));
        chatAskRepository.save(chatAsk);
        return new JSONResult(true, "在线成功", map);


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
    public JSONResult heartbeat(@RequestParam long seqId, @RequestParam boolean isFriend) {
        Map<String, Object> map = new HashMap<>();
        boolean online;
        OnlineUser one = onlineUserRepository.findOne(seqId);
        if (null != one && one.getExpireTime() >= System.currentTimeMillis()) {
            online = true;
        } else {
            online = false;
        }
        map.put("online", online);
        if (null != one) {
            if (one.getExpireTime() < System.currentTimeMillis()) {
                deleteExpireUser(one);
            }
            if (!isFriend) {
                List<FriendshipTip> friendshipTips = friendshipTipRepository.findByUserSeqIdAndFromUserIdAndIread(one.getSeqId(), one.getContactSeqId(), false);
                if (friendshipTips.size() > 0) {
                    FriendshipTip friendshipTip = friendshipTips.get(0);
                    if (friendshipTip.isAgree()) {
                        map.put("friendly", true);
                    } else {
                        map.put("friendly", false);
                    }
                    map.put("friend", friendshipTip);
                } else {
                    map.put("friend", false);
                }
            }
        }

        return new JSONResult(true, "心跳成功", map);
    }


    @RequestMapping("/heartBeatChat")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult heartbeat(@RequestParam long seqId, @RequestParam long userSeqId) {
        Map<String, Object> map = new HashMap<>();
        boolean online;
        ChatAsk chatAsk = chatAskRepository.findOne(seqId);
        if (Math.abs((int) (DateUtil.format(chatAsk.getAskTime()).getTime() - DateUtil.format(chatAsk.getRecTime()).getTime()) / 1000) >= 2) {
            online = false;
        } else {
            if (userSeqId == chatAsk.getAskUserSeqId()) {
                chatAsk.setAskTime(DateUtil.format(new Date()));
            } else {
                chatAsk.setRecTime(DateUtil.format(new Date()));
            }
            chatAskRepository.save(chatAsk);
            online = true;
        }
        map.put("online", online);
        return new JSONResult(true, "心跳成功", map);
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

    @RequestMapping("/closeTalkChat")
    @Transactional(rollbackOn = Exception.class)
    public JSONResult closeTalkChat(@RequestParam long seqId) {
        ChatAsk one1 = chatAskRepository.findOne(seqId);
        if (null != one1) {
            chatAskRepository.delete(one1);
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
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
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
