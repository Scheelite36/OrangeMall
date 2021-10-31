package com.orange.orangemall.method;

import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.utils.MD5EncodeUtils;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

/**
 * @author Scheelite
 * @date 2021/10/10
 * @email jwei.gan@qq.com
 * @description
 **/

public class MD5Test {
    @Test
    public void showMD5Result() throws NoSuchAlgorithmException {
        String mdTest = "12345678"+ OrangeMallConstant.SALT;
        System.out.println(MD5EncodeUtils.getMD5Str(mdTest));
    }
}
