package com.glitter.spring.boot.page;

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
	private long totalRecords;
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
		com.github.pagehelper.PageInfo pageInfoGithub = new com.github.pagehelper.PageInfo(data);
		if(null != pageInfoGithub){
			this.pageNum = pageInfoGithub.getPageNum();
			this.pageSize = pageInfoGithub.getPageSize();
			this.totalRecords = pageInfoGithub.getTotal();
			this.totalPages = pageInfoGithub.getPages();
			this.firstPageNum = pageInfoGithub.getFirstPage();
			this.lastPageNum = pageInfoGithub.getLastPage();
			this.prePageNum = pageInfoGithub.getPrePage();
			this.nextPageNum = pageInfoGithub.getNextPage();
			this.hasPrePage = pageInfoGithub.isHasPreviousPage();
			this.hasNextPage = pageInfoGithub.isHasNextPage();
		}
		this.data = data;
	}


	public int getPageSize() {
		return pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public long getTotalRecords() {
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