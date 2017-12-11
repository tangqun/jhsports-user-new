package com.jhsports.user.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by TQ on 2017/12/4.
 */
public class CryptoHelper {
    public static String getMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        BASE64Encoder base64Encoder = new BASE64Encoder();
//        return base64Encoder.encode();
        byte[] md5Bytes = md5.digest(str.getBytes("utf-8"));
        return bytesToHex(md5Bytes);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toLowerCase();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String str = "123456";
        System.out.println(getMD5(str));
    }
}
