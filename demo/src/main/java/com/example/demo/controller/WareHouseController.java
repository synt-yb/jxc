package com.example.demo.controller;

import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.Warehouse;
import com.example.demo.service.WarehouseService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("warehouse")
public class WareHouseController {

    @Resource
    private WarehouseService warehouseService;

    @PostMapping("getWarehouseManagerList")
    public ResponseInfo getWarehouseManagerList()
    {

        return warehouseService.getWarehouseManagerList();
    }

    @PostMapping("show")
    public ResponseInfo show(Warehouse warehouse){
        System.out.println(warehouse.getWarehouse_name());
        return warehouseService.show(warehouse);
    }

    @PostMapping("add")
    public ResponseInfo add(Warehouse warehouse){
        System.out.println(warehouse.getCapacity());
        if (!StringUtils.hasLength(warehouse.getWarehouse_name())){
            return ResponseInfo.error(400,"仓库名称不能为空");
        }
        if (!StringUtils.hasLength(warehouse.getLocation())){
            return ResponseInfo.error(400,"仓库地址不能为空");
        }
        if (!StringUtils.hasLength(warehouse.getManager_id())){
            return ResponseInfo.error(400,"请选择负责人");
        }
        if (warehouse.getCapacity()==null||!(warehouse.getCapacity().compareTo(BigDecimal.ZERO)>=0)){
            return ResponseInfo.error(400,"请输入正确容量");
        }
        if (warehouse.getCreate_time()==null){
            return ResponseInfo.error(400,"请输入创建时间");
        }
        return warehouseService.add(warehouse);
    }
}
