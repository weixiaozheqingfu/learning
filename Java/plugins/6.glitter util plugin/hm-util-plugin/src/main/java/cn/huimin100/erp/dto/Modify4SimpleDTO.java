package cn.huimin100.erp.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Modify4SimpleDTO implements Serializable{
    /** 待删除的id集合 */
    private List<Integer> deleteIds ;
    /** 待增加的id集合 */
    private List<Integer> insertIds ;
}