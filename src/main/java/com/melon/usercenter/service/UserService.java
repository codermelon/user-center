package com.melon.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.melon.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author panjie
* @description 针对表【user】的数据库操作Service
* @createDate 2024-05-02 15:05:10
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param orginuser
     * @return
     */
    User getSafetyUser(User orginuser);
}
