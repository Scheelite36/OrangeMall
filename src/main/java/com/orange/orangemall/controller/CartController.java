package com.orange.orangemall.controller;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Scheelite
 * @date 2021/10/30
 * @email jwei.gan@qq.com
 * @description
 **/

@RestController
@Api(tags = "购物车模块")
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    @ApiOperation("返回购物车列表")
    public ApiRestResponse list() {
        return ApiRestResponse.success(cartService.list());
    }

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(Integer productId, Integer count) {
        return ApiRestResponse.success(cartService.add(productId, count));
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(Integer productId, Integer count) {
        return ApiRestResponse.success(cartService.update(productId, count));
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车")
    public ApiRestResponse delete(Integer productId) {
        return ApiRestResponse.success(cartService.delete(productId));
    }

    @PostMapping("/select")
    @ApiOperation("更新选中状态")
    public ApiRestResponse select(Integer productId, Integer selected){
        return ApiRestResponse.success(cartService.select(productId,selected));
    }

    @PostMapping("/selectAll")
    @ApiOperation("更新全选状态")
    public ApiRestResponse selectAll(Integer selected){
        return ApiRestResponse.success(cartService.selectAll(selected));
    }
}
