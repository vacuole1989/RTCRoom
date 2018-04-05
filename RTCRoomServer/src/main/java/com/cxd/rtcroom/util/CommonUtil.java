package com.cxd.rtcroom.util;

import com.alibaba.fastjson.JSON;
import com.cxd.rtcroom.bean.QueryPay;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

public class CommonUtil {
    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串
     * 实现步骤:
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        if (paraMap == null) {
            return "";
        }
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                String key = item.getKey();
                String val = item.getValue();
                if (urlEncode) {  //如果是中文，则不参与编码
                    if (key.equals("package")) {

                    } else if (key.equals("body")) {

                    } else if (key.equals("notify_url")) {

                    } else {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                }
                if (keyToLower) {
                    buf.append(key.toLowerCase() + "=" + val);
                } else {
                    buf.append(key + "=" + val);
                }
                buf.append("&");
            }
            buff = buf.toString();
            if (!buff.equals("")) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    /**
     * Map转XML
     *
     * @param param
     * @return
     */
    public static String GetXMLFromMap(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String, String> m = new HashMap<>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static void refund(List<QueryPay> queryPayByNonceStr, ConstantUtil constantUtil) {
        if (queryPayByNonceStr.size() > 0) {
            QueryPay queryPay = queryPayByNonceStr.get(0);
            String outRefundNo = DateUtil.format(new Date(), "yyyyMMddHHmmssSS") + UUID.randomUUID().toString().substring(0, 5);

            String url = constantUtil.getRefundUrl();


            int fee = new BigDecimal(queryPay.getTotalFee()).multiply(new BigDecimal("100")).intValue();
            int rFee = new BigDecimal(queryPay.getTotalFee()).multiply(new BigDecimal("100")).intValue();
            Map<String, String> param = new HashMap<>();
            param.put("appid", constantUtil.getAppId());
            param.put("mch_id", constantUtil.getMchId());
            param.put("nonce_str", CommonUtil.GetNonceStr());
            param.put("out_trade_no", queryPay.getOutTradeNo());
            param.put("out_refund_no", outRefundNo);
            param.put("total_fee", fee + "");
            param.put("refund_fee", rFee + "");
            String sign = CommonUtil.GetSign(param, constantUtil);
            param.put("sign", sign);
            String xml = CommonUtil.GetXMLFromMap(param);


            HttpUtil.httpsRequestRefund(url, xml, constantUtil.getMchId());

            System.out.println(DateUtil.format(new Date())+" 退款成功。");

        }


    }

    public static String GetNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 获取签名
     *
     * @param param
     * @return
     */
    public static String GetSign(Map<String, String> param, ConstantUtil constantUtil) {
        String StringA = CommonUtil.formatUrlMap(param, false, false);
        String stringSignTemp = MD5Util.MD5(StringA + "&key=" + constantUtil.getApiKey()).toUpperCase();
        return stringSignTemp;
    }

    /**
     * 微信统一下单参数设置
     *
     * @param description 商品描述
     * @param outTradeNo  商户订单号
     * @param totalFee    标价金额（单位元）
     * @return
     */
    public static String WXParamGenerate(String description, String outTradeNo, String totalFee, String ip, String openid, ConstantUtil constantUtil) {
        int fee = new BigDecimal(totalFee).multiply(new BigDecimal("100")).intValue();
        Map<String, String> param = new HashMap<>();
        param.put("appid", constantUtil.getAppId());
        param.put("mch_id", constantUtil.getMchId());
        param.put("nonce_str", CommonUtil.GetNonceStr());
        param.put("body", description);
        param.put("out_trade_no", outTradeNo);
        param.put("total_fee", fee + "");
        param.put("spbill_create_ip", ip);
        param.put("notify_url", constantUtil.getNotifyUrl());
        param.put("trade_type", "JSAPI");
        param.put("openid", openid);


        String sign = CommonUtil.GetSign(param, constantUtil);

        param.put("sign", sign);
        return CommonUtil.GetXMLFromMap(param);
    }

    public static void sendTemplate(RestTemplate restTemplate, String totalFee, String searchTypeDesc, String sn, String openid, String outTradeNo, String prepayId, String nonceStr, ConstantUtil constantUtil) {
        Object forObject = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + constantUtil.getAppId() + "&secret=" + constantUtil.getAppSecret(), Object.class);
        String access_token = (String) JSON.parseObject(JSON.toJSONString(forObject), Map.class).get("access_token");
        Map<String, Object> params = new HashMap<>();
        params.put("template_id", constantUtil.getMessageTemplate());
        params.put("touser", openid);
        params.put("form_id", prepayId);
        params.put("page", "pages/result/index?str=" + nonceStr);
        Map<String, Map<String, String>> tdata = new HashMap<>();
        Map<String, String> tt = new HashMap<>();
        tt.put("value", searchTypeDesc);
        tdata.put("keyword1", tt);
        tt = new HashMap<>();
        tt.put("value", sn);
        tdata.put("keyword2", tt);
        tt = new HashMap<>();
        tt.put("value", "请进入小程序查看本次查询结果（为了保护隐私，数据48小时候清除）");
        tdata.put("keyword3", tt);
        tt = new HashMap<>();
        tt.put("value", outTradeNo);
        tdata.put("keyword4", tt);
        tt = new HashMap<>();
        tt.put("value", totalFee + "元");
        tdata.put("keyword5", tt);

        params.put("data", tdata);

        String post = HttpUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token, "POST", JSON.toJSONString(params));

        System.out.println(DateUtil.format(new Date())+" 消息模板发送成功。");
    }


}
