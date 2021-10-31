package com.orange.orangemall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Scheelite
 * @date 2021/10/24
 * @email jwei.gan@qq.com
 * @description 状态枚举类，可用于商品类的校验等
 **/
@Getter
@AllArgsConstructor
@Deprecated
public enum SatusEnum {
    PUT_ON(1),
    PUT_OFF(0);

    private Integer code;
}
