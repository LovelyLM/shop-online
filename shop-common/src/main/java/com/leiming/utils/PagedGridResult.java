package com.leiming.utils;

import java.util.List;

/**
 * 
 * @author LeiMing
 * @Title: PagedGridResult.java
 * @Description: 用来返回分页Grid的数据格式
 */
public class PagedGridResult {
	
	private int page;
	private int total;
	private long records;
	private List<?> rows;

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getRecords() {
		return records;
	}
	public void setRecords(long records) {
		this.records = records;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
