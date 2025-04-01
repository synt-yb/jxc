package com.example.demo.dao;


import com.example.demo.entity.RequestInfo;
import com.example.demo.entity.StockIO;
import com.example.demo.entity.StockIOAudit;
import com.example.demo.entity.StockIODetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StockIOInterface {

    boolean insertStockIO(StockIO stockIO);

    boolean insertStockIODetail(StockIO stockIO);

    boolean deleteStockIO(StockIO stockIO);

    boolean deleteStockIODetail(StockIO stockIO);

    List<StockIO> show(@Param("stockIO") StockIO stockIO, @Param("input") RequestInfo input);

    Long show_count(StockIO stockIO);

    boolean submit(StockIO stockIO);

    StockIO selectStockIOById(StockIO stockIO);

    List<StockIODetail> selectStockIODetailById(StockIO stockIO);

    boolean update(StockIO stockIO);

    List<StockIO> auditShow(@Param("stockIO") StockIO stockIO, @Param("input") RequestInfo input);

    Long auditShow_count(StockIO stockIO);

    StockIO getAuditMsg(StockIO stockIO);

    List<StockIODetail> getAuditDetailMsg(StockIO stockIO);

    boolean submitForAuditAdd(StockIOAudit stockIOAudit);

    boolean submitForAuditUpdate(StockIOAudit stockIOAudit);

    boolean removeStockIOAudit(StockIO stockIO);

    String getWarehouseIDByStockIOId(String stockIOId);
}
