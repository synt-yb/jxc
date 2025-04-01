package com.example.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class RequestInfo {
    Integer currentPage;
    Integer pageSize;
    Integer start;

}
