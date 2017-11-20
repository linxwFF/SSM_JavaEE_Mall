package com.service;

import com.common.ServerResponse;
import com.pojo.User;

/**
 * Created by LINxwFF on 2017/11/15.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> resetPassword(String username,String passwordNew,String forgetToken);
    ServerResponse<String> resetPasswordLogin(User user,String passwordNew,String passwordOld);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(Integer userId);
    ServerResponse checkAdminRole(User user);
}
