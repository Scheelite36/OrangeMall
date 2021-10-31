package com.orange.orangemall.service;

import com.github.pagehelper.PageInfo;
import com.orange.orangemall.model.request.CategoryReq;
import com.orange.orangemall.model.request.UpdateCategoryReq;
import com.orange.orangemall.model.vo.CategoryVO;

import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description
 **/
public interface CategoryService {
    void addCategory(CategoryReq categoryReq);

    void updateCategory(UpdateCategoryReq updateCategoryReq);

    void deleteCategory(Integer id);

    PageInfo categoryVOList(Integer pageNum, Integer pageSize);

    List<CategoryVO> categoryVOS(Integer parentId);
}
