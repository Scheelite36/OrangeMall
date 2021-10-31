package com.orange.orangemall.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/21
 * @email jwei.gan@qq.com
 * @description 用于构建商品的查询
 **/
@Getter
@Setter
public class ProductListQuery {
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 分类id列表
     */
    private List<Integer> ids;
}
