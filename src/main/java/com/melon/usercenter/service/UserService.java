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
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    User doLogin(String userAccount, String userPassword, HttpServletRequest request);
}
