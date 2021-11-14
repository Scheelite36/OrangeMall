package com.orange.orangemall.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description 更新商品分类请求
 **/

@Data
public class UpdateCategoryReq {

    @NotNull(message = "商品分类id不能为空")
    private Integer id;

    @Length(message = "商品分类名需在{min}到{max}个字符之间",min = 2,max = 10)
    private String name;

    @Range(message = "目录层级需在{min}到{max}之间",min = 1,max = 3)
    private Integer type;

    @Range(message = "父目录id需大于0",min = 0)
    private Integer parentId;

    @Range(message = "排序级别需大于0",min = 0)
    private int orderNum;
}
