package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.StockIOService;
import com.example.demo.validation.orderSubmitGroup;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("stockIO")
public class StockIOController {

    @Resource
    private StockIOService stockIOService;

    @PostMapping("addDpIO")
    public ResponseInfo addDpIO(@Validated(orderSubmitGroup.class) @RequestBody StockIO stockIO) {
        return stockIOService.addDpIO(stockIO);
    }

    @PostMapping("delete")
    public ResponseInfo delete(StockIO stockIO) {
        return stockIOService.delete(stockIO);
    }

    @PostMapping("submit")
    public ResponseInfo submit(StockIO stockIO) {
        return stockIOService.submit(stockIO);
    }
    @PostMapping("update")
    public ResponseInfo update(@RequestBody StockIO stockIO) {
        if (stockIO.getStockIODetail().isEmpty())
            return ResponseInfo.error(400,"请填写订单详情");
        if(stockIO.getIo_time()==null)
            return ResponseInfo.error(400,"请填写出入库时间");
        return stockIOService.update(stockIO);
    }
    @PostMapping("show")
    public ResponseInfo show(String operator_id,StockIO stockIO,RequestInfo input) {
        return stockIOService.show(operator_id,stockIO,input);
    }

    @PostMapping("auditShow")
    public ResponseInfo auditShow(StockIO stockIO,RequestInfo input) {
        return stockIOService.auditShow(stockIO,input);
    }

    @PostMapping("getUpdMsg")
    public ResponseInfo getUpdMsg(StockIO stockIO) {
        return stockIOService.getUpdMsg(stockIO);
    }

    @PostMapping("getAuditMsg")
    public ResponseInfo getAuditMsg(StockIO stockIO) {
        return stockIOService.getAuditMsg(stockIO);
    }

    @PostMapping("submitForAudit")
    public ResponseInfo submitForAudit(StockIOAudit stockIOAudit) {
        // 后端审核
        if (!StringUtils.hasLength(stockIOAudit.getStock_io_id())||!StringUtils.hasLength(stockIOAudit.getAuditor_id())||
                !StringUtils.hasLength(stockIOAudit.getAudit_status().toString())){
            return ResponseInfo.error(400,"信息不完整");
        }
        return stockIOService.submitForAudit(stockIOAudit);
    }
}
