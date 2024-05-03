package com.melon.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.melon.usercenter.mapper.UserMapper;
import com.melon.usercenter.model.domain.User;
import com.melon.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author panjie
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-05-02 15:05:10
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    // 混淆密码
    private static final String SALT = "melon";
    public static final String USER_LOGIN_STATE="userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 非空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        // 账户长度不小于4位
        if(userAccount.length()<4){
            return -1;
        }
        // 密码不小于8位
        if(userPassword.length()<8){
            return -1;
        }
        // 不包含特殊字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        // 如果包含非法字符，则返回
        if(matcher.find()){
            return (long) -1;
        }
        // 密码和检验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            return -1;
        }
        // 对密码进行加密
        String verifyPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

        // 向数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(verifyPassword);
        int insert = userMapper.insert(user);
        if(insert<0){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 非空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        // 账户长度不小于4位
        if(userAccount.length()<4){
            return null;
        }
        // 密码不小于8位
        if(userPassword.length()<8){
            return null;
        }
        // 不包含特殊字符
        String validRule = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validRule).matcher(userAccount);
        // 如果包含非法字符，则返回
        if(matcher.find()){
            return null;
        }
        // 2.对密码进行加密并对比
        String verifyPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",verifyPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if(user == null){
            log.info("user login failed, userAccount Cannot match userPassword");
            return null;
        }

        // 3.用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setUserAccount(user.getUserAccount());
        newUser.setAvatarUrl(user.getAvatarUrl());
        newUser.setGender(user.getGender());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setUserStatus(user.getUserStatus());
        newUser.setCreateTime(user.getCreateTime());

        // 4.记录用户的登录态（session），将其存到服务器上
        request.getSession().setAttribute(USER_LOGIN_STATE, newUser);

        // 5.返回脱敏后的用户信息
        return newUser;
    }
}




