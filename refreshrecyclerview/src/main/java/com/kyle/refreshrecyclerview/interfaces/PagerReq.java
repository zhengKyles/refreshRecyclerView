package com.kyle.refreshrecyclerview.interfaces;

import java.util.List;

public interface PagerReq<D> {
    int getPage();

    int getLimit();
    
    void setPage(int page);

}
