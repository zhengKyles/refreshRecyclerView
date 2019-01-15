package com.kyle.refreshrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kyle on 2018/8/17.
 * 设置高度为最大值的RecyclerView
 */

public class LMyRecyclerView extends LRecyclerView {
    public LMyRecyclerView(Context context) {
        super(context);
    }

    public LMyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
       setNestedScrollingEnabled(false);
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
