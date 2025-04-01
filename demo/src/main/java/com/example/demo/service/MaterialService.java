package com.example.demo.service;
import com.example.demo.dao.MaterialInterface;
import com.example.demo.entity.Category;
import com.example.demo.entity.Material;
import com.example.demo.entity.RequestInfo;
import com.example.demo.entity.ResponseInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MaterialService {



    @Resource
    CategoryService categoryService;

    @Resource
    MaterialInterface materialInterface;

    @Transactional
    public ResponseInfo show(Material material, RequestInfo input) {
        Map<String, Object> map = new HashMap<>();
        input.setStart((input.getCurrentPage()-1)*input.getPageSize());
        map.put("categoryList", categoryService.selectAll());
        map.put("materialList", materialInterface.show(material, input));
        map.put("count", materialInterface.show_count(material));
        return ResponseInfo.success(map);
    }

    public ResponseInfo add(Material material) {
        material.setMaterial_id(UUID.randomUUID().toString().replace("-", ""));
        material.setCreate_time(new Date());
        if (materialInterface.add(material))
            return ResponseInfo.success(material);
       return ResponseInfo.error(400, "操作失败");
    }

    public ResponseInfo delete(Material material) {
        //留空判断该物料是否被其他表关联


        if (materialInterface.delete(material))
            return ResponseInfo.success(material);
        return ResponseInfo.error(400, "操作失败");
    }

    public ResponseInfo update(Material material) {
        material.setUpdate_time(new Date());
        if (materialInterface.update(material))
            return ResponseInfo.success(material);
        return ResponseInfo.error(400, "操作失败");
    }

    @Transactional
    public ResponseInfo getDPMsg(Material material) {
        Map<String, Object> map = new HashMap<>();
        String first_category_id = null;
        List<Category> categoryList = categoryService.selectAll();
        if (!StringUtils.hasLength(material.getCategory_id())){
            first_category_id = categoryList.get(0).getId();
        }else {
            first_category_id = material.getCategory_id();
        }
        map.put("categoryList", categoryList);
        map.put("first_category_id",first_category_id);
        map.put("materialList", materialInterface.getDPMsg(material));
        return ResponseInfo.success(map);
    }
}
