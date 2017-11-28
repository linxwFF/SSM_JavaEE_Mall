package com.service;

import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import vo.OrderVo;

/**
 * Created by LINxwFF on 2017/11/27.
 */
public interface IOrderService {

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse<String> cancel(Integer userId,Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);
}
