package com.example.demo.service;

import com.example.demo.dao.MenuInterface;
import com.example.demo.dao.RoleInterface;
import com.example.demo.entity.Menu;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleFlag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService {

    @Resource
    RoleInterface roleInterface;

    @Resource
    MenuInterface menuInterface;


    private final List<String> whiteList = Arrays.asList("42765769671347afb202ff7495a128d8", "7ab9a9b9c6a646649d953ca040798ed6", "ffb91c218b664f328a2b10d1fb3a4778");


    @Transactional
    public ResponseInfo add(Role role) {
        role.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            roleInterface.add(role);
        } catch (DataIntegrityViolationException e) {
            return ResponseInfo.error(400, "角色标识重复");
        }
        return show();
    }

    @Transactional
    public ResponseInfo delete(Role role) {
        // 过滤白名单
        if (whiteList.contains(role.getId()))
            return ResponseInfo.error(400, "该角色为系统默认角色，无法删除");
        // 查询是否关联菜单
        List<Menu> menuList = menuInterface.getRoleMenu(role);
        if (!menuList.isEmpty())
            return ResponseInfo.error(400, "该角色下存在菜单，请先解绑菜单");
        // 删除角色，且删除用户绑定的角色
        roleInterface.cancelRole2(role);
        if (roleInterface.delete(role))
            return show();
        else
            return ResponseInfo.error(400, "角色删除失败");
    }

    @Transactional
    public ResponseInfo update(Role role) {
        try {
            roleInterface.update(role);
        } catch (DataIntegrityViolationException e) {
            return ResponseInfo.error(400, "角色标识重复");
        }
        return show();
    }


    public ResponseInfo show() {
        List<Role> roleList = roleInterface.getAll();
        if (roleList.isEmpty())
            return ResponseInfo.error(400, "角色信息获取失败");
        else
            return ResponseInfo.success(roleList);
    }


    public List<Role> select(Role role) {
        return roleInterface.select(role);
    }

    public ResponseInfo getUserRelativeRole(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("userRole", roleInterface.getRoleByUser(user));
        map.put("roleList", roleInterface.getAll());
        return ResponseInfo.success(map);
    }

    public List<Role> getRoleByUser(User user) {
        return roleInterface.getRoleByUser(user);
    }

    public List<Role> getRoleByUser(String id) {
        User user = new User();
        user.setId(id);
        return roleInterface.getRoleByUser(user);
    }

    /**
     * 判断用户是否具有查看库存的权限
     * @param roles 用户角色列表
     * @return 返回角色标识，若为空则没有权限
     */
    public String roleOfInventory(List<Role> roles) {
        String roleFlag = null;
        if (roles.isEmpty())
            return null;
        for (Role role : roles) {
            if (role.getFlag().equals(RoleFlag.Manager.toString()))
                return role.getFlag();
            if (role.getFlag().equals(RoleFlag.Warehouse_Manager.toString()))
                return role.getFlag();
            if (role.getFlag().equals(RoleFlag.Sale.toString()) || role.getFlag().equals(RoleFlag.Purchase.toString()))
                roleFlag = role.getFlag();
        }
        return roleFlag;
    }

}
