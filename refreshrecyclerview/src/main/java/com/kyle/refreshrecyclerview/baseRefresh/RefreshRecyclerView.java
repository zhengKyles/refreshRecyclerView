package com.kyle.refreshrecyclerview.baseRefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kyle.baserecyclerview.BaseAdapter;
import com.kyle.refreshrecyclerview.R;
import com.kyle.refreshrecyclerview.databinding.LayoutRefreshRecyclerviewBinding;
import com.kyle.refreshrecyclerview.interfaces.PagerReq;
import com.kyle.refreshrecyclerview.interfaces.PagerResp;
import com.kyle.refreshrecyclerview.util.NetUtils;

import java.util.List;

import static com.kyle.baserecyclerview.LRecyclerView.VERTICAL;


/**
 * Created by Kyle on 2018/9/19.
 */

public abstract class RefreshRecyclerView<Adapter extends BaseAdapter, Req extends PagerReq> extends RelativeLayout {
    protected LayoutRefreshRecyclerviewBinding binding;


    private Context mContext;
    private View emptyView;
    private View errorView;
    private View noNetView;

    public Adapter adapter;

    protected Req req;
    protected PagerResp resp;

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = View.inflate(context, R.layout.layout_refresh_recyclerview, null);
        binding = DataBindingUtil.bind(view);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
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
        a.recycle();
        addView(view);

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (resp == null || req.getPage() >= resp.getTotalPages()) {
                finishLoadMore();
                return;
            }
            req.setPage(req.getPage() + 1);
            loadData();
        });
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            reLoad();
        });
    }

    protected abstract Req getReq();

    public void reLoad() {
        req.setPage(1);
        request();
    }

    /***
     * 禁止分页
     */
    public void disableLoadMore() {
        binding.refreshLayout.setEnableLoadMore(false);
    }

    /***
     * 禁止下拉刷新
     */
    public void disableRefresh() {
        binding.refreshLayout.setEnableRefresh(false);
    }

    /***
     * 请求数据
     */
    public void request() {
        if (!NetUtils.isConnected(mContext)) {
            showNoNet();
        }
        if (!binding.refreshLayout.isRefreshing() && !binding.refreshLayout.isLoading()) {
            showLoading();
        }
        loadData();
    }

    public void onSuccess(PagerResp resp) {
        this.resp = resp;
        onRequestEnd();
        finishLoadMore();
        finishRefresh();
        if (resp.getPage() == 1) {
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
    }

    public void onError() {
        onRequestEnd();
        finishLoadMore();
        finishRefresh();
        showError();
    }


    public void showContent() {
        binding.refreshMultipleStatusView.showContent();
    }

    public void finishLoadMore() {
        binding.refreshLayout.finishLoadMore();
    }

    public void finishRefresh() {
        binding.refreshLayout.finishRefresh();
    }


    public void showNoNet() {
        if (noNetView != null) {
            binding.refreshMultipleStatusView.showNoNetwork(noNetView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            binding.refreshMultipleStatusView.showNoNetwork();
        }
    }


    public void showError() {
        if (errorView != null) {
            binding.refreshMultipleStatusView.showError(errorView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            binding.refreshMultipleStatusView.showError();
        }
    }

    public void showEmpty() {
        if (emptyView != null) {
            binding.refreshMultipleStatusView.showEmpty(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            binding.refreshMultipleStatusView.showEmpty();
        }
    }

    public void showLoading() {
        binding.refreshMultipleStatusView.showLoading();
    }


    public void setNewData(List d) {
        adapter.setNewData(d);
    }

    public void addData(List d) {
        adapter.addData(d);
    }

    public abstract Adapter getAdapter();

    public abstract void loadData();

    public void onRequestEnd() {

    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setAdapter(BaseAdapter adapter) {
        binding.list.setAdapter(adapter);
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        getAdapter().setOnItemClickListener(onItemClickListener);
    }

    public void setDividerHorizontal(int dividerHorizontal) {
        binding.list.setDividerHorizontal(dividerHorizontal);
    }

    public void requestView() {
        binding.list.requestView();
    }
}
