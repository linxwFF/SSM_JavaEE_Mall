package com.service;

import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import com.pojo.Shipping;

/**
 * Created by LINxwFF on 2017/11/25.
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse delete(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId,Shipping shipping);
    ServerResponse<Shipping> select(Integer userId,Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
