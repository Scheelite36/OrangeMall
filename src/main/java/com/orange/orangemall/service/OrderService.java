package com.orange.orangemall.service;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.orange.orangemall.model.request.AddOrderRequest;
import com.orange.orangemall.model.vo.OrderVO;

import java.io.IOException;

/**
 * @author Scheelite
 * @date 2021/11/3
 * @email jwei.gan@qq.com
 * @description
 **/
public interface OrderService {

    String add(AddOrderRequest addOrderRequest);

    OrderVO detail(String orderNo);

    PageInfo list(Integer pageNumm, Integer pageSize);

    void cancel(String orderNo);

    String qrCode(String orderNo) throws IOException, WriterException;

    void pay(String orderNo);

    void finish(String orderNo);

    void delivery(String orderNo);

    // todo 传入可选排序内容
    PageInfo listForAdmin(Integer pageNo, Integer pageSize);
}
