package com.example.demo.service;

import com.example.demo.dao.WarehouseInterface;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.entity.Warehouse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WarehouseService {

    @Resource
    UserService userService;

    @Resource
    WarehouseInterface warehouseInterface;

    public ResponseInfo getWarehouseManagerList()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseManagerList",userService.getUserByRole("仓库管理员"));
        return ResponseInfo.success(map);
    }

    public ResponseInfo add(Warehouse warehouse)
    {
        warehouse.setWarehouse_id(UUID.randomUUID().toString().replace("-", ""));
        warehouse.setStatus(0);
        if(warehouseInterface.add(warehouse))
            return ResponseInfo.success("添加成功");
        else
            return ResponseInfo.error(400,"操作失败");
    }

    public ResponseInfo show(Warehouse warehouse)
    {

        Map<String,Object> map = new HashMap<>();
        map.put("warehouseList",warehouseInterface.show(warehouse));
        return ResponseInfo.success(map);
    }

    /**
     * 根据用户id和角色查询仓库完整信息
     * @param operatorId  用户id
     * @param mainRole 用户身份
     * @return 仓库列表
     */
    public List<Warehouse> selectByUserAndRole(String operatorId, String mainRole) {
        return warehouseInterface.selectByUserAndRole(operatorId,mainRole);
    }

    /**
     * 获取仓库id和仓库名
     */
    public List<Warehouse> getIdAndName() {
        return warehouseInterface.getIdAndName();
    }

    /**
     * 获取仓库id和仓库名
     */
    public List<Warehouse> getIdAndName(String operatorId) {
        return warehouseInterface.getIdAndName();
    }


}
