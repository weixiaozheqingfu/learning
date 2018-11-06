package com.pagehelper.plugin;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 * @author limengjun
 * @param <T>
 */
public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = -4647032854428993236L;

	/** 每页记录数 */
	private int pageSize;
	/** 页码 */
	private int pageNum;
	/** 总记录数 */
	private int totalRecords;
	/** 总页数*/
	private int totalPages;
	/** 结果集 */
	private List<T> data;

	public PageInfo(List<T> data){
		Page page = PageContext.getPage();
		if(null != page){
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.totalRecords = page.getTotalRecords();
			this.totalPages = page.getTotalPages();
		}
		this.data = data;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<T> getData() {
		return data;
	}

}