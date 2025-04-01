package com.example.demo.dao;

import com.example.demo.entity.Warehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarehouseInterface {

    boolean add(Warehouse warehouse);

    List<Warehouse> show(Warehouse warehouse);

    List<Warehouse> getIdAndName();

    List<Warehouse> selectByUserAndRole(@Param("operator_id") String operatorId, @Param("mainRole") String mainRole);
}
