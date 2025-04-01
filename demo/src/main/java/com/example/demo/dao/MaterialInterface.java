package com.example.demo.dao;

import com.example.demo.entity.Material;
import com.example.demo.entity.RequestInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialInterface {
    boolean add(Material material);
    List<Material> show(@Param("material") Material material, @Param("input") RequestInfo input);

    Long show_count(Material material);

    boolean delete(Material material);

    boolean update(Material material);

    List<Material> getDPMsg(Material material);
}
