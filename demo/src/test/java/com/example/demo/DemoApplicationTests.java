package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    UserController userController;
    @Resource
    UserController userController2;
    @Resource
    UserController userController3;
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        System.out.println(userController);
        System.out.println(userController2);
        System.out.println(userController3);
//    	User user = new User();
//        user.setUsername("theonefx");
//        user.setPassword("123456");
//        userController.login(user);
    }

}
