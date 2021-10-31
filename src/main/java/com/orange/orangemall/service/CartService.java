package com.orange.orangemall.service;

import com.orange.orangemall.model.pojo.Cart;
import com.orange.orangemall.model.vo.CartVO;

import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/30
 * @email jwei.gan@qq.com
 * @description
 **/
public interface CartService {
    List<CartVO> list();

    List<CartVO> add(Integer productId, Integer count);

    List<CartVO> update(Integer productId, Integer count);

    List<CartVO> delete(Integer productId);

    List<CartVO> select(Integer productId, Integer selected);

    List<CartVO> selectAll(Integer selected);
}
