package com.orange.orangemall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.model.request.CategoryReq;
import com.orange.orangemall.model.request.UpdateCategoryReq;
import com.orange.orangemall.model.vo.CategoryVO;
import com.orange.orangemall.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/7
 * @email jwei.gan@qq.com
 * @description
 **/
@Controller
@RequestMapping("/category")
@Api(tags = "商品分类模块")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/add")
    @ApiOperation("添加商品分类")
    @ResponseBody
    public ApiRestResponse addCategory(@Valid @RequestBody CategoryReq categoryReq){
        categoryService.addCategory(categoryReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/update")
    @ApiOperation("更新商品分类")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq){
        categoryService.updateCategory(updateCategoryReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/delete")
    @ApiOperation("删除商品分类")
    @ResponseBody
    public ApiRestResponse deleteCategory(@NotNull(message = "id不能为空") @RequestParam Integer id){
        categoryService.deleteCategory(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/admin/list")
    @ApiOperation("商品分类分页接口 for admin")
    @ResponseBody
    public ApiRestResponse categoryPagedList(@NotNull(message = "页码不能为空") @RequestParam Integer pageNum,
                                             @NotNull(message = "分页大小不为空") @RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.categoryVOList(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @GetMapping("/list")
    @ApiOperation("递归商品分类列表 for user")
    @Cacheable(value = "categoryList")
    @ResponseBody
    public ApiRestResponse categoryList(){
        List<CategoryVO> categoryVOS = categoryService.categoryVOS(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
