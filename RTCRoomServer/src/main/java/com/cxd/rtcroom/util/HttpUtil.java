package com.cxd.rtcroom.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.List;

public class HttpUtil {

    public static String httpsRequestRefund(String url, String xml, String mchId) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            PathUtil.getInstance().getWebRootPath();
            FileInputStream instream = new FileInputStream(new File("/" + PathUtil.getInstance().getWebRootPath() + "/" + mchId + ".p12"));
            try {
                keyStore.load(instream, mchId.toCharArray());
            } finally {
                instream.close();
            }

            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mchId.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            StringBuffer buffer = new StringBuffer();
            try {

                HttpPost httpPost = new HttpPost(url);

                // 构建消息实体
                StringEntity postentity = new StringEntity(xml.toString(), Charset.forName("UTF-8"));
                postentity.setContentEncoding("UTF-8");
                // 发送Json格式的数据请求
                httpPost.setEntity(postentity);
                CloseableHttpResponse response = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();

                    if (entity != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));

                        String text = null;
                        while ((text = bufferedReader.readLine()) != null) {
                            buffer.append(text);
                        }

                    }
                    EntityUtils.consume(entity);
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送http请求
     *
     * @param requestUrl    地址
     * @param requestMethod 设置请求方式（GET/POST）
     * @param outputStr     参数
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpURLConnectionGET(String geturl, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            // 把字符串转换为URL请求地址
            URL url = new URL(geturl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 连接会话
            connection.connect();
            // 获取输入流
            if (connection.getResponseCode() == 500) {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            String line = null;
            // 循环读取流
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // 关闭流
            br.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String httpURLConnectionPOST(String posturl, List<String> props, List<String> params, String charset) {
        try {
            URL url = new URL(posturl);

            // 将url 以 open方法返回的urlConnection 连接强转为HttpURLConnection连接
            // (标识一个url所引用的远程对象连接)
            // 此时cnnection只是为一个连接对象,待连接中
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据
            // application/x-javascript->json对象
            // application/x-www-form-urlencoded->表单数据
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 建立连接
            // (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            // URLEncoder.encode()方法为字符串进行编码
            String parm = "";
            if (props != null) {
                for (int i = 0; i < props.size(); i++) {
                    if (i != 0) {
                        parm += "&" + props.get(i) + "=" + URLEncoder.encode(params.get(i), "UTF8");
                    } else {
                        parm += props.get(i) + "=" + URLEncoder.encode(params.get(i), "UTF8");
                    }
                }
            }

            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            // 重要且易忽略步骤 (关闭流,切记!)
            dataout.close();
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据
            if (connection.getResponseCode() == 500) {
                return null;
            }
            // 连接发起请求,处理服务器响应 (从连接获取到输入流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            String line = null;
            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            // 重要且易忽略步骤 (关闭流,切记!)
            bf.close();

            // 销毁连接
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}