package cn.huimin100.erp.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class Modify4Simple4StringDTO implements Serializable{
    /** 待删除的id集合 */
    private List<String> deleteIds;
    /** 待增加的id集合 */
    private List<String> insertIds;
}