package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.dao.ArticleRepository;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.tls.tls_sigature.tls_sigature;
import com.cxd.rtcroom.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class ChatController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ArticleRepository articleRepository;


    @RequestMapping("/getUserSign")
    public JSONResult getArticleList(@RequestBody Map map) {

        String userId = map.get("id")+"";

        tls_sigature.GenTLSSignatureResult result = null;
        try {
            result = tls_sigature.GenTLSSignatureEx(Config.IM.IM_SDKAPPID, userId, Config.IM.PRIVATEKEY);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(result.urlSig);


        return new JSONResult(true, "查找成功", result);

    }
}
