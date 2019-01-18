package com.kyle.refreshrecyclerview.interfaces;

public interface PagerReq<D> {
    int getPage();

    int getLimit();
    
    void setPage(int page);

}
