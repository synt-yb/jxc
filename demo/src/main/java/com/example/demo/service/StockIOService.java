package com.example.demo.service;

import com.example.demo.dao.StockIODetailInterface;
import com.example.demo.dao.StockIOInterface;
import com.example.demo.entity.*;
import com.example.demo.enums.StockIoStatus;
import com.example.demo.enums.StockIoSubType;
import com.example.demo.enums.StockIoType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.*;

@Service
public class StockIOService {

    @Resource
    RoleService roleService;

    @Resource
    WarehouseService warehouseService;

    @Resource
    InventoryService inventoryService;

    @Resource
    StockIOInterface stockIOInterface;

    @Resource
    StockIODetailInterface stockIODetailInterface;


    @Transactional
    public ResponseInfo addDpIO(StockIO stockIO) {
        stockIO.setType(StockIoType.IN);
        stockIO.setSub_type(StockIoSubType.DP);
        stockIO.setStockInOutDetail();
        stockIOInterface.insertStockIO(stockIO);
        stockIOInterface.insertStockIODetail(stockIO);
        return ResponseInfo.success(null);
    }

    @Transactional
    public ResponseInfo show(String operator_id,StockIO stockIO, RequestInfo input) {
        // 页面
        input.setStart((input.getCurrentPage()-1)*input.getPageSize());
        // 判断身份: 采购或销售  --> 所有的仓库
        List<Role> roleList = roleService.getRoleByUser(operator_id);
        String mainRole=stockIO.check_role(roleList);
        if(mainRole==null)
            return ResponseInfo.error(400,"您没有权限");
        List<Warehouse> warehouseList = warehouseService.selectByUserAndRole(operator_id,mainRole);
        if (warehouseList.isEmpty()){
            return ResponseInfo.success("您没有关联任何仓库");
        }
        // 是否初次进入页面
        if (stockIO.getTo_warehouse_id()==null){
            stockIO.setTo_warehouse_id(warehouseList.get(0).getWarehouse_id());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("stockIOList",stockIOInterface.show(stockIO, input));
        map.put("count",stockIOInterface.show_count(stockIO));
        map.put("warehouseList",warehouseList);
        map.put("warehouse_id",stockIO.getTo_warehouse_id());
        return ResponseInfo.success(map);
    }

    /**
     * 删除订单表---->删除订单详情表--->删除审核表信息
     * @param stockIO 订单id
     */
    @Transactional
    public ResponseInfo delete(StockIO stockIO) {
        stockIOInterface.deleteStockIODetail(stockIO);
        stockIOInterface.removeStockIOAudit(stockIO);
        stockIOInterface.deleteStockIO(stockIO);
        return ResponseInfo.success("删除成功");
    }

    /**
     * 提交订单-->状态草稿改为待审核
     * @param stockIO 订单id
     */
    public ResponseInfo submit(StockIO stockIO) {
        stockIO.setStatus(StockIoStatus.PENDING);
        if(stockIOInterface.submit(stockIO))
            return ResponseInfo.success("提交成功");
        return ResponseInfo.error(400,"操作失败");
    }

    /**
     * 获得要修改的订单信息
     * @param stockIO 订单id
     * @return 订单信息
     */
    public ResponseInfo getUpdMsg(StockIO stockIO) {
        Map<String,Object> map = new HashMap<>();
        map.put("stockIO",stockIOInterface.selectStockIOById(stockIO));
        map.put("stockIODetail",stockIOInterface.selectStockIODetailById(stockIO));
        return ResponseInfo.success(map);
    }

    /**
     * 修改订单-->状态改为草稿--->删除审核表信息
     * @param stockIO 修改后的订单信息
     * @return 修改结果
     */
    @Transactional
    public ResponseInfo update(StockIO stockIO) {
        stockIO.setStockInOutDetail();
        stockIOInterface.update(stockIO);
        stockIOInterface.deleteStockIODetail(stockIO);
        System.out.println(stockIO);
        stockIOInterface.insertStockIODetail(stockIO);
        stockIOInterface.removeStockIOAudit(stockIO);
        return ResponseInfo.success(null);
    }

    /**
     * 获得审核的页面的初始信息
     * @param stockIO 用于条件查询
     * @param input 分页信息
     * @return 审核的页面的初始信息
     */
    public ResponseInfo auditShow(StockIO stockIO, RequestInfo input) {
        // 页面
        input.setStart((input.getCurrentPage()-1)*input.getPageSize());
        // 判断身份: 采购或销售  --> 所有的仓库
        List<Warehouse> warehouseList = warehouseService.getIdAndName();
        if (warehouseList.isEmpty()){
            return ResponseInfo.success("仓库信息出现问题");
        }
        // 是否初次进入页面
        if (!StringUtils.hasLength(stockIO.getTo_warehouse_id())){
            stockIO.setTo_warehouse_id(warehouseList.get(0).getWarehouse_id());
        }
        stockIO.setStatus(StockIoStatus.PENDING);
        Map<String,Object> map = new HashMap<>();
        map.put("stockIOList",stockIOInterface.auditShow(stockIO, input));
        map.put("count",stockIOInterface.auditShow_count(stockIO));
        map.put("warehouseList",warehouseList);
        map.put("warehouse_id",stockIO.getTo_warehouse_id());
        return ResponseInfo.success(map);
    }

    /**
     * 获得审核订单信息
     * @param stockIO 订单id
     * @return 审核订单信息
     */
    public ResponseInfo getAuditMsg(StockIO stockIO) {
        Map<String,Object> map = new HashMap<>();
        map.put("stockIO",stockIOInterface.getAuditMsg(stockIO));
        map.put("stockIODetail",stockIOInterface.getAuditDetailMsg(stockIO));
        return ResponseInfo.success(map);
    }

    /**
     * 生成审核id，审核时间 --》如果审核通过更新库存表
     * @param stockIOAudit 订单id，审核人员id，审核状态，审核意见
     * @return 审核结果
     */
    @Transactional
    public ResponseInfo submitForAudit(StockIOAudit stockIOAudit) {
        // 生成审核id，审核时间
        stockIOAudit.setAudit_time(new Date());
        stockIOAudit.setAudit_id(UUID.randomUUID().toString().replace("-", ""));
        // 插入审核表内容，更新订单表状态
        stockIOInterface.submitForAuditAdd(stockIOAudit);
        stockIOInterface.submitForAuditUpdate(stockIOAudit);
        // 审核通过更新库存表
        if (stockIOAudit.getAudit_status()==StockIoStatus.APPROVED){
            // 更新库存表: 获得仓库id，获得订单详情，插入库存表
            List<StockIODetail> stockIODetailList = stockIODetailInterface.selectByStockIOId(stockIOAudit.getStock_io_id());
            String warehouse_id = stockIOInterface.getWarehouseIDByStockIOId(stockIOAudit.getStock_io_id());
            List<Inventory> inventoryList = inventoryService.getInventoryListByStockIODetailList(stockIODetailList,warehouse_id);
            inventoryService.add(inventoryList);
        }
        return ResponseInfo.success("审核成功");
    }


}
