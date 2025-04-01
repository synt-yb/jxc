package com.example.demo.service;

import com.example.demo.config.redis.RedisUtil;
import com.example.demo.dao.MenuInterface;
import com.example.demo.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MenuService {


    @Resource
    RedisUtil redisUtil;

    @Resource
    MenuInterface menuInterface;

    @Resource
    UserService userService;


    public ResponseInfo add(Menu menu) {

        String prefix = menu.getPid()==null?"I_":"II_";
        menu.setId(prefix+ UUID.randomUUID().toString().substring(0,4));
        if(menuInterface.add(menu)){
            List<Menu> menuList = menuInterface.getAll();
            return ResponseInfo.success(menuList);
        }else{
            return ResponseInfo.error(400,"菜单添加失败");
        }
    }

    @Transactional
    public ResponseInfo delete(Menu menu) {
        //菜单删除  --》选判断能否删除（存在子菜单不准许删除）
        List<Menu> menuList = menuInterface.selectByPid(menu);
        if(!menuList.isEmpty())
            return ResponseInfo.error(400,"该菜单下存在子菜单，无法删除");
        // *删除相关关联角色下的该菜单
        menuInterface.clearRoleMenu2(menu);
        // 删除对应的用户缓存
        List<User> userList = userService.selectByMenu(menu);
        for (User user : userList){
            redisUtil.del("user_menu:"+user.getId());
        }
        //判断是否删除成功，成功则返回删除后的菜单列表
        if(menuInterface.delete(menu)){
            menuList = menuInterface.getAll();
            return ResponseInfo.success(menuList);
        }else{
            return ResponseInfo.error(400,"菜单删除失败");
        }
    }

    @Transactional
    public ResponseInfo update(Menu menu) {
        List<User> userList = userService.selectByMenu(menu);
        for (User user : userList){
            redisUtil.del("user_menu:"+user.getId());
        }
        if(menuInterface.update(menu)){
            List<Menu> menuList = menuInterface.getAll();
            return ResponseInfo.success(menuList);
        }else{
            return ResponseInfo.error(400,"菜单修改失败");
        }
    }

    /**
     * 查询所有菜单
     */
    public ResponseInfo getAll() {

        List<Menu> menuList = menuInterface.getAll();
        if (menuList.isEmpty())
            return ResponseInfo.error(400,"查找菜单出了问题");
        else
            return ResponseInfo.success(menuList);
    }

    /**
     * 通过用户=>查询菜单
     */
    public ResponseInfo selectByUser(User user) {

        ResponseInfo res ;
        try {
            String key = "user_menu:" + user.getId();
            if (redisUtil.hasKey(key))
                return ResponseInfo.success(redisUtil.get(key));
            // redis 不存在,查询数据库用户的角色
            List<Menu> menuList = menuInterface.getMenuByUser(user);
            menuList = Menu.getTypeTree(menuList);
            res = ResponseInfo.success(menuList);
            redisUtil.set(key, menuList);
        }catch (Exception e){
            List<Menu> menuList = menuInterface.getMenuByUser(user);
            menuList = Menu.getTypeTree(menuList);
            res = ResponseInfo.success(menuList);
        }
        return res;

    }


    @Transactional
    public ResponseInfo getRelativeMenu(Role role) {
        Map<String,Object> map = new HashMap<>();
        List<Menu> menuList = menuInterface.getRoleMenu(role);
        List<Menu> allMenu = menuInterface.getAll();
        map.put("roleList",menuList);
        map.put("allMenu",allMenu);
        return ResponseInfo.success(map);

    }
    @Transactional
    public ResponseInfo updateRoleMenu(RelativeParam param) {
        Role role = new Role();
        role.setId(param.getId());

        // 先清空
        menuInterface.clearRoleMenu(role);
        param.getIdList().forEach(menuId->{
            Menu_Role menu_role = new Menu_Role();
            menu_role.setId(UUID.randomUUID().toString().replace("-",""));
            menu_role.setMenu_id(menuId);
            menu_role.setRole_id(role.getId());
            menuInterface.addRoleMenu(menu_role);
        });
        List<User> userList = userService.selectByRole(role);
        for (User user : userList){
            redisUtil.del("user_menu:"+user.getId());
        }
        return getAll();
    }
}
