package com.example.demo.service;

import com.example.demo.config.redis.RedisUtil;
import com.example.demo.dao.CategoryInterface;
import com.example.demo.entity.Category;
import com.example.demo.entity.ResponseInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Resource
    private CategoryInterface categoryInterface;

    @Resource
    private RedisUtil redisUtil;

    /**
     *  添加物料的类别
     * @param category 物料类别的信息
     * @return ResponseInfo
     */
    public ResponseInfo add(Category category) {
        String key = "categoryList";
        category.setId(UUID.randomUUID().toString().replace("-", ""));
        if(categoryInterface.add(category)) {
            redisUtil.del(key);
            return ResponseInfo.success("添加类别成功");
        }
        return ResponseInfo.error(400, "添加类别失败");
    }

    /**
     * 查询所有类别
     * @return ResponseInfo
     */
    public List<Category> selectAll() {
        List<Category> categoryList;
        String key = "categoryList";
        if (redisUtil.hasKey(key)) {
            return (List<Category>) redisUtil.get(key);
        }else{
            categoryList = categoryInterface.selectAll();
            redisUtil.set(key, categoryList);
        }
        return categoryInterface.selectAll();
    }
}
