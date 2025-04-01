package com.example.demo.controller;


import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.RoleService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    RoleService roleService;

    @PostMapping("add")
    public ResponseInfo add(Role role){
        if (!StringUtils.hasLength(role.getName()) || !StringUtils.hasLength(role.getFlag()))
            return ResponseInfo.error(400,"数据不完整");
        return roleService.add(role);
    }

    @PostMapping("delete")
    public ResponseInfo delete(Role role){

        return roleService.delete(role);
    }

    @PostMapping("update")
    public ResponseInfo update(Role role){
        if (!StringUtils.hasLength(role.getName()) || !StringUtils.hasLength(role.getFlag()))
            return ResponseInfo.error(400,"请输入正确的数据格式");
        return roleService.update(role);
    }

    @PostMapping("show")
    public ResponseInfo show(){
        return  roleService.show();
    }

    @PostMapping("select")
    public ResponseInfo select(Role role){

        return ResponseInfo.success(roleService.select(role));
    }

    @PostMapping("getUserRelativeRole")
    public ResponseInfo getUserRelativeRole(User user){
        if (!StringUtils.hasLength(user.getId()))
            return ResponseInfo.error(400,"id异常");
        return roleService.getUserRelativeRole(user);
    }



}
