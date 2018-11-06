package com.pagehelper.plugin;

/**
 * 分页运算类上下文对象
 * @author limengjun
 */
public class PageContext {

	private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
	
	public static Page getPage() {
		return pageThreadLocal.get();
	}

	public static void setPage(Page page) {
		pageThreadLocal.set(page);
	}

	public static void removePage(){
		pageThreadLocal.remove();
	}

}
