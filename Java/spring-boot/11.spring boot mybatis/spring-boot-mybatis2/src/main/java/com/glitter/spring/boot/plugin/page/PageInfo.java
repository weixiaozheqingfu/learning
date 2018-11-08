package com.glitter.spring.boot.plugin.page;

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

	/** 首页 */
	private int firstPageNum;
	/** 尾页 */
	private int lastPageNum;
	/** 上一页  */
	private int prePageNum;
	/** 下一页 */
	private int nextPageNum;
	/** 是否有上一页 */
	private boolean hasPrePage;
	/** 是否有下一页 */
	private boolean hasNextPage;

	public PageInfo(List<T> data){
		Page page = PageContext.getPage();
		if(null != page){
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.totalRecords = page.getTotalRecords();
			this.totalPages = page.getTotalPages();
			this.firstPageNum = page.getFirstPageNum();
			this.lastPageNum = page.getLastPageNum();
			this.prePageNum = page.getPrePageNum();
			this.nextPageNum = page.getNextPageNum();
			this.hasPrePage = page.hasPrePage();
			this.hasNextPage = page.hasNextPage();
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

	public int getFirstPageNum() {
		return firstPageNum;
	}

	public int getLastPageNum() {
		return lastPageNum;
	}

	public int getPrePageNum() {
		return prePageNum;
	}

	public int getNextPageNum() {
		return nextPageNum;
	}

	public boolean isHasPrePage() {
		return hasPrePage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

}