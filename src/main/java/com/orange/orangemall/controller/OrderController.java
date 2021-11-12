package com.orange.orangemall.controller;

import com.google.zxing.WriterException;
import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.model.request.AddOrderRequest;
import com.orange.orangemall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author Scheelite
 * @date 2021/11/3
 * @email jwei.gan@qq.com
 * @description
 **/

@RestController
@Api(tags = "订单模块")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("/add")
    public ApiRestResponse add(@Valid @RequestBody AddOrderRequest addOrderRequest) {
        return ApiRestResponse.success(orderService.add(addOrderRequest));
    }

    @ApiOperation("订单详情")
    @GetMapping("/detail")
    public ApiRestResponse detail(@RequestParam String orderNo){
        return ApiRestResponse.success(orderService.detail(orderNo));
    }

    @ApiOperation("订单列表")
    @GetMapping("/list")
    public ApiRestResponse list(@RequestParam Integer pageNo, @RequestParam Integer pageSize ){
        return ApiRestResponse.success(orderService.list(pageNo,pageSize));
    }

    @ApiOperation("取消订单")
    @PostMapping("/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("生成二维码")
    @PostMapping("/QRCode")
    public ApiRestResponse qrCode(@RequestParam String orderNo) throws IOException, WriterException {
        return ApiRestResponse.success(orderService.qrCode(orderNo));
    }

    @ApiOperation("支付订单")
    @PostMapping("/pay")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("完成订单")
    @PostMapping("/finish")
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("订单发货")
    @PostMapping("/admin/delivery")
    public ApiRestResponse delivery(@RequestParam String orderNo){
        orderService.delivery(orderNo);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台订单列表")
    @GetMapping("/admin/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNo, Integer pageSize){
        return ApiRestResponse.success(orderService.listForAdmin(pageNo,pageSize));
    }
}
