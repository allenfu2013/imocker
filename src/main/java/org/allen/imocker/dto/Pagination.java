package org.allen.imocker.dto;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
    private long total;
    private int totalPage;
    private int prePage;
    private int nextPage;
    private int currentPage;
    private List data = new ArrayList();
    private int pageSize;

    public Pagination(int pageSize, long total, int requestPage) {
        this.total = total;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil((double) total / (double) pageSize);
        this.currentPage = caculateCurrentPage(requestPage);
        this.prePage = (this.currentPage > 1) ? this.currentPage - 1 : 0;
        this.nextPage = (totalPage > this.currentPage) ? this.currentPage + 1 : totalPage;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    private int caculateCurrentPage(int requestPage) {
        int smallestPage = 1;
        try {
            return Math.min(totalPage, Math.max(smallestPage, requestPage));
        } catch (Exception e) {
            return smallestPage;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }

    public int getPageSize() {
        return pageSize;
    }
}
