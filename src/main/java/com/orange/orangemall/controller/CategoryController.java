package com.orange.orangemall.controller;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.model.vo.CategoryVO;
import com.orange.orangemall.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

/**
 * @author Scheelite
 * @date 2021/11/13
 * @email jwei.gan@qq.com
 * @description
 **/

@RestController
@RequestMapping("/category")
@Api(tags = "商品分类模块")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("递归商品分类列表 for user")
    @Cacheable(value = "categoryList")
    @ResponseBody
    public ApiRestResponse categoryList(){
        List<CategoryVO> categoryVOS = categoryService.categoryVOS(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
