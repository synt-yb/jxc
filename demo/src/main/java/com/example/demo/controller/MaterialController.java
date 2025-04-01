package com.example.demo.controller;

import com.example.demo.entity.Material;
import com.example.demo.entity.RequestInfo;
import com.example.demo.entity.ResponseInfo;
import com.example.demo.service.MaterialService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("material")
public class MaterialController {

    @Resource
    private MaterialService materialService;

    @PostMapping("add")
    public ResponseInfo add(Material material){
        //后端验证
        String check = material.check_add();
        if(check!=null)
            return ResponseInfo.error(400,check);

        return materialService.add(material);
    }

    @PostMapping("delete")
    public ResponseInfo delete(Material material){
        if (!StringUtils.hasLength(material.getMaterial_id()))
            return ResponseInfo.error(400,"id异常");
        return materialService.delete(material);
    }

    @PostMapping("update")
    public ResponseInfo update(Material material){
        String check = material.check_update();
        if(check!=null)
            return ResponseInfo.error(400,check);
        return materialService.update(material);


    }

    @PostMapping("show")
    public ResponseInfo show(Material material, RequestInfo input){
        return materialService.show(material,input);
    }

    @PostMapping("getDPMsg")
    public ResponseInfo getDPMsg(Material material){
        return materialService.getDPMsg(material);
    }
}
