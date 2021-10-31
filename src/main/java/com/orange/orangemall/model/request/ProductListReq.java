package com.orange.orangemall.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Scheelite
 * @date 2021/10/18
 * @email jwei.gan@qq.com
 * @description 商品列表查询请求
 **/
@Getter
@Setter
public class ProductListReq {

    private Integer categoryId;

    private String keyword;

    private String orderBy;

    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
