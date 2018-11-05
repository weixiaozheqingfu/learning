package com.glitter.spring.boot.plugin.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页运算类
 * @author limengjun
 * @param <T>
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = -6069386888143600090L;
	
	/** 默认页码 */
	private static int DEFAULT_PAGE_NUM = 1;
	/** 默认每页记录数 */
	private static int DEFAULT_PAGE_SIZE = 10;
	
	/** 每页记录数 */
	private int pageSize; 
	/** 页码 */
	private int pageNum; 
	/** 总记录数 */
	private int totalRecords;
	
	/** 起始索引 */
	private int startIndex;
	/** 结束索引  */
	private int endIndex;
	/** 总页数 (根据pageNum+totalRecords运算得到数据) */
	private int totalPages; 
	
	/** 结果集 */
	private List<T> data;
	
	
	public Page(){

	}
	
	public Page(int pageNum, int pageSize){
		this.setPageSize(pageSize);
		this.setPageNum(pageNum);
	}
	
	public Page(int totalRecords){
		this.setTotalRecords(totalRecords);
	}
	
	public Page(int pageNum, int pageSize, int totalRecords){
		this.setPageSize(pageSize);
		this.setPageNum(pageNum);
		this.setTotalRecords(totalRecords);
	}

	public int getPageNum() {
		return this.pageNum;
	}
	
	public void setPageNum(int pageNum) {
		if (pageNum < 1) {
			pageNum = DEFAULT_PAGE_NUM;
		}
		this.pageNum = pageNum;
		this.startIndex = this.pageSize * (pageNum - 1);
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize< 1 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	public int getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		this.totalPages = (totalRecords + this.pageSize - 1) / this.pageSize;
		if (this.pageNum > this.totalPages) {
			if(this.totalPages>0){
				this.pageNum = this.totalPages;
			}else{
				this.pageNum = DEFAULT_PAGE_NUM;
			}
			this.startIndex = this.pageSize * (this.pageNum - 1);
			this.endIndex = totalRecords;
		}
		this.endIndex = this.startIndex + this.pageSize > totalRecords ? totalRecords : this.startIndex + this.pageSize;
	}
	
	public int getStartIndex() {
		return this.startIndex;
	}

	public int getEndIndex() {

		return this.endIndex;
	}

	public int getTotalPages() {
		return this.totalPages;
	}
	
	public List<T> getData() {
		return this.data;
	}

	public void setData(List<T> data) {
		this.data = data;
		PageContext.removePage();
	}
	
	/** -----------------------------------------------------------以下属于锦上添花部分,用不到就忽略----------------------------------------------------- */

	/** 获取首页 */
	public int getTopPageNo() {
		return DEFAULT_PAGE_NUM;
	}

	/** 获取上一页  */
	public int getPreviousPageNum() {
		if (this.pageNum <= 1) {
			return DEFAULT_PAGE_NUM;
		}
		return this.pageNum - 1;
	}

	/** 获取下一页 */
	public int getNextPageNum() {
		if (this.pageNum >= getBottomPageNum()) {
			return getBottomPageNum();
		}
		return this.pageNum + 1;
	}

	/** 获取尾页 */
	public int getBottomPageNum() {
		return this.getTotalPages();
	}
	
	/** 是否有上一页 */
	public boolean isHavePrePage(){
		if(this.pageNum > 1){
			return true;
		}else{
			return false;
		}
	}
	
	/** 是否有下一页 */
	public boolean isHaveNextPage(){
		// 计算是否有下一页
		if(this.pageNum < this.totalPages){
			return true;
		}else{
			return false;
		}
	}
	
}