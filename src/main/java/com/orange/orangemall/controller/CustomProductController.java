package com.orange.orangemall.controller;

import com.github.pagehelper.PageInfo;
import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.model.pojo.Product;
import com.orange.orangemall.model.request.ProductListReq;
import com.orange.orangemall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Scheelite
 * @date 2021/10/17
 * @email jwei.gan@qq.com
 * @description 提供给用户的商品接口
 **/
@RestController
@Api(tags = "提供给用户的商品接口")
@RequestMapping("/product")
public class CustomProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/detail")
    @ApiOperation("商品详情")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product= productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @GetMapping("/list")
    @ApiOperation("前台商品列表")
    public ApiRestResponse list(@Valid @RequestBody ProductListReq productListReq){
        PageInfo pageInfo = productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }
}
