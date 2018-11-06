package com.pagehelper.plugin;

/**
 * 分页辅助类
 * @author limengjun
 */
public class PageHelper{

	public static void startPage(int pageNum, int pageSize){
		Page page = new Page(pageNum, pageSize);
		PageContext.setPage(page);
	}

	public static void clearPage() {
		PageContext.removePage();
	}

}