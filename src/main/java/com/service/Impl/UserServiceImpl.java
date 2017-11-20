package com.service.Impl;

import com.common.Const;
import com.common.ServerResponse;
import com.common.TokenCache;
import com.dao.UserMapper;
import com.pojo.User;
import com.service.IUserService;
import com.utils.MD5Util;
import org.apache.commons.configuration.plist.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by LINxwFF on 2017/11/15.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password)
    {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        //解密密码登录md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null)
        {
            return ServerResponse.createByErrorMsg("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user)
    {
        ServerResponse vaildResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!vaildResponse.isSuccess())
        {
            return ServerResponse.createByErrorMsg("用户名已经存在");
        }

        vaildResponse = this.checkValid(user.getEmail(),Const.EMAIL);

        if(!vaildResponse.isSuccess())
        {
            return ServerResponse.createByErrorMsg("邮箱已经存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0)
        {
            return ServerResponse.createByErrorMsg("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type)
    {
        if(StringUtils.isNoneBlank(type))
        {
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0)
                {
                    return ServerResponse.createByErrorMsg("用户名已经存在");
                }
            }
            if(Const.EMAIL.equals(type))
            {
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0)
                {
                    return ServerResponse.createByErrorMsg("邮箱已经存在");
                }
            }

        }else{
            return ServerResponse.createByErrorMsg("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username)
    {
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNoneBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("找回密码的答案是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer)
    {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0)
        {
            //答案正确
            //生成token
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("问题的答案错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken)
    {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMsg("参数错误需要传token");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMsg("token无效，或者已经过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5password);
            if(rowCount>0)
            {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMsg("token错误，重新获取修改密码的token");
        }

        return ServerResponse.createByErrorMsg("修改密码错误");
    }

    public ServerResponse<String> resetPasswordLogin(User user,String passwordNew,String passwordOld)
    {
        //防止横向越权，校验一下用户的旧密码
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0)
        {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }
        //修改密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMsg("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user)
    {
        //username不能被更新
        //email校验
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0 ){
            return ServerResponse.createBySuccessMessage("email已经存在");
        }

        User UpdateUser = new User();
        UpdateUser.setId(user.getId());
        UpdateUser.setEmail(user.getEmail());
        UpdateUser.setPhone(user.getPhone());
        UpdateUser.setQuestion(user.getQuestion());
        UpdateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(UpdateUser);
        if (updateCount > 0)
        {
            return ServerResponse.createBySuccessMessage("更新个人信息成功");
        }

        return ServerResponse.createByErrorMsg("更新个人信息失败");

    }

    @Override
    public ServerResponse<User> getInformation(Integer userId)
    {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }

//        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backen
    public ServerResponse checkAdminRole(User user)
    {
        if(user == null && user.getRole().intValue() != Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMsg("用户权限不足");
        }
        return ServerResponse.createBySuccess();
    }

}
