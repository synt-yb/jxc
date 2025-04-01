package com.example.demo.dao;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInterface {

    boolean add(User user);

    boolean update(User user);

    List<User> search(@Param("user")User user, @Param("input")RequestInfo input);

    List<User> selectByRole(Role role);

    User select(User user);

    User selectById(String id);


    long count(User user);


    void addUserRole(Role_User role_user);


    void delete(User user);

    List<User> selectByRoleFlag(Role role);

    List<User> selectByMenu(Menu menu);

    List<User> getUserByRole(String roleName);
}
