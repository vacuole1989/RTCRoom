package com.cxd.rtcroom.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxd.rtcroom.BizException;
import com.cxd.rtcroom.bean.*;
import com.cxd.rtcroom.dao.*;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;


@RestController
@RequestMapping("/app")
public class QueryController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QueryLogsRepository queryLogsRepository;
    @Autowired
    private QueryPayRepository queryPayRepository;
    @Autowired
    private ActivationlockRepository activationlockRepository;
    @Autowired
    private AppraisalRepository appraisalRepository;
    @Autowired
    private CnRepository cnRepository;
    @Autowired
    private IcloudRepository icloudRepository;
    @Autowired
    private IndexRepository indexDao;
    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private SerialRepository serialRepository;
    @Autowired
    private SimlockRepository simlockRepository;
    @Autowired
    private SoldRepository soldRepository;
    @Autowired
    private QueryPayLogsRepository queryPayLogsRepository;
    @Autowired
    private AppTagRepository appTagRepository;
    @Autowired
    private FeeEnumRepository feeEnumRepository;
    @Autowired
    private InsImgRepository insImgRepository;


    @RequestMapping("/{appid}/{itype}")
    @ResponseBody
    public Object apple(@PathVariable(value = "appid") String appid, @PathVariable(value = "itype") String itype, String sn, String imei, String nonceStr, HttpServletRequest request) {
        ConstantUtil constantUtil = new ConstantUtil(appid, appTagRepository);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("key", constantUtil.getKey3023());
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        String stype = (null == imei) ? "sn" : "imei";
        String value = (null == imei) ? sn : imei;

        String url = constantUtil.getApi3023();

        ResponseEntity<String> exchange = this.restTemplate.exchange("{url}{type}?{stype}={value}", HttpMethod.GET, requestEntity, String.class, url, itype, stype, value);
        String body = "{}";
        try {
            body = new String(exchange.getBody().getBytes("iso8859-1"), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String ip = IpUtil.getIpAddress(request);
        QueryLogs queryLogs = new QueryLogs();
        queryLogs.setCrtTim(DateUtil.format(new Date()));
        queryLogs.setImei(imei);
        queryLogs.setSn(sn);
        queryLogs.setIpAddr(ip);
        queryLogs.setReturnBody(body);
        queryLogs.setNonceStr(nonceStr);
        queryLogs.setUrl(url + itype + "?" + stype + "=" + value);
        queryLogs.setItype(itype);
        queryLogs.setIsn(null == imei);
        queryLogs.setAppId(appid);
        queryLogsRepository.save(queryLogs);

        JSONObject jsonObject = JSON.parseObject(body);
        if ("apple".equalsIgnoreCase(itype) || "imei".equalsIgnoreCase(itype)) {
            if (StringUtils.isEmpty(jsonObject.get("code"))) {
                indexDao.save(new IndexQuery()
                        .setCapacity(jsonObject.get("capacity") + "")
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setColor(jsonObject.get("color") + "")
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("color") + "")
                        .setReturnBody(body)
                        .setSn(stype.equals("sn") ? value : ""));

            } else {
                indexDao.save(new IndexQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("serial".equalsIgnoreCase(itype)) {
            if (StringUtils.isEmpty(jsonObject.get("code"))) {
                serialRepository.save(new SerialQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)

                );
            } else {
                serialRepository.save(new SerialQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("appraisal".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                appraisalRepository.save(new AppraisalQuery().setApplecare(jsonObject.get("applecare") + "")
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setDaysleft(jsonObject.get("daysleft") + "")
                        .setImei(stype.equals("sn") ? "" : value)
                        .setIpAddr(ip)
                        .setManufactureFactory(JSON.parseObject(jsonObject.get("manufacture") + "").get("factory") + "")
                        .setPurchaseDate(JSON.parseObject(jsonObject.get("purchase") + "").get("date") + "")
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setSn(stype.equals("sn") ? value : "")
                        .setStatus(jsonObject.get("status") + "")
                        .setStorage(jsonObject.get("storage") + "")
                        .setSupport(jsonObject.get("support") + "")
                );
            } else {
                appraisalRepository.save(new AppraisalQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("cn".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                cnRepository.save(new CnQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setCoverage(jsonObject.get("coverage") + "")
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setPurchaseCountry(JSON.parseObject(jsonObject.get("purchase") + "").get("country") + "")
                );
            } else {
                cnRepository.save(new CnQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("activationlock".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                activationlockRepository.save(new ActivationlockQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setLocked(jsonObject.get("locked") + "")
                        .setTime(jsonObject.get("time") + "")

                );
            } else {
                activationlockRepository.save(new ActivationlockQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("icloud".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                icloudRepository.save(new IcloudQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setIcloud(jsonObject.get("icloud") + "")
                        .setLocked(jsonObject.get("locked") + "")

                );
            } else {
                icloudRepository.save(new IcloudQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("repair".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                repairRepository.save(new RepairQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setSales(jsonObject.get("sales") + "")
                        .setStatus(jsonObject.get("status") + "")

                );
            } else {
                repairRepository.save(new RepairQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        } else if ("sold".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                soldRepository.save(new SoldQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setPurchaseCountry(JSON.parseObject(jsonObject.get("purchase") + "").get("country") + "")
                        .setStorage(jsonObject.get("storage") + "")
                );
            } else {
                soldRepository.save(new SoldQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }

        } else if ("simlock".equalsIgnoreCase(itype)) {
            if ("0".equals(jsonObject.get("code") + "")) {
                jsonObject = JSON.parseObject(jsonObject.get("data") + "");
                simlockRepository.save(new SimlockQuery()
                        .setCode("0")
                        .setNonceStr(nonceStr)
                        .setCrtTim(DateUtil.format(new Date()))
                        .setImei(stype.equals("sn") ? "" : value)
                        .setSn(stype.equals("sn") ? value : "")
                        .setIpAddr(ip)
                        .setModel(jsonObject.get("model") + "")
                        .setReturnBody(body)
                        .setSimlock(jsonObject.get("simlock") + "")

                );
            } else {
                simlockRepository.save(new SimlockQuery().setCode(jsonObject.get("code") + "").setMessage(jsonObject.get("message") + ""));
            }
        }
        JSONObject checkbody = JSON.parseObject(body);
        if (!"apple".equals(itype) && !"imei".equals(itype)) {
            List<QueryPay> queryPayByNonceStr = queryPayRepository.getQueryPayByNonceStr(nonceStr);
            if (!StringUtils.isEmpty(checkbody.get("message"))) {
                if ("302311".equals(checkbody.get("code") + "") || "302312".equals(checkbody.get("code") + "") || "302313".equals(checkbody.get("code") + "") || "302314".equals(checkbody.get("code") + "") || (checkbody.get("message") + "").contains("不扣费")) {
                    CommonUtil.refund(queryPayByNonceStr, constantUtil);
                }
            }
            boolean aaa = ("serial".equalsIgnoreCase(itype) && StringUtils.isEmpty(checkbody.get("code"))) || (!"serial".equalsIgnoreCase(itype) && "0".equals(checkbody.get("code") + "")) || (!"simlock".equalsIgnoreCase(itype) && "302315".equals(checkbody.get("code") + ""));
            if (aaa) {
                if (queryPayByNonceStr.size() > 0) {
                    FeeEnum feeEnum1=new FeeEnum(appid, feeEnumRepository);
                    QueryPay queryPay = queryPayByNonceStr.get(0);
                    FeeEnum feeByType = feeEnum1.getFeeByType(itype);
                    CommonUtil.sendTemplate(restTemplate, queryPay.getTotalFee(), "序列号查询-" + feeByType.getName(), sn, queryPay.getOpenid(), queryPay.getOutTradeNo(), queryPay.getPrepayId(), nonceStr, constantUtil);
                }
            }
        }

        return new JSONResult(true, JSON.parseObject(body));
    }


    @RequestMapping("/{appid}/queryhistory")
    @ResponseBody
    public Object queryhistory(@PathVariable(value = "appid") String appid, String nonceStr) {
        List<QueryLogs> queryLogs = queryLogsRepository.getQueryLogsByNonceStr(nonceStr);
        if (queryLogs.size() > 0) {
            QueryLogs queryLogs1 = queryLogs.get(0);
            if ((DateUtil.format(queryLogs1.getCrtTim()).getTime() + 48 * 60 * 60 * 1000L) < System.currentTimeMillis()) {
                return new JSONResult(false, "数据已过期，请重新查询。");
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("success", true);
                map.put("isn", queryLogs1.isIsn());
                map.put("itype", queryLogs1.getItype());
                map.put("data", JSON.parseObject(queryLogs1.getReturnBody()));
                return map;
            }
        }
        return null;
    }

    @RequestMapping("/{appid}/initBtns")
    @ResponseBody
    public Object initBtns(@PathVariable(value = "appid") String appid) {
        FeeEnum feeEnum1=new FeeEnum(appid, feeEnumRepository);
        AppTag appTag = appTagRepository.findOne(appid);
        List<FeeEnum> feeEnums=feeEnum1.validateSwitchNotAll(appTag);
        InsImg one = insImgRepository.findOne(appid);
        return new JSONResult(true,feeEnums,one);
    }

    @RequestMapping("/{appid}/unifiedorder")
    @ResponseBody
    public Object querypay(HttpServletRequest request, @PathVariable(value = "appid") String appid, String itype, String code) {
        ConstantUtil constantUtil = new ConstantUtil(appid, appTagRepository);
        FeeEnum feeEnum1=new FeeEnum(appid, feeEnumRepository);
        String totalFee = feeEnum1.getFeeByType(itype).getFee();
        String description = constantUtil.getPayDesc();
        String ip = IpUtil.getIpAddress(request);
        Object obj = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Object.class, constantUtil.getGetSessionKeyUrl(), constantUtil.getAppId(), constantUtil.getAppSecret(), code);
        String openid = (String) JSON.parseObject(JSON.toJSONString(obj), Map.class).get("openid");

        String outTradeNo = DateUtil.format(new Date(), "yyyyMMddHHmmssSS") + UUID.randomUUID().toString().substring(0, 5);

        String url = constantUtil.getUnifiedorderUrl();
        String xml = CommonUtil.WXParamGenerate(description, outTradeNo, totalFee, ip, openid, constantUtil);
        String res = HttpUtil.httpsRequest(url, "POST", xml);


        try {
            QueryPayLogs queryPayLogs = new QueryPayLogs();
            queryPayLogs.setCrtTim(DateUtil.format(new Date()));
            queryPayLogs.setIpAddr(ip);
            queryPayLogs.setSendXml(xml);
            queryPayLogs.setUrl(url);
            queryPayLogs.setDescription(description);
            queryPayLogs.setOutTradeNo(outTradeNo);
            queryPayLogs.setTotalFee(totalFee);
            queryPayLogs.setReturnBody(res);
            queryPayLogsRepository.save(queryPayLogs);
        } catch (Exception e) {

        }


        Map<String, String> data;
        try {
            data = CommonUtil.doXMLParse(res);
        } catch (Exception e) {
            throw new BizException("订单提交失败，请重试");
        }


        String noneceStr = CommonUtil.GetNonceStr();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        try {
            QueryPay querypay = new QueryPay();
            querypay.setCode(code)
                    .setDescription(description)
                    .setIp(ip)
                    .setNonceStr(noneceStr)
                    .setOpenid(openid)
                    .setOutTradeNo(outTradeNo)
                    .setPrepayId(data.get("prepay_id"))
                    .setReciveXml(res)
                    .setResultCode(data.get("result_code"))
                    .setReturnCode(data.get("return_code"))
                    .setReturnMsg(data.get("return_msg"))
                    .setSendXml(xml)
                    .setTimeStamp(timeStamp)
                    .setCrtTim(DateUtil.format(new Date()))
                    .setTotalFee(totalFee);
            queryPayRepository.save(querypay);
        } catch (Exception e) {
            /**
             * 防止数据库插入错误影响查询。
             */
        }
        System.out.println(DateUtil.format(new Date()) + " 统一下单发送成功。");
        if (data.get("return_code").equals("SUCCESS") && data.get("result_code").equals("SUCCESS")) {
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


    @RequestMapping("/{appid}/refund")
    @ResponseBody
    public void refund(@PathVariable(value = "appid") String appid, String nonceStr) {
        List<QueryPay> queryPayByNonceStr = queryPayRepository.getQueryPayByNonceStr(nonceStr);
        CommonUtil.refund(queryPayByNonceStr, new ConstantUtil(appid, appTagRepository));
    }


    @RequestMapping("/notify")
    @ResponseBody
    public void notify(HttpServletRequest request) {

    }


}
