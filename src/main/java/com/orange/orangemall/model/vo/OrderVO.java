package com.orange.orangemall.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Scheelite
 * @date 2021/11/7
 * @email jwei.gan@qq.com
 * @description 用于订单对象的返回
 **/
@Getter
@Setter
public class OrderVO {

    private String orderNo;

    private Integer userId;

    private Integer totalPrice;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private Integer orderStatus;

    private Integer postage;

    private Integer paymentType;

    private Date deliveryTime;

    private Date payTime;

    private Date endTime;

    private Date createTime;

    private List<OrderItemVO> orderItemVOList;

    private String orderStatusName;

}
