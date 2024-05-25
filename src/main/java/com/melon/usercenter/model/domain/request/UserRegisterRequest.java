package com.melon.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserRegisterRequest
 * Package: com.melon.usercenter.model.domain.request
 * Description:
 *
 * @Author melon
 * @Create 2024/5/22 23:15
 * @Version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1949072670449089912L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
