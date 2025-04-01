package com.example.demo.controller;
import com.example.demo.entity.Inventory;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("inventory")
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("show")
    public ResponseInfo show(Inventory inventory, String user_id)
    {
        return inventoryService.show(inventory,user_id);
    }

    @PostMapping("pdf")
    public ResponseInfo pdf(Inventory inventory)
    {
        System.out.println(inventory.getWarehouse_id());//warehouse_id
        return inventoryService.pdf(inventory);
    }

}
