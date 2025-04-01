package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.service.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("add")
    public ResponseInfo add(Category category) {
        System.out.println(category);
        return categoryService.add(category);
    }
}
