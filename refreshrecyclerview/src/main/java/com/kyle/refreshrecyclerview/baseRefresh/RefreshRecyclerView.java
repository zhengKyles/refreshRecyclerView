package com.kyle.refreshrecyclerview.baseRefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kyle.baserecyclerview.BaseAdapter;
import com.kyle.refreshrecyclerview.R;
import com.kyle.refreshrecyclerview.databinding.LayoutRefreshRecyclerviewBinding;
import com.kyle.refreshrecyclerview.interfaces.PagerReq;
import com.kyle.refreshrecyclerview.interfaces.PagerResp;
import com.kyle.refreshrecyclerview.util.NetUtils;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.kyle.baserecyclerview.LRecyclerView.VERTICAL;


/**
 * Created by Kyle on 2018/9/19.
 */

public abstract class RefreshRecyclerView<Adapter extends BaseAdapter, Resp, Req extends PagerReq> extends RelativeLayout {
    public Req req;
    protected Context mContext;
    protected Adapter adapter;
    private RecyclerViewHandler recyclerViewHandler;
    protected LayoutRefreshRecyclerviewBinding binding;

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = View.inflate(context, R.layout.layout_refresh_recyclerview, null);
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        addView(view, params);
        binding = DataBindingUtil.bind(view);
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RefreshRecyclerView_divider_width_horizontal) {
                binding.list.setDividerHorizontal((int) a.getDimension(attr, 1));
            } else if (attr == R.styleable.RefreshRecyclerView_divider_height_vertical) {
                binding.list.setDividerVertical((int) a.getDimension(attr, 1));
            } else if (attr == R.styleable.RefreshRecyclerView_span_count) {
                binding.list.setSpanCount(a.getInt(attr, 1));
            } else if (attr == R.styleable.RefreshRecyclerView_recycler_divider) {
                binding.list.setDivider(a.getDrawable(attr));
            } else if (attr == R.styleable.RefreshRecyclerView_direction) {
                binding.list.setOrientation(a.getInt(attr, VERTICAL));
            } else if (attr == R.styleable.RefreshRecyclerView_lastEnable) {
                binding.list.setLastEnable(a.getBoolean(attr, false));
            }
        }
        adapter = getAdapter();
        req = getReq();
        binding.list.setAdapter(adapter);
        binding.list.requestView();

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            int page = req.getPage();
            req.setPage(page + 1);
            load();
        });
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> reLoad());
        a.recycle();
    }


    public void finishLoadMore() {
        binding.refreshLayout.finishLoadMore();
    }

    public void finishRefresh() {
        binding.refreshLayout.finishRefresh();
    }

    public void showContent() {
        binding.list.setVisibility(View.VISIBLE);
        binding.viewStatus.setVisibility(View.GONE);
    }


    public void showNoNet() {
        binding.list.setVisibility(View.GONE);
        binding.viewStatus.setVisibility(View.VISIBLE);
        inflate(mContext, getNoNetViewId(), binding.viewStatus);
    }

    public void showError() {
        binding.list.setVisibility(View.GONE);
        binding.viewStatus.setVisibility(View.VISIBLE);
        inflate(mContext, getErrorViewId(), binding.viewStatus);
    }

    public void showEmpty() {
        binding.list.setVisibility(View.GONE);
        binding.viewStatus.setVisibility(View.VISIBLE);
        inflate(mContext, getEmptyViewId(), binding.viewStatus);
    }

    public void showLoading() {
        binding.refreshLayout.autoRefresh(0, 200, 1f);
    }

    /***
     * 禁止分页
     */
    public void disableLoadMore() {
        binding.refreshLayout.setEnableLoadMore(false);
    }

    public void enableLoadMore() {
        binding.refreshLayout.setEnableLoadMore(true);
    }

    /***
     * 禁止下拉刷新
     */
    public void disableRefresh() {
        binding.refreshLayout.setEnableRefresh(false);
    }

    public void setNewData(List<Resp> d) {
        adapter.setNewData(d);
    }

    public void addData(List<Resp> d) {
        adapter.addData(d);
    }

    /***
     * 请求数据
     */
    public void request() {
        if (!NetUtils.isConnected(mContext)) {
            showNoNet();
            onFinish();
            return;
        }
        if (!binding.refreshLayout.isRefreshing() && !binding.refreshLayout.isLoading()) {
            showLoading();
        }
    }

    public void onSuccess(PagerResp resp) {
        if (req.getPage() == 1) {
            if (resp.getData().size() == 0) {
                showEmpty();
                return;
            }
            showContent();
            setNewData(resp.getData());
        } else {
            showContent();
            addData(resp.getData());
        }
        onFinish();
    }

    protected void onError() {
        if (isFirstPage()) {
            showError();
        }
        onFinish();
    }


    /***
     * 请求结束 无论失败或者成功都会返回
     */
    public void onFinish() {
        finishLoadMore();
        finishRefresh();
        if (recyclerViewHandler != null) {
            recyclerViewHandler.onFinish();
        }
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        getAdapter().setOnItemClickListener(onItemClickListener);
    }

    protected abstract Adapter getAdapter();


    protected abstract void loadData();

    protected abstract Req getReq();

    protected boolean isFirstPage() {
        return 1 == req.getPage();
    }


    public void reLoad() {
        req.setPage(1);
        load();
    }

    private void load() {
        if (!NetUtils.isConnected(mContext)) {
            showNoNet();
            return;
        }
        loadData();
    }

    public void setRecyclerViewHandler(RecyclerViewHandler handler) {
        this.recyclerViewHandler = handler;
    }

    public interface RecyclerViewHandler {
        void onFinish();
    }

    protected int getErrorViewId() {
        return R.layout.layout_error;
    }

    protected int getEmptyViewId() {
        return R.layout.layout_empty;
    }

    protected int getNoNetViewId() {
        return R.layout.layout_no_net;
    }

}
