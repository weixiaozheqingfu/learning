package cn.huimin100.erp.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommonModifyDTO<V> implements Serializable{
    /** 待删除的id集合 */
    private List<Integer> deleteIds ;
    /** 更新的记录集合 */
    private List<V> updateRecords;
    /** 插入的记录集合 */
    private List<V> insertRecords;
}