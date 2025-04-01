package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController {

    @Resource
    MenuService menuService;


    /**
     * 添加菜单
     */
    @PostMapping("add")
    public ResponseInfo add(Menu menu) {
        //后端验证
        if(!StringUtils.hasLength(menu.getText())||
                !StringUtils.hasLength(menu.getIcon())||
                !StringUtils.hasLength(menu.getOrder())){
            return ResponseInfo.error(400,"数据不完整");
        }

        if (StringUtils.hasLength(menu.getUrl())&&!menu.getUrl().equals("#"))
            if (menu.getUrl().lastIndexOf("/")==-1){
                return ResponseInfo.error(400,"请输入正确的url");
            }
        return menuService.add(menu);
    }

    @PostMapping("delete")
    public ResponseInfo delete(Menu menu) {
        return menuService.delete(menu);
    }

    @PostMapping("update")
    public ResponseInfo update(Menu menu) {
        return menuService.update(menu);
    }

    @PostMapping("updateRoleMenu")
    public ResponseInfo updateRoleMenu(@RequestBody RelativeParam param) {
        return menuService.updateRoleMenu(param);
    }

    @PostMapping("getAll")
    public ResponseInfo getMenu() {
        return menuService.getAll();
    }

    @PostMapping("selectByUser")
    public ResponseInfo selectByUser(User user) {
        if (!StringUtils.hasLength(user.getId())){
            return ResponseInfo.error(400,"用户id不存在或者异常");
        }
        return menuService.selectByUser(user);
    }

    @PostMapping("getRelativeMenu")
    public ResponseInfo selectByRole(Role role) {
        return menuService.getRelativeMenu(role);
    }


}
