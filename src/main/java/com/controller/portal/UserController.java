package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.common.ServerResponse;
import com.pojo.User;
import com.service.IUserService;
import com.sun.deploy.nativesandbox.comm.Response;
import com.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by LINxwFF on 2017/11/15.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> Login(String username, String password, HttpSession session)
    {
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess())
        {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logOut(HttpSession session)
    {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user)
    {
        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type)
    {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null)
        {
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMsg("用户未登录,无法获取当前用户的信息");
    }

    @RequestMapping(value = "forget_get_question", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username)
    {
        return iUserService.selectQuestion(username);
    }

    //忘记密码验证 生成token
    @RequestMapping(value = "forget_check_answer", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer)
    {
        return iUserService.checkAnswer(username, question, answer);
    }

    //重置密码
    @RequestMapping(value = "forget_reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew, String forgetToken)
    {
        return iUserService.resetPassword(username, passwordNew, forgetToken);
    }

    //登录状态下重置密码
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew)
    {
        User current = (User) session.getAttribute(Const.CURRENT_USER);
        if(current == null)
        {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return iUserService.resetPasswordLogin(current,passwordOld,passwordNew);
    }

    //更新用户个人信息
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session,User user)
    {
        User current = (User) session.getAttribute(Const.CURRENT_USER);
        if(current == null)
        {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        user.setId(current.getId());
        user.setUsername(current.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }

        return response;
    }

    //获取个人信息
    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session)
    {
        User current = (User) session.getAttribute(Const.CURRENT_USER);
        if(current == null)
        {
            return ServerResponse.createByErrorMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，status = 10");
        }

        return iUserService.getInformation(current.getId());
    }
}
