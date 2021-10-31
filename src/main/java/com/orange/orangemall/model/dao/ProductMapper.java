package com.orange.orangemall.model.dao;

import com.orange.orangemall.model.pojo.Product;
import com.orange.orangemall.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    Product selectByName(String name);

    int batchUpdateStatus(@Param("ids") Integer[] ids, @Param("status") Integer status);

    List<Product> selectAll();

    List<Product> selectSelected(ProductListQuery query);
}