package com.glitter.bean;

import lombok.Data;

import java.util.List;

@Data
public abstract class Page<T> {

    private int pageNum ;

    private int pageSize ;

    private int totalCount ;

    private List<T> data;

    private TotalData totalData;

}
