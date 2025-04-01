package com.example.demo.entity;

import com.example.demo.enums.StockIoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class StockIOAudit {
    private String audit_id;
    private String stock_io_id;
    private String auditor_id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date audit_time;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('APPROVED', 'REJECTED')")
    private StockIoStatus audit_status;
    private String audit_comment;
}
