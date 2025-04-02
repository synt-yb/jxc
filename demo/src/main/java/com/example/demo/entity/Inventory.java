package com.example.demo.entity;

import com.example.demo.until.DateUntils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class Inventory {
    private String inventory_id;
    private String warehouse_id;
    private String material_id;
    private int quantity;
    private BigDecimal purchase_price;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date production_date;

    // 非数据库字段
    private String material_name;
    private String warehouse_name;
    private String category_id;
    private String category_name;
    private String volume_per_unit;
    private Integer shelf_life;

    public List<String> getAllInventoryInfo()
    {
        return new ArrayList<>(Arrays.asList(
                getMaterial_name(),
                getCategory_name(),
                getVolume_per_unit(),
                getPurchase_price().toString(),
                String.valueOf(getQuantity()),
                DateUntils.formatDate(getProduction_date(), "yyyy-MM-dd hh:mm:ss"),
                String.valueOf(getShelf_life()))
        );
    }
    public List<String> getPartInventoryInfo()
    {
        return new ArrayList<>(Arrays.asList(
                getPurchase_price().toString(),
                String.valueOf(getQuantity()),
                DateUntils.formatDate(getProduction_date(), "yyyy-MM-dd hh:mm:ss"),
                String.valueOf(getShelf_life()))
        );
    }
}
