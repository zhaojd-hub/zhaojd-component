package org.zhaojd.component.cahce.single.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zhaojd.component.cahce.dto.BizObj;


/**
 * @author jdzhao2
 * @version V1.0
 * @Description 业务Service
 * @date 2021.11.18 10:47
 */
@Service
public class BizService {
    
    @Cacheable(cacheManager = "cacheManager", cacheNames = {"default-cache"}, key = "#id")
    public BizObj findObjById(int id) {
        System.out.println("通过数据库查询...");
        BizObj obj = new BizObj();
        obj.setBizName("测试缓存注解");
        obj.setDesc("测试缓存注解描述");
        obj.setId(100);
        
        return obj;
    }
}
