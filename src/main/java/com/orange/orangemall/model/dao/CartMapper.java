package com.orange.orangemall.model.dao;

import com.orange.orangemall.model.pojo.Cart;
import com.orange.orangemall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.Caret;
import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    @Deprecated
    List<Cart> selectCartList(Integer userId);

    List<CartVO> selectCartVOList(Integer userId);

    Cart selectCart(@Param("userId") Integer userId, @Param("productId") Integer productId);

    int changeSelectStatus(@Param("userId") Integer userId, @Param("productId") Integer productId,
                     @Param("selected") Integer selected);

    int deleteByProductId(Integer productId);
}