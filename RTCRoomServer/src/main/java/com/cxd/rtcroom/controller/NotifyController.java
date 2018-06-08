package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.dto.JSONResult;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class NotifyController {

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
            System.out.println("缴费数据数据：" + strcont);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONResult(true, "心跳成功");
    }




}
