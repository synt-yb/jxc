package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Material {
    // 数据表字段
    private String material_id;
    private String material_name;
    private String category_id;
    private int min_stock;
    private int shelf_life;
    private BigDecimal box_volume;
    private int box_quantity;
    private String unit;
    private String remark;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_time;
    private String administrator_id;

    // 扩展字段
    private String category_name;
    private String administrator_name;
    private String volume_per_unit;  //规格显示

    // 添加的后端验证
    public String check_add(){
        if(!StringUtils.hasLength(material_name)){
            return "请输入物料名称";
        }
        if(!StringUtils.hasLength(category_id)){
            return "请选择物料分类";
        }
        if(!StringUtils.hasLength(unit)){
            return "请输入物料单位";
        }
        if(min_stock<=0){
            return "请输入正确的最小库存";
        }
        if(shelf_life<=0){
            return "请输入正确的保质期";
        }
        if(box_quantity<=0){
            return "请输入正确的每箱件数";
        }
        if(box_volume==null||box_volume.compareTo(BigDecimal.ZERO)<=0){
            return "请输入正确的单位体积";
        }
        if(!StringUtils.hasLength(administrator_id)){
            return "管理员id错误";
        }
        return null;
    }

    public String check_update(){
        String res = check_add();
        if(res!=null)
            return res;
        if(!StringUtils.hasLength(material_id)){
            return "物料id错误";
        }
        if(create_time==null){
            return "创建时间错误";
        }
        return null;

    }




}
