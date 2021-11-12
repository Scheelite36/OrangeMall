package com.orange.orangemall.common;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Scheelite
 * @date 2021/10/3
 * @email jwei.gan@qq.com
 * @description 保存使用的常量
 **/

@Component
public class OrangeMallConstant {

    /**
     * session用户信息
     */
    public static final String MALL_USER = "orange_mall_user";
    /**
     * 密码加盐的随机数
     */
    public static final String SALT = "./.AnOrange%^&";

    /**
     * 文件上传的存放路径
     */
    public static String FILE_UPLOAD_DIR;
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    /**
     * 文件访问url
     */
    public static String FILE_ACCESS;
    @Value("${file.access.url}")
    public void setFileAccess(String fileAccess){
        FILE_ACCESS = fileAccess;
    }

    /**
     * 排序字段set
     */
    public static final Set<String> ORDER_BY = new HashSet<String>(Arrays.asList("price desc","price asc"));

    /**
     * 商品上下架状态
     */
    public static final Integer ON_SALE = 1;
    public static final Integer OFF_SALE = 0;
    /**
     * 购物车选中状态
     */
    public static final Integer SELECTED = 1;
    public static final Integer UNSELECTED = 0;

    /**
     * 订单是否包邮，邮费
     */
    public static final Integer ZERO_POSTAGE = 0;

    /**
     * 支付方式
     */
    public static final Integer ONLINE_PAY = 1;

    /**
     * 用户角色
     */
    public static final Integer ADMIN_ROLE = 2;
    public static final Integer CUSTOM_ROLE =1;
}
