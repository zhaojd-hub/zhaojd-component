package org.zhaojd.component.dao.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author jdzhao2
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T> {
    
    @Autowired
    protected M baseMapper;
    
    public BaseServiceImpl() {
    }
    
    @Override
    public M getBaseMapper() {
        return this.baseMapper;
    }
    
    // TODO 分页相关base 方法
}