package com.example.demo.entity;

import com.example.demo.enums.StockIoStatus;
import com.example.demo.enums.StockIoSubType;
import com.example.demo.enums.StockIoType;
import com.example.demo.validation.orderSubmitGroups.orderSubmitGroup1;
import com.example.demo.validation.orderSubmitGroups.orderSubmitGroup2;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class StockIO {

    private String stock_io_id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('IN', 'OUT')")
    private StockIoType type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DP', 'TR', 'SE', 'DE')")
    private StockIoSubType sub_type;

    private String from_warehouse_id;

    private String to_warehouse_id;

    private String operator_id;

    @NotNull(message = "出入库时间不能为空", groups = orderSubmitGroup1.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date io_time;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DRAFT', 'PENDING', 'APPROVED', 'REJECTED')")
    private StockIoStatus status;

    private String auditor_id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date audit_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_time;

    private String remark;

    // 拓展页面显示字段
    private String from_warehouse_name;
    private String to_warehouse_name;
    private String operator_name;
    private String auditor_name;
    private BigDecimal total_price;

    // 拓展字段
    @Valid
    @Size(min = 1, message = "请至少选择一项入库商品", groups = orderSubmitGroup2.class)
    private List<StockIODetail> stockIODetail;

    // 构造函数
    public StockIO() {
        this.stockIODetail = new ArrayList<>();
    }

    /**
     * 前置：设置出入库的类型（type，sub_type）
     * 设置：设置审核单号和创建时间，设置审核状态为草稿，设置订单详情的id和订单id
     */
    public void setStockInOutDetail(){
        // 前置判断
        if (this.getType() == null||this.getSub_type()==null){
            return;
        }
        // 获得参数
        String type = this.getType().toString();
        String subType = this.getSub_type().toString();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(date);
        // 设置审核单号和时间
        if(this.getCreate_time()==null){
            this.setCreate_time(date);
            this.setStock_io_id(type + dateStr + subType +
                    UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        }else {
            this.setUpdate_time(date);
        }
        this.setStatus(StockIoStatus.DRAFT);
        // 设置订单详情信息
        if (!this.getStockIODetail().isEmpty()){
            for (StockIODetail stockIODetail : this.getStockIODetail()) {
                stockIODetail.setDetail_id(UUID.randomUUID().toString().replace("-", ""));
                stockIODetail.setStock_io_id(this.getStock_io_id());
            }
        }
    }

    /**
     * 判断当前用户是否有权限操作该类型
     * @param roles 用户角色列表
     * @return true：有权限，false：无权限
     */
    public String check_role(List<Role> roles){
        if (roles.isEmpty()){
            return null;
        }
        String judge = this.getType().toString().equals("IN")?"Purchase":"Sale";
        for (Role role : roles) {
            if (role.getFlag().equals(judge))
                return judge;
        }
        return null;
    }


}
