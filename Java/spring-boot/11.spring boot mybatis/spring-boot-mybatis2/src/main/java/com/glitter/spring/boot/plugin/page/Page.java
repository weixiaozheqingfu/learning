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


	private Page(){

	}

	protected Page(int pageNum, int pageSize){
		this.setPageSize(pageSize);
		this.setPageNum(pageNum);
	}

	protected Page(int totalRecords){
		this.setTotalRecords(totalRecords);
	}

	protected Page(int pageNum, int pageSize, int totalRecords){
		this.setPageSize(pageSize);
		this.setPageNum(pageNum);
		this.setTotalRecords(totalRecords);
	}

	protected int getPageNum() {
		return this.pageNum;
	}

	protected void setPageNum(int pageNum) {
		this.pageNum = pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
	}

	protected int getPageSize() {
		return this.pageSize;
	}

	protected void setPageSize(int pageSize) {
		this.pageSize = pageSize< 1 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	protected int getTotalRecords() {
		return this.totalRecords;
	}

	protected void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;

		this.totalPages = 0 == totalRecords ? 0 : ((totalRecords + this.pageSize - 1) / this.pageSize);
		// 比如pageNum=1,totalPages=0 或 比如pageNum=100000,totalPages=0 或 pageNum=100000,totalPages=5, 想要请求的页码已经大于实际的页码的情况, 要进行纠错调整
		this.pageNum = 0 == totalRecords ? DEFAULT_PAGE_NUM : (this.pageNum <= this.totalPages ? this.pageNum: this.totalPages);

		this.startIndex = this.pageSize * (this.pageNum - 1);
		this.endIndex = this.startIndex + this.pageSize > totalRecords ? totalRecords : this.startIndex + this.pageSize;
	}

	protected int getStartIndex() {
		return this.startIndex;
	}

	protected int getEndIndex() {

		return this.endIndex;
	}

	protected int getTotalPages() {
		return this.totalPages;
	}

	protected List<T> getData() {
		return this.data;
	}

	protected void setData(List<T> data) {
		this.data = data;
		PageContext.removePage();
	}

	/** -----------------------------------------------------------以下属于锦上添花部分,用不到就忽略----------------------------------------------------- */

	/** 获取首页 */
	protected int getFirstPageNum() {
		return DEFAULT_PAGE_NUM;
	}

	/** 获取尾页 */
	protected int getLastPageNum() {
		return 0 == this.totalPages ? 1 : this.totalPages;
	}

	/** 获取上一页  */
	protected int getPrePageNum() {
		return this.pageNum > 1 ? this.pageNum - 1 : DEFAULT_PAGE_NUM;
	}

	/** 获取下一页 */
	protected int getNextPageNum() {
		return this.pageNum < this.getLastPageNum() ? this.pageNum + 1 : this.getLastPageNum();
	}

	/** 是否有上一页 */
	protected boolean hasPrePage(){
		return this.pageNum > 1;
	}

	/** 是否有下一页 */
	protected boolean hasNextPage(){
		return this.pageNum < this.totalPages;
	}

}