package com.orange.orangemall.controller;

import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Scheelite
 * @date 2021/11/13
 * @email jwei.gan@qq.com
 * @description
 **/

@RestController
@Api(tags = "管理员订单模块")
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation("订单发货")
    @PostMapping("/delivery")
    public ApiRestResponse delivery(@RequestParam String orderNo){
        orderService.delivery(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台订单列表")
    @GetMapping("/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNo, Integer pageSize){
        return ApiRestResponse.success(orderService.listForAdmin(pageNo,pageSize));
    }
}
