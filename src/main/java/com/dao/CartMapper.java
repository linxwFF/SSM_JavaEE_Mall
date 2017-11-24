package com.dao;

import com.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    //查询用户的购物车是否已经有该产品
    Cart selectCartByUserIdProductId(@Param(value = "userId") Integer userID,@Param(value = "productId") Integer productId);

    //查询用户的购物车信息
    List<Cart> selectCartByUserId(Integer userId);

    //判断购物车中的商品是否全选
    int selectCartProductCheckStatusByUserId(Integer userId);

    //多项删除
    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    //全选 全不选
    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param(value = "productId") Integer productId,@Param("checked") Integer checked);

    //购物车商品数量
    int selectCartProductCount(@Param("userId") Integer userId);
}