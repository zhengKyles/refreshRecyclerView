package com.kyle.refreshrecyclerview.interfaces;

import java.util.List;

public interface Pager<D> {
    int getPage();

    int getLimit();

    int getTotalPages();

    void setPage(int page);

    void setTotalPage(int totalPage);

    List<D> getData();
}
