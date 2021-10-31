package com.orange.orangemall.model.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Scheelite
 * @date 2021/10/16
 * @email jwei.gan@qq.com
 * @description
 **/
@Getter
@Setter
public class UpdateProductReq {

    @NotNull(message = "商品id不能为空")
    private Integer id;

    @Length(message = "长度不超过{max}个字符",max = 256)
    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    private Integer price;

    private Integer stock;

    @Range(min = 0,max = 1,message = "状态为0或1")
    private Integer status;

}
