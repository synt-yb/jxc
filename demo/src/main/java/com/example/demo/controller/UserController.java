package com.example.demo.controller;
import com.example.demo.entity.RelativeParam;
import com.example.demo.entity.RequestInfo;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.validation.UserAddGroups;
import com.example.demo.validation.UserLoginGroups;
import com.example.demo.validation.UserUpdateGroups;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("login")
    public ResponseInfo login(@Validated(UserLoginGroups.class)@RequestBody User user){
        return userService.login(user);
    }

    @PostMapping("add")
    public ResponseInfo add(@Validated(UserAddGroups.class) @RequestBody User user) {
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
    public ResponseInfo update(@Validated(UserUpdateGroups.class) User user) {
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
