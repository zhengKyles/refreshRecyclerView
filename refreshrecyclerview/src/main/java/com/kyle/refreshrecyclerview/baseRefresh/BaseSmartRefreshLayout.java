package com.kyle.refreshrecyclerview.baseRefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by Kyle on 2018/10/25.
 */

public class BaseSmartRefreshLayout extends SmartRefreshLayout {
    public BaseSmartRefreshLayout(Context context) {
        super(context);
    }

    public BaseSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs,-1);
        setEnableAutoLoadMore(false);
        setEnableHeaderTranslationContent(true);
        setRefreshHeader(new ClassicsHeader(context));
        setRefreshFooter(new ClassicsFooter(context).setDrawableSize(20));
    }
}
