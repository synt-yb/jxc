package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockIODetail {
    private String detail_id;
    private String stock_io_id;
    private String material_id;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date production_date;
    private BigDecimal unit_price;
    private int quantity;
    private String remark;

    // 拓展字段
    private String material_name;
    private String category_name;
    private String volume_per_unit;
    private String shelf_life;


}
