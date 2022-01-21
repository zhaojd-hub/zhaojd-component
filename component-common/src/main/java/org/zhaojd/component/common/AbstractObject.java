package org.zhaojd.component.common;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

/**
 * @author jdzhao2
 * @version V1.0
 * @Description 抽象父类对象
 * @date 2021.11.19 15:09
 */
public class AbstractObject {
    
    /**
     * 浅度克隆
     *
     * @param target target
     * @return 处理结果
     * @throws BeansException bean拷贝异常
     */
    public <T> T clone(T target) throws Exception {
        BeanUtils.copyProperties(this, target);
        return target;
    }
    
    /**
     * 浅度克隆
     *
     * @param clazz target class
     * @return Target
     * @throws BeansException bean拷贝异常
     */
    public <T> T clone(Class<T> clazz) throws Exception {
        T target = clazz.newInstance();
        BeanUtils.copyProperties(this, target);
        return target;
    }
}