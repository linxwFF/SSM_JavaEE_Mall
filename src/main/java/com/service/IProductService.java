package com.service;

import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import com.pojo.Product;
import vo.ProductDetailVo;

/**
 * Created by LINxwFF on 2017/11/20.
 */
public interface IProductService {
    ServerResponse<String> addOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);
    //获取产品详情
    ServerResponse<ProductDetailVo> getDetail(Integer productId);
    //获取产品列表
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    //查询产品
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    //前端查询产品详细信息
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    //前端查询产品根据分类
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
