package com.controller.portal;

import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import com.pojo.Product;
import com.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ProductDetailVo;

/**
 * Created by LINxwFF on 2017/11/23.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId)
    {
        return iProductService.getProductDetail(productId);
    }

    //前台搜索产品
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> searchList(@RequestParam(value = "keyword",required = false) String keyword,
                                               @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                               @RequestParam(value = "orderBy",defaultValue = "") String orderBy)
    {
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
