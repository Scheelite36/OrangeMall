package com.orange.orangemall.controller;

import com.github.pagehelper.PageInfo;
import com.orange.orangemall.common.ApiRestResponse;
import com.orange.orangemall.model.request.AddProductReq;
import com.orange.orangemall.model.request.UpdateProductReq;
import com.orange.orangemall.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/16
 * @email jwei.gan@qq.com
 * @description
 **/
@RestController
@RequestMapping("/admin/product")
@Api(tags = "后台商品模块")
public class AdminProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    @ApiOperation("添加商品")
    public ApiRestResponse add(@Valid @RequestBody AddProductReq addProductReq){
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    public ApiRestResponse uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        String url = productService.uploadFile(request,file);
        return ApiRestResponse.success(url);
    }

    @PostMapping("/update")
    @ApiOperation("更新商品")
    public ApiRestResponse update(@Valid @RequestBody UpdateProductReq updateProductReq){
        productService.update(updateProductReq);
        return ApiRestResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除商品")
    public ApiRestResponse delete(@NotNull(message = "商品id不能为空") @RequestParam Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @PostMapping("/batchUpdateSallSatuts")
    @ApiOperation("批量上下架")
    // todo 枚举校验
    public ApiRestResponse batchUpdateStatus(@RequestParam Integer[] ids,
                                                 @RequestParam Integer status){
        productService.batchUpdateStatus(ids,status);
        return ApiRestResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation("商品列表展示")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize){
        PageInfo pageInfo = productService.listForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}

