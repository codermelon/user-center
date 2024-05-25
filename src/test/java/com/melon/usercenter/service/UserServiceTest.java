package com.melon.usercenter.service;

import com.melon.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * ClassName: UserServiceTest
 * Package: com.melon.usercenter.service
 * Description:
 *
 * @Author melon
 * @Create 2024/5/2 15:19
 * @Version 1.0
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void test(){
        User user = new User();
        user.setUsername("testmelon");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123");
        user.setEmail("123");
        boolean result = userService.save(user);
        System.out.println(result);
    }

    @Test
    void userRegister() {
        String userAccount="jaymelon";
        String userPassword="12345678";
        String checkPassword="12345678";
        long register = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println(register);
    }
}