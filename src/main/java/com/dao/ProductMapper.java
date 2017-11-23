package com.dao;

import com.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //获取产品列表
    List<Product> selectList();

    //搜索产品
    List<Product> selectByNameAndProductId(@Param(value = "productName") String productName,@Param(value = "productId") Integer productId);

    //搜索产品 根据分类
    List<Product> selectByNameAndCategoryIds(@Param(value = "productName") String productName,@Param(value = "categoryIdList") List<Integer> categoryIdList);
}