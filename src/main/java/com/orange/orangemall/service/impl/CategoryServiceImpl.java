package com.orange.orangemall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.model.dao.CategoryMapper;
import com.orange.orangemall.model.pojo.Category;
import com.orange.orangemall.model.request.CategoryReq;
import com.orange.orangemall.model.request.UpdateCategoryReq;
import com.orange.orangemall.model.vo.CategoryVO;
import com.orange.orangemall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryReq categoryReq){
        Category categoryOld = categoryMapper.selectByName(categoryReq.getName());
        Category category = new Category();
        BeanUtils.copyProperties(categoryReq,category);
        if (categoryOld != null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count != 1) {
            throw new OrangeMallException(OrangeMallExceptionEnum.INSERT_FAIL);
        }
    }

    @Override
    public void updateCategory(UpdateCategoryReq updateCategoryReq){
        if (updateCategoryReq.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategoryReq.getName());
            if (categoryOld != null && !categoryOld.getId().equals(updateCategoryReq.getId())) {
                throw new OrangeMallException(OrangeMallExceptionEnum.NAME_EXISTED);
            }
        }
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq,category);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count != 1) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    @Override
    public void deleteCategory(Integer id){
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0 ) {
            throw new OrangeMallException(OrangeMallExceptionEnum.DELETE_FAIL);
        }
    }

    @Override
    public PageInfo categoryVOList(Integer pageNum, Integer pageSize){
        // order使用的是数据库内的字段名称
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    public List<CategoryVO> categoryVOS(Integer parentId){
        // 返回商品分类信息及子目录
        List<CategoryVO> categoryVOS = new ArrayList<>();
        recursivelyFindCategories(categoryVOS,parentId);
        return categoryVOS;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOS, int parentId) {
        // 获取子目录
        List<Category> categoryList = categoryMapper.findCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            // 递归将子目录放入父目录中
            categoryList.forEach(
                    category -> {
                        CategoryVO categoryVO = new CategoryVO();
                        BeanUtils.copyProperties(category,categoryVO);
                        categoryVOS.add(categoryVO);
                        // 补充vo中的子目录列表
                        recursivelyFindCategories(categoryVO.getChildCategories(),categoryVO.getId());
                    }
            );
        }

    }
}
