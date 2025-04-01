package com.example.demo.dao;


import com.example.demo.entity.Menu;
import com.example.demo.entity.Menu_Role;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import java.util.List;

public interface MenuInterface {


    List<Menu> getRoleMenu(Role role);

    List<Menu> getAll();

    boolean add(Menu menu);

    boolean delete(Menu menu);

    List<Menu> selectByPid(Menu menu);

    boolean update(Menu menu);

    void clearRoleMenu(Role role);

    void clearRoleMenu2(Menu menu);

    void addRoleMenu(Menu_Role menu_role);

    List<Menu> getMenuByUser(User user);


}
