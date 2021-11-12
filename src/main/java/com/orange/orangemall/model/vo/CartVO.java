package com.orange.orangemall.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Scheelite
 * @date 2021/10/30
 * @email jwei.gan@qq.com
 * @description 返回给前端的单个购物车对象
 **/

@Getter
@Setter
public class CartVO {
    // 购物车id
    public Integer id;
    // 商品id
    public Integer productId;
    // 用户id
    public Integer userId;
    // 数量
    public Integer quantity;
    // 是否选中
    public Integer selected;
    // 商品单价
    public Integer price;
    // 商品总价
    public Integer totalPrice;
    // 商品名称
    public String productName;
    // 商品图片
    public String productImage;
}
