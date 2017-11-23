package com.service.Impl;

import com.common.Const;
import com.common.ResponseCode;
import com.common.ServerResponse;
import com.dao.CategoryMapper;
import com.dao.ProductMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.pojo.Category;
import com.pojo.Product;
import com.service.ICategoryService;
import com.service.IProductService;
import com.utils.DateTimeUtil;
import com.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import vo.ProductDetailVo;
import vo.ProductListVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LINxwFF on 2017/11/20.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse<String>  addOrUpdateProduct(Product product)
    {
        if(product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String [] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功~");
                }
                return ServerResponse.createByErrorMsg("更新产品失败");
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponse.createBySuccess("新增产品成功~");
                }
                return ServerResponse.createByErrorMsg("新增产品失败~");
            }

        }

        return ServerResponse.createByErrorMsg("参数错误，添加或更新产品失败");
    }

    @Override
    public ServerResponse<String>  setSaleStatus(Integer productId,Integer status)
    {
        if(productId == null || status == null){
            return ServerResponse.createByErrorMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("状态更新成功");
        }
        return ServerResponse.createByErrorMsg("状态更新失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> getDetail(Integer productId)
    {
        if(productId == null)
        {
            return ServerResponse.createByErrorMsg("没有相关数据");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMsg("无法获取该产品信息。");
        }

        return ServerResponse.createBySuccess(assembleProductDetailVo(product));
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize)
    {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();

        //组装转换 vo
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //分页结果
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize)
    {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        //组装转换 vo
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem: productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //分页结果
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //组装 vo
    private ProductDetailVo assembleProductDetailVo(Product product)
    {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        //没有分类，就代表是父节点
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0); //默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.DateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.DateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    //组装vo
    private ProductListVo assembleProductListVo(Product product)
    {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setCategoryId(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setSubtitle(product.getSubtitle());

        return productListVo;
    }

    //前台获取产品详细信息
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId)
    {
        if(productId == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMsg("无法获取该产品信息。");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMsg("产品已经下架了");
        }
        return ServerResponse.createBySuccess(assembleProductDetailVo(product));
    }

    //前台获取搜索的结果list
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy)
    {
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();    //保存category
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                //没有查到分类
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }

        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuffer("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //排序
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String [] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " "+ orderByArray[1]);
            }
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size() == 0 ?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product item : productList){
            ProductListVo productListVo = assembleProductListVo(item);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
