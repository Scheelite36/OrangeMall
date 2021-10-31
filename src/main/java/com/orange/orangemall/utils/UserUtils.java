package com.orange.orangemall.utils;

import com.orange.orangemall.model.pojo.User;

/**
 * @author Scheelite
 * @date 2021/10/30
 * @email jwei.gan@qq.com
 * @description 存储当前线程的用户信息工具类
 **/
public abstract class UserUtils {
    // 创建线程变量，存放用户实体
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setLoginUser(User user){
        userThreadLocal.set(user);
    }

    public static void removeUser(){
        userThreadLocal.remove();
    }

    public static User getLoginUser(){
        return userThreadLocal.get();
    }

    public static Integer getLoginUserId(){
        User user = userThreadLocal.get();
        if (user.getId()==null) {
            return null;
        }
        return user.getId();
    }
}
