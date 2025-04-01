package com.example.demo.service;

import com.example.demo.dao.InventoryInterface;
import com.example.demo.entity.*;
import com.example.demo.enums.RoleFlag;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class InventoryService {

    @Resource
    InventoryInterface inventoryInterface;

    @Resource
    RoleService roleService;

    @Resource
    WarehouseService warehouseService;

    @Resource
    CategoryService categoryService;

    /**
     * 添加库存
     *
     * @param inventoryList 库存信息
     * @return 返回true表示添加成功，false表示添加失败
     */
    public boolean add(List<Inventory> inventoryList) {
        return inventoryInterface.add(inventoryList);
    }


    /**
     * 根据库存详情列表获取库存列表
     * @param stockIODetailList 库存详情列表
     * @param warehouse_id 仓库id
     * @return 库存列表
     */
    public List<Inventory> getInventoryListByStockIODetailList(List<StockIODetail> stockIODetailList, String warehouse_id) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (StockIODetail stockIODetail : stockIODetailList) {
            Inventory inventory = new Inventory();
            inventory.setInventory_id(UUID.randomUUID().toString().replace("-", ""));
            inventory.setWarehouse_id(warehouse_id);
            inventory.setMaterial_id(stockIODetail.getMaterial_id());
            inventory.setQuantity(stockIODetail.getQuantity());
            inventory.setPurchase_price(stockIODetail.getUnit_price());
            inventory.setProduction_date(stockIODetail.getProduction_date());
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    /**
     * 显示页面《库存汇总》的页面信息:角色判断
     * @param inventory 条件查询
     * @param user_id 用户id，用于权限判定
     * @return 《库存汇总》的页面信息
     */
    public ResponseInfo show(Inventory inventory,String user_id) {
        // 获取用户角色列表
        List<Role> roleList = roleService.getRoleByUser(user_id);
        String roleFlag = roleService.roleOfInventory(roleList);
        // 非经理，仓管，销售，采购无权限
        if (roleFlag==null) {
            return ResponseInfo.error(400, "权限不足");
        }
        //经理，仓管：全部仓库信息 采购，销售：所属仓库信息
        List<Warehouse> warehouseList;
        if (roleFlag.equals(RoleFlag.Warehouse_Manager.toString())||roleFlag.equals(RoleFlag.Manager.toString())){
            warehouseList = warehouseService.getIdAndName();
        }else{
            warehouseList = warehouseService.getIdAndName(user_id);
        }
        // 是否初次进入页面，设置查询仓库id
        if (!StringUtils.hasLength(inventory.getWarehouse_id())){
            inventory.setWarehouse_id(warehouseList.get(0).getWarehouse_id());
        }
        // 库存查询和分类列表查询
        List<Inventory> inventoryList = inventoryInterface.show(inventory);
        List<Category> categoryList = categoryService.selectAll();
        // 返回页面信息: 仓库列表，分类列表，库存列表，仓库id
        Map<String, Object> map = new HashMap<>();
        map.put("warehouseList", warehouseList);
        map.put("categoryList", categoryList);
        map.put("inventoryList", inventoryList);
        map.put("warehouse_id", inventory.getWarehouse_id());
        return ResponseInfo.success(map);
    }

    public ResponseInfo pdf(Inventory inventory){
        // 创建PDF文档对象
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            // 写入仓库ID到PDF
            System.out.println(inventory);
            document.add(new Paragraph("仓库ID: " + inventory.getWarehouse_id()));
            document.close();
        } catch (DocumentException e) {
            return ResponseInfo.error(400, "生成PDF时发生错误");
        }
        // 将PDF内容封装到ResponseInfo中返回，这里假设ResponseInfo支持设置二进制数据
        Map<String, Object> map = new HashMap<>();
        map.put("pdfContent", byteArrayOutputStream.toByteArray());
        return ResponseInfo.success(map);
    }
}
