package com.example.demo.service;
import com.example.demo.config.redis.RedisUtil;
import com.example.demo.dao.RoleInterface;
import com.example.demo.dao.UserInterface;
import com.example.demo.entity.*;
import com.example.demo.filter.TokenFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Resource
    RedisUtil redisUtil;

    @Resource
    UserInterface userInterface;

    @Resource
    RoleInterface roleInterface;

    @Resource
            @Lazy
    RoleService roleService;



    public ResponseInfo login(User user) {
        User res=userInterface.select(user);
        if(res != null){
            Map<String, Object> map = new HashMap<>();
            map.put("token", TokenFilter.getToken(res.getId()));
            map.put("id",res.getId());
            map.put("name",res.getName());
            return ResponseInfo.success(map);
        }else {
            return ResponseInfo.error(400,"账号或密码错误");
        }
    }


    public ResponseInfo getUserInfo(String id) {

        User user = userInterface.selectById(id);
        if (user == null)
            return ResponseInfo.error(400,"用户不存在");

        return ResponseInfo.success(userInterface.selectById(id));
    }

    public ResponseInfo update(User user) {
        try {
            userInterface.update(user);
        }catch (DataIntegrityViolationException e){
            return ResponseInfo.error(400,"账号重复");
        }
        return ResponseInfo.success("修改成功");


    }


    public ResponseInfo show(User user, RequestInfo input) {

        Map<String,Object> map=new HashMap<>();
        input.setStart((input.getCurrentPage()-1)*input.getPageSize());
        List<User> userList = userInterface.search(user,input);
        map.put("list",userList);
        long count = userInterface.count(user);
        map.put("count",count);
        return ResponseInfo.success(map);
    }

    public ResponseInfo add(User user) {
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setPassword("123456");
        try {
            userInterface.add(user);
        }catch (DataIntegrityViolationException e){
            return ResponseInfo.error(400,"账号重复");
        }
        return ResponseInfo.success("添加成功");
    }

    @Transactional
    public ResponseInfo giveUserRole(RelativeParam param) {
        if (param.getId()==null)
            return ResponseInfo.error(400,"用户id异常");
        //管理员身份不能兼任其他角色
        if (param.getIdList().size()>1){
            Role role=new Role();
            role.setFlag("admin");
            List<Role> roleList = roleService.select(role);
            if (roleList.isEmpty())
                return ResponseInfo.error(400,"管理员的标识不为admin，请检查数据库相关数据");
            else{
                String adminId=roleList.get(0).getId();
                if (param.getIdList().contains(adminId))
                    return ResponseInfo.error(400,"管理员身份不能兼任其他角色");
            }
        }
        //清空用户角色
        User user=new User();
        user.setId(param.getId());
        roleInterface.cancelRole(user);
        for (String id:param.getIdList()){
            Role_User role_user = new Role_User();
            role_user.setId(UUID.randomUUID().toString().replace("-",""));
            role_user.setUser_id(param.getId());
            role_user.setRole_id(id);
            userInterface.addUserRole(role_user);
        }
        redisUtil.del("user_menu:"+param.getId());
        return ResponseInfo.success("添加成功");
    }

    //删除用户：删除用户关联的用户角色表，删除用户关联的订单表
    @Transactional
    public ResponseInfo delete(User user) {
        //判断身份
//        List<Role> roleList = roleService.getRoleByUser(user);
        //清空用户角色
        try {
            roleInterface.cancelRole(user);
            userInterface.delete(user);
        }catch (DataIntegrityViolationException e){
            return ResponseInfo.error(400,"删除异常");
        }
        //
        //欠你一个删除用户关联的订单表
        //
        return ResponseInfo.success("删除成功");
    }

    public ResponseInfo getUserPower(User user) {
        List<Role> roleList = roleService.getRoleByUser(user);
        boolean input=false,output=false;
        for (Role role:roleList){
            if ("purchase".equals(role.getFlag()))
                input=true;
            else if ("sale".equals(role.getFlag()))
                output=true;
            else if ("admin".equals(role.getFlag())) {
                input=output=false;
                break;
            }
        }
        Map<String,Boolean> map=new HashMap<>();
        map.put("input",input);
        map.put("output",output);
        return ResponseInfo.success(map);

    }

    public List<User> selectByRoleFlag(Role role) {
        return userInterface.selectByRoleFlag(role);
    }

    public List<User> selectByRole(Role role) {
        return userInterface.selectByRole(role);
    }

    public List<User> selectByMenu(Menu menu) {
        return userInterface.selectByMenu(menu);
    }

    public List<User> getUserByRole(String roleName) {

        return userInterface.getUserByRole(roleName);
    }
}
