package com.cxd.rtcroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
@RequestMapping("/app")
public class QueryController {


    private static final String APPID="wx30dde37837561559";
    private static final String sessionKey="https://api.weixin.qq.com/sns/jscode2session";
    private static final String SECRET="ed201c3998d2fb9ecb32f9c2a6c42aec";


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/onLogin")
    public Map onLogin(@RequestParam String code,) {

        System.out.println(code);
        Map map = restTemplate.getForObject("{sessionkey}?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code", Map.class, sessionKey, APPID, SECRET, code);
        System.out.println(map);
        String openid =map.get("openid")+"";
        String sessionKey=map.get("session_key")+"";
        System.out.println(openid);
        System.out.println(sessionKey);





        return null;
    }

}
