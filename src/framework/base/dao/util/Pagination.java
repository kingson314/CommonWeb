package framework.base.dao.util;

import java.util.List;

/**
 * @Description:分页查询Bean
 * @date Jan 28, 2014
 * @author:fgq
 */
public class Pagination {
	// 每页显示记录数
	private int pageSize;
	// 当前页码
	private int currentPage;
	// 总页数
	private int pagecount;
	// 总记录数
	private int totalCount;
	// 记录集
	private List<Object> listData;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Object> getListData() {
		return listData;
	}

	public void setListData(List<Object> listData) {
		this.listData = listData;
	}

}
