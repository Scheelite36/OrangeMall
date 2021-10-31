package com.orange.orangemall.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description 用于展示商品分类列表
 **/
@Getter
@Setter
public class CategoryVO implements Serializable {

    private int id;

    private String name;

    private int type;

    private int parentId;

    private int orderNum;
    // 子目录，需要初始化，因为在impl中是先get获取再放子目录进入
    private List<CategoryVO> childCategories = new ArrayList<>();

    private Date createTime;

    private Date updateTime;
}
