package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

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
}
