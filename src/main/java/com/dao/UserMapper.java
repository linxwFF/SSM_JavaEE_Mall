package com.dao;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //查询用户是否存在
    int checkUsername(String username);

    //查询登录用户信息
    User selectLogin(@Param("username") String username,@Param("password") String password);

    //查询email是否存在
    int checkEmail(String email);

    //查询用户的忘记密码的问题
    String selectQuestionByUsername(String username);

    //验证忘记密码的答案
    int checkAnswer(@Param("username") String username,@Param("question")String question,@Param("answer") String answer);

    //更新密码
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    //验证密码
    int checkPassword(@Param("password") String password,@Param("userId") Integer userId);

    //验证邮箱
    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}