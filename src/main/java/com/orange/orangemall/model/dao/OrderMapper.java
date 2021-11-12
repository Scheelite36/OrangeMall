package com.orange.orangemall.model.dao;

import com.orange.orangemall.model.pojo.Order;
import io.swagger.models.auth.In;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByOrderNo(String orderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> selectAll();
}