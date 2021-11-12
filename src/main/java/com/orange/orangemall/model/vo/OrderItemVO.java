package com.orange.orangemall.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Scheelite
 * @date 2021/11/7
 * @email jwei.gan@qq.com
 * @description 订单VO中包含的单个商品信息
 **/

@Getter
@Setter
public class OrderItemVO {
    private String orderNo;

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;

}
