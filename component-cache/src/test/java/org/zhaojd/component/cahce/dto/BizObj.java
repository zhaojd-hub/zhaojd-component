package org.zhaojd.component.cahce.dto;

import java.io.Serializable;

/**
 * @author jdzhao2
 * @version V1.0
 * @Description 测试对象
 * @date 2021.11.18 10:37
 */
public class BizObj implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String bizName;
    private String desc;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getBizName() {
        return bizName;
    }
    
    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
}