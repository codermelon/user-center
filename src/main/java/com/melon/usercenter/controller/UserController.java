package com.melon.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.melon.usercenter.model.domain.User;
import com.melon.usercenter.model.domain.request.UserLoginRequest;
import com.melon.usercenter.model.domain.request.UserRegisterRequest;
import com.melon.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.melon.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.melon.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * ClassName: UserController
 * Package: com.melon.usercenter.controller
 * Description:
 *
 * @Author melon
 * @Create 2024/5/22 23:10
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return -1;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        return userService.userRegister(userAccount,userPassword,checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        return userService.userLogin(userAccount,userPassword,request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userlist = userService.list(queryWrapper);
        return userlist.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){
            return false;
        }
        if(id<0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否有管理员权限
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可查询
        Object userobj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userobj;
        if(user == null || user.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;
    }
}
