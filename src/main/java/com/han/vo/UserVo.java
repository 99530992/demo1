package com.han.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;

    private String username;

    private String password;

    private boolean rememberMe;


}
