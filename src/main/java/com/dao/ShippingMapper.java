package com.dao;

import com.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    //根据用户删除自己的地址
    int deleteByShippingUserId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    //根据用户更新自己的地址
    int updateByShipping(Shipping record);

    //查询自己的地址
    Shipping selectByShippingUserId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    //所以地址
    List<Shipping> selectByUserId(@Param(value = "userId") Integer userId);
}