package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Warehouse {
    private String warehouse_id;
    private String warehouse_name;
    private String location;
    private String manager_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date create_time;
    private int status;
    private BigDecimal capacity;
}
