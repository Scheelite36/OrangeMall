package com.orange.orangemall.utils;

import com.orange.orangemall.common.OrangeMallConstant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Scheelite
 * @date 2021/10/3
 * @email jwei.gan@qq.com
 * @description
 **/
public class MD5EncodeUtils {
    public static String getMD5Str(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((str+ OrangeMallConstant.SALT).getBytes()));
    }
}
