package com.example.demo.dao;

import com.example.demo.entity.Inventory;

import java.util.List;

public interface InventoryInterface {

    boolean add(List<Inventory> inventoryList);

    List<Inventory> show(Inventory inventory);
}
