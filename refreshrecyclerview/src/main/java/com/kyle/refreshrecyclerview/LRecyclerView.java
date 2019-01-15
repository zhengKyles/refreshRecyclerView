package com.kyle.refreshrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Kyle on 2018/8/15.
 * 主RecyclerView 可设置分割线等
 */

public class LRecyclerView extends RecyclerView {

    public LRecyclerView(Context context) {
        this(context, null);
    }

    @IntDef({HORIZONTAL, VERTICAL, GRID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    private int mOrientation = VERTICAL;
    private Context context;

    private float dividerHorizontal = 1;
    private float dividerVertical = 1;

    private int spanCount = 1;//当Grid时，列数
    private Drawable mDivider = null;//分割线颜色、图片等

    private boolean lastEnable = false;//最后一行一列是否画线
    private PagerItemDecoration itemDecoration;

    public LRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOverScrollMode(SCROLL_AXIS_NONE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LRecyclerView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.LRecyclerView_divider_width_horizontal) {
                dividerHorizontal = a.getDimension(attr, 1);
            } else if (attr == R.styleable.LRecyclerView_divider_height_vertical) {
                dividerVertical = a.getDimension(attr, 1);
            } else if (attr == R.styleable.LRecyclerView_span_count) {
                this.spanCount = a.getInt(attr, 1);
            } else if (attr == R.styleable.LRecyclerView_recycler_divider) {
                mDivider = a.getDrawable(attr);
            } else if (attr == R.styleable.LRecyclerView_direction) {
                mOrientation = a.getInt(attr, VERTICAL);
            }else if(attr==R.styleable.LRecyclerView_lastEnable){
                lastEnable=a.getBoolean(attr,false);
            }
        }
        a.recycle();
        setOverScrollMode(OVER_SCROLL_NEVER);
        requestView();
    }

    public boolean isLastEnable() {
        return lastEnable;
    }

    public void setLastEnable(boolean lastEnable) {
        this.lastEnable = lastEnable;
    }

    public void setDivider(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    public void setDividerHorizontal(float dividerHorizontal) {
        this.dividerHorizontal = dividerHorizontal;
    }

    public void setDividerVertical(float dividerVertical) {
        this.dividerVertical = dividerVertical;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            this.mOrientation = orientation;
        }
        requestView();
    }

    public void setLayoutManager(LayoutManager manager) {
        super.setLayoutManager(manager);
    }

    public void requestView() {
        LayoutManager manager = null;
        switch (mOrientation) {
            case HORIZONTAL:
                manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                setPadding(0, 0, 0, 0);
                break;
            case VERTICAL:
                manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                setPadding(0, 0, 0, 0);
                break;
            case GRID:
                manager = new GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false);
                setPadding((int) dividerHorizontal, 0, 0, 0);
                break;
        }
        setLayoutManager(manager);

        if (itemDecoration != null) {
            removeItemDecoration(itemDecoration);
        }
        itemDecoration = new PagerItemDecoration();
        addItemDecoration(itemDecoration);
    }

    public class PagerItemDecoration extends ItemDecoration {

        int topBottom = 0;
        int leftRight = 0;

        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            topBottom = (int) dividerHorizontal;
            leftRight = (int) dividerVertical;
            if (mDivider != null) {
                if (mOrientation == GRID) {
                    drawHorizontal(c, parent);
                    drawVertical(c, parent);
                } else if (mOrientation == HORIZONTAL) {
                    drawHorizontal(c, parent);
                } else {
                    drawVertical(c, parent);
                }
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            int left;
            int right;
            int top;
            int bottom;
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final LayoutParams params =
                        (LayoutParams) child.getLayoutParams();
                left = child.getRight() + params.rightMargin;
                right = (int) (left + dividerHorizontal);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            int left;
            int right;
            int top;
            int bottom;
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final LayoutParams params =
                        (LayoutParams) child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = (int) (top + dividerVertical);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            boolean isLastRaw = false;
            boolean isLastColum = false;
            float curVertical = dividerVertical;
            float curHorizontal = dividerHorizontal;
            if (!lastEnable) {
                int position = parent.getChildAdapterPosition(view);
                isLastRaw = isLastRaw(parent, position, spanCount, parent.getAdapter().getItemCount());
                isLastColum = isLastColum(parent, position, spanCount, parent.getAdapter().getItemCount());
            }

            if (mOrientation == GRID) {
                if (isLastRaw) {
                    curVertical = 0;
                }
                if (isLastColum) {
                    curHorizontal = 0;
                }
                outRect.set(0, 0, (int) curHorizontal,
                        (int) curVertical);
            } else if (mOrientation == HORIZONTAL) {
                if (isLastColum) return;
                outRect.set(0, 0, (int) dividerHorizontal, 0);
            } else {
                if (isLastRaw) return;
                outRect.set(0, 0, 0, (int) dividerVertical);
            }
        }


        private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                    int childCount) {
            LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                    {
                        return true;
                    }
                } else {
                    childCount = childCount - childCount % spanCount;
                    if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                        return true;
                }
            }
            return false;
        }

        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                  int childCount) {
            LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager)
                        .getOrientation();
                // StaggeredGridLayoutManager 且纵向滚动
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    if (pos >= childCount)
                        return true;
                } else
                // StaggeredGridLayoutManager 且横向滚动
                {
                    // 如果是最后一行，则不需要绘制底部
                    if ((pos + 1) % spanCount == 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
