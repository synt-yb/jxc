package com.example.demo.dao;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface RoleInterface {


    List<Role> getRoleByUser(User user);

    List<Role> getAll();

    boolean add(Role role) ;

    void update(Role role);

    boolean delete(Role role);

    List<Role> select(Role role);//两种情况：1.传入身份的名称模糊查询，返回集合2.传入用户标识，返回角色对象(集合只取下标0)

    void cancelRole(User user);


    void cancelRole2(Role role);
}
