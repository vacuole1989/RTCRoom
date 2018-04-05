package com.cxd.rtcroom.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class Base64Util {

    /**
     * 加密
     *
     * @param str
     * @return
     */

    public static String encode(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = Base64.encodeBase64String(b);
        }
        return s;
    }

    /**
     * 解密
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        byte[] b;
        String result = null;
        if (s != null) {
            try {
                b = Base64.decodeBase64(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 文件转base64字符串
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeBase64String(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * base64字符串转文件
     *
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64,String fileToPath) {
        File file = null;
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            file = new File(fileToPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes = Base64.decodeBase64(base64);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void main(String[] args) {
        String str=fileToBase64(new File("D:/1.zip"));
        System.out.println(str);
        File file = base64ToFile(str,"D:/2.zip");
    }

}
