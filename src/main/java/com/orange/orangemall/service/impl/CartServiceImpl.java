package com.orange.orangemall.service.impl;

import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.model.dao.CartMapper;
import com.orange.orangemall.model.dao.ProductMapper;
import com.orange.orangemall.model.pojo.Cart;
import com.orange.orangemall.model.pojo.Product;
import com.orange.orangemall.model.vo.CartVO;
import com.orange.orangemall.service.CartService;
import com.orange.orangemall.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Scheelite
 * @date 2021/10/30
 * @email jwei.gan@qq.com
 * @description
 **/

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public List<CartVO> list() {
        Integer userId = UserUtils.getLoginUserId();
        return cartMapper.selectCartVOList(userId);
    }

    @Override
    public List<CartVO> add(Integer productId, Integer count) {
        validProduct(productId, count);
        Integer userId = UserUtils.getLoginUserId();
        // 通过用户id 商品id查找购物车中的商品，如果存在则叠加数量，如果不存在则新增购物车记录
        Cart cart = cartMapper.selectCart(userId, productId);
        Cart newCart = new Cart();
        if (cart == null) {
            newCart.setQuantity(count);
            newCart.setProductId(productId);
            newCart.setSelected(OrangeMallConstant.SELECTED);
            newCart.setUserId(userId);
            int re = cartMapper.insertSelective(newCart);
            if (re == 0) {
                throw new OrangeMallException(OrangeMallExceptionEnum.INSERT_FAIL);
            }
        } else {
            newCart.setUserId(cart.getId());
            newCart.setQuantity(cart.getQuantity() + count);
            newCart.setSelected(OrangeMallConstant.SELECTED);
            int re = cartMapper.updateByPrimaryKeySelective(newCart);
            if (re == 0) {
                throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
            }
        }
        return cartMapper.selectCartVOList(userId);
    }

    /**
     * 验证商品是否存在，库存是否足够
     *
     * @param productId
     * @param count
     */
    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus().equals(OrangeMallConstant.OFF_SALE)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        // 简单校验库存，但是没有将已加入购物车的数量进行比对。对于库存需要在下订单时候再次校验
        if (count > product.getStock()) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> update(Integer productId, Integer count) {
        validProduct(productId, count);
        Integer userId = UserUtils.getLoginUserId();
        Cart cart = cartMapper.selectCart(userId, productId);
        if (cart == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        Cart newCart = new Cart();
        newCart.setQuantity(count);
        newCart.setId(cart.getId());
        int re = cartMapper.updateByPrimaryKeySelective(newCart);
        if (re == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
        return cartMapper.selectCartVOList(userId);
    }

    @Override
    public List<CartVO> delete(Integer productId) {
        Integer userId = UserUtils.getLoginUserId();
        int count = cartMapper.deleteByPrimaryKey(productId);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.DELETE_FAIL);
        }
        return cartMapper.selectCartVOList(userId);
    }

    @Override
    public List<CartVO> select(Integer productId, Integer selected) {
        Integer userId = UserUtils.getLoginUserId();
        int count = cartMapper.changeSelectStatus(userId, productId, selected);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
        return cartMapper.selectCartVOList(userId);
    }

    @Override
    public List<CartVO> selectAll(Integer selected) {
        Integer userId = UserUtils.getLoginUserId();
        int count = cartMapper.changeSelectStatus(userId, null, selected);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
        return cartMapper.selectCartVOList(userId);
    }
}
