package com.example.demo.dao;

import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryInterface {

    boolean add(Category category);

    List<Category> selectAll();
}
