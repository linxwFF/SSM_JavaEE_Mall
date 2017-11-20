package com.controller.backend;

import com.common.Const;
import com.common.ResponseCode;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.ICategoryService;
import com.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by LINxwFF on 2017/11/19.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //验证是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //添加分类业务逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping("set_category.do")
    @ResponseBody
    public ServerResponse setCategory(HttpSession session,Integer categoryId,String categoryName)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //验证是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //添加分类业务逻辑  更新分类信息
            return iCategoryService.UpdateCategory(categoryName,categoryId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping("get_ChildrenCategory.do")
    @ResponseBody
    public ServerResponse getChildrenCategory(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //验证是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //添加分类业务逻辑  更新分类信息
            return iCategoryService.getChildrenCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }

    @RequestMapping("get_deep_ChildrenCategory.do")
    @ResponseBody
    public ServerResponse getChildrenCategoryDeep(HttpSession session,@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        //验证是否管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //添加分类业务逻辑   递归查询
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("无权限操作");
        }
    }
}
