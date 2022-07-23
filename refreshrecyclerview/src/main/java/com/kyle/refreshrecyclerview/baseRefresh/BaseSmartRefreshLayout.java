package com.kyle.refreshrecyclerview.baseRefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
 * Created by Kyle on 2018/10/25.
 */

public class BaseSmartRefreshLayout extends SmartRefreshLayout {
    public BaseSmartRefreshLayout(Context context) {
        super(context);
    }

    public BaseSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnableAutoLoadMore(false);
        setEnableHeaderTranslationContent(true);
        setRefreshHeader(new ClassicsHeader(context));
        setRefreshFooter(new ClassicsFooter(context).setDrawableSize(20));
    }
}
