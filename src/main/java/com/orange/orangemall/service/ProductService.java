package com.orange.orangemall.service;

import com.github.pagehelper.PageInfo;
import com.orange.orangemall.model.pojo.Product;
import com.orange.orangemall.model.request.AddProductReq;
import com.orange.orangemall.model.request.ProductListReq;
import com.orange.orangemall.model.request.UpdateProductReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Scheelite
 * @date 2021/10/16
 * @email jwei.gan@qq.com
 * @description
 **/
public interface ProductService {
    void add(AddProductReq addProductReq);

    String uploadFile(HttpServletRequest request, MultipartFile file) throws IOException, URISyntaxException;

    void update(UpdateProductReq updateProductReq);

    void delete(Integer id);

    void batchUpdateStatus(Integer[] ids, Integer status);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
