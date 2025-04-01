package com.example.demo.controller;
import com.example.demo.entity.RelativeParam;
import com.example.demo.entity.RequestInfo;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("login")
    public ResponseInfo login(User user){
        //后端验证
        if (!StringUtils.hasLength(user.getAccount())){
            return ResponseInfo.error(400,"账号不能为空");
        }
        if (!StringUtils.hasLength(user.getPassword())){
            return ResponseInfo.error(400,"密码不能为空");
        }
        return userService.login(user);
    }

    @PostMapping("add")
    public ResponseInfo add(User user){
        if (!StringUtils.hasLength(user.getAccount())||!StringUtils.hasLength(user.getName()))
            return ResponseInfo.error(400,"账号和姓名不能为空");
        return userService.add(user);
    }

    @PostMapping("delete")
    public ResponseInfo delete(User user){
        if (user.getId()==null)
            return ResponseInfo.error(400,"id异常");
        return userService.delete(user);
    }
    @PostMapping("giveUserRole")
    public ResponseInfo giveUserRole(@RequestBody RelativeParam param){
        return userService.giveUserRole(param);
    }


    @PostMapping("getUserInfo")
    public ResponseInfo getUserInfo(String id){
        return userService.getUserInfo(id);
    }

    @PostMapping("update")
    public ResponseInfo update(User user) {
        if (!StringUtils.hasLength(user.getAccount())||!StringUtils.hasLength(user.getName()))
            return ResponseInfo.error(400,"账号和姓名不能为空");
        return userService.update(user);
    }

    @PostMapping("getUserPower")
    public ResponseInfo getUserPower(User user){
        return userService.getUserPower(user);
    }

    @PostMapping("show")
    public ResponseInfo show(User user, RequestInfo input){
        return userService.show(user, input);
    }




}
