package com.orange.orangemall.model.request;

import com.orange.orangemall.common.OrangeMallConstant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Scheelite
 * @date 2021/11/3
 * @email jwei.gan@qq.com
 * @description 新增订单传入参数
 **/
@Data
public class AddOrderRequest {

    @NotNull(message = "收件人不为空")
    private String receiverName;

    @NotNull(message = "手机号不为空")
    @Pattern(regexp = "^[1][0-9]{10}",message = "手机号格式错误")
    private String receiverMobile;

    @NotNull(message = "收货地址不为空")
    private String receiverAddress;

    private Integer postage = OrangeMallConstant.ZERO_POSTAGE;

    private Integer paymentType = OrangeMallConstant.ONLINE_PAY;
}
