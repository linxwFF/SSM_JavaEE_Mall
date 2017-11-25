package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.common.ServerResponse;
import com.github.pagehelper.PageInfo;
import com.pojo.Shipping;
import com.pojo.User;
import com.service.IShippingService;
import com.service.Impl.ShippingServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by LINxwFF on 2017/11/25.
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session,Shipping shipping)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return  iShippingService.add(user.getId(),shipping);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session,Integer shippingId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return  iShippingService.delete(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,Shipping shipping)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return  iShippingService.update(user.getId(),shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session,Integer shippingId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return  iShippingService.select(user.getId(),shippingId);
    }

    //地址分页
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return  iShippingService.list(user.getId(),pageNum,pageSize);
    }


}
