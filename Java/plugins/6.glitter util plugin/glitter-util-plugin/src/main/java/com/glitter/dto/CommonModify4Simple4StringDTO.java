package com.glitter.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommonModify4Simple4StringDTO implements Serializable{
    /** 待删除的id集合 */
    private List<String> deleteIds;
    /** 待增加的id集合 */
    private List<String> insertIds;
}