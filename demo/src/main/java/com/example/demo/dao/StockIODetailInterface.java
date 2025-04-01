package com.example.demo.dao;

import com.example.demo.entity.StockIODetail;

import java.util.List;

public interface StockIODetailInterface {

    List<StockIODetail> selectByStockIOId(String stock_io_id);
}
