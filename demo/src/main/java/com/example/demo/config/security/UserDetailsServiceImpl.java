package com.example.demo.config.security;

import com.example.demo.entity.Role;
import com.example.demo.service.RoleService;
import com.example.demo.config.jwt.JwtUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private RoleService roleService;

    // 这里需要实现根据用户名从数据库获取用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Role> roleList = roleService.getRoleByUser(username);
        List<String> roles = new ArrayList<>();
        if (roleList != null && !roleList.isEmpty()){
                for (Role role:roleList){
                    roles.add("ROLE_"+role.getFlag().toUpperCase().trim());
                }
        }
        // 伪代码示例，实际应从数据库查询
        return new JwtUser(
                username, // userId
                roles
        );
    }
}