package org.zhaojd.component.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

/**
 * 转换工具
 */
public class MappingUtils {
    
    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    public static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();
    
    /**
     * List  实体类 转换器
     *
     * @param source 原数据
     * @param clz    转换类型
     */
    public static <T, S> List<T> convertor(List<S> source, Class<T> clz) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T> map = new ArrayList<>();
        for (S s : source) {
            map.add(MAPPER.map(s, clz));
        }
        return map;
    }
    
    /**
     * List  实体类 转换器
     *
     * @param source 原数据
     * @param clz    转换类型
     */
    public static <T, K, V> List<T> convertor(Map<K, V> source, Class<T> clz) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> map = new ArrayList<>();
        try {
            for (K k : source.keySet()) {
                V v = source.get(k);
                T t = clz.newInstance();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == k.getClass()) {
                        field.setAccessible(true);
                        field.set(t, k);
                    }
                    if (field.getType() == v.getClass()) {
                        field.setAccessible(true);
                        field.set(t, v);
                    }
                }
                map.add(t);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    /**
     * Set 实体类 深度转换器
     *
     * @param source 原数据
     * @param clz    目标对象
     */
    public static <T, S> Set<T> convertor(Set<S> source, Class<T> clz) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptySet();
        }
        Set<T> set = new HashSet<>();
        for (S s : source) {
            set.add(MAPPER.map(s, clz));
        }
        return set;
    }
    
    /**
     * 实体类 深度转换器
     */
    public static <T, S> T convertor(S source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        return MAPPER.map(source, clz);
    }
    
    public static void convertor(Object source, Object object) {
        MAPPER.map(source, object);
    }
    
    public static <T> void copyConvertor(T source, Object object) {
        MAPPER.map(source, object);
    }
}
