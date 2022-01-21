package org.zhaojd.component.common.resp;

import java.util.Collections;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author fywen
 * @version V1.0
 * @Description 封装的分页对象
 * @date 2021.11.13 16:56
 */
@ApiModel("封装的分页对象")
public class PageResult<T> {
    
    /**
     * 总数
     */
    @ApiModelProperty("记录总数")
    private long total = 0;
    
    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty("每页显示条数")
    private long pageSize = 10;
    
    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private long pageNo = 1;
    
    /**
     * 分页包含的数据列表
     */
    @ApiModelProperty("分页包含的数据列表")
    private List<T> records = Collections.emptyList();
    
    public PageResult() {
    }
    
    public PageResult(long pageNo, long pageSize, long total) {
        this.total = total;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public long getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
    
    public long getPageNo() {
        return pageNo;
    }
    
    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }
    
    public List<T> getRecords() {
        return records;
    }
    
    public void setRecords(List<T> records) {
        this.records = records;
    }
}
