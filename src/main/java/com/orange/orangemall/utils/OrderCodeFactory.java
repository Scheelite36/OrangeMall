package com.orange.orangemall.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SplittableRandom;

/**
 * @author Scheelite
 * @date 2021/11/1
 * @email jwei.gan@qq.com
 * @description 用于生成加密的订单编号
 **/
public class OrderCodeFactory {

    // 订单类别头
    private static final String ORDER_CODE = "1";
    // 用于加密的数组
    private static final int[] r = new int[]{2, 0, 2, 1, 1, 1, 0, 1, 3, 6};

    // id+随机数最大长度
    private static final int max_length = 5;

    /**
     * @param n
     * @return 固定位数的随机数
     */
    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        return new SplittableRandom().nextLong(max - min) + min;
    }

    /**
     * @param id
     * @return max_length长度的id+随机数
     */
    private static String toCode(Integer id) {
        String idStr = id.toString();
        StringBuilder ids = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            ids.append(r[idStr.charAt(i) - '0']);
        }
        return ids.append(getRandom(max_length - idStr.length())).toString();
    }

    /**
     * 时间戳
     *
     * @return
     */
    private static String getDateFormat() {
        DateFormat sdf = new SimpleDateFormat("HHmmss");
        return sdf.format(new Date());
    }

    /**
     * 生成编码
     *
     * @param userId
     * @return
     */
    private static synchronized String getCode(Integer userId) {
        userId = userId == null ? 10000 : userId;
        return getDateFormat() + toCode(userId);
    }

    /**
     * 生成订单编码
     * @param userId
     * @return
     */
    public static String getOrderCode(Integer userId) {
        return ORDER_CODE + getCode(userId);
    }
}
