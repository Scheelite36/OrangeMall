package com.orange.orangemall.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

/**
 * @author Scheelite
 * @date 2021/10/15
 * @email jwei.gan@qq.com
 * @description 添加商品的请求类
 **/
@Data
public class AddProductReq {

    @NotNull(message = "商品名称不能为空")
    @Length(message = "长度不超过{max}个字符",max = 256)
    private String name;

    @NotNull(message = "图片地址不能为空")
    private String image;

    @NotNull(message = "商品详情描述不能为空")
    private String detail;

    @NotNull(message = "商品分类id不能为空")
    private Integer categoryId;

    @NotNull(message = "商品价格不能为空")
    private Integer price;

    @NotNull(message = "商品库存不能为空")
    @Range(message = "库存默认大于{min}",min = 0)
    private Integer stock;

    @Range(max = 1,min = 0,message = "状态为0或1")
    private Integer status;

}
