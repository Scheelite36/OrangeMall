package com.orange.orangemall.enums;

import com.orange.orangemall.exception.OrangeMallException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Scheelite
 * @date 2021/11/7
 * @email jwei.gan@qq.com
 * @description 订单状态: 0用户已取消，10未付款（初始状态），20已付款，30已发货，40交易完成
 **/
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    CANCLED(0,"取消"),
    UNPAID(10,"未付款"),
    PAID(20, "已付款"),
    DELIVERED(30,"已发货"),
    COMPLETED(40,"交易完成");
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态信息
     */
    private final String info;

    /**
     * 通过code返回info的方法
     * @param code
     * @return
     */
    public static String getOrderStatusInfo(Integer code){
        for(OrderStatusEnum orderStatusEnum: values()){
            if (code.equals(orderStatusEnum.getCode())) {
                return orderStatusEnum.getInfo();
            }
        }
        throw new OrangeMallException(OrangeMallExceptionEnum.NO_SUCH_ENUM);
    }
}
