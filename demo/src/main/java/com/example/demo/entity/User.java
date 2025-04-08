package com.example.demo.entity;

import com.example.demo.validation.UserAddGroups;
import com.example.demo.validation.UserLoginGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class User {
    private String id;

    @Size(min = 1, message = "请输入账号", groups = {UserAddGroups.class, UserLoginGroups.class})
    private String account;

    @Size(min = 1, message = "请输入用户名", groups = UserAddGroups.class)
    private String name;

    @Size(min = 1, message = "请输入密码", groups = UserLoginGroups.class)
    private String password;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;
    private String remark;
    private String photo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;
}
