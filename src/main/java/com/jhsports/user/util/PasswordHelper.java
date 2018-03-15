package com.jhsports.user.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by TQ on 2017/12/4.
 */
public class PasswordHelper {
    public static String encrypto(String password, String salt, boolean isOld) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (isOld) {
            if (password.startsWith("0")) {
                password = password.substring(1);
            }

            password = CryptoHelper.getMD5(password.toLowerCase() + salt).toLowerCase();
        } else {
            password = CryptoHelper.getMD5(salt + password.toLowerCase()).toLowerCase();
        }

        return  password;
    }
}
