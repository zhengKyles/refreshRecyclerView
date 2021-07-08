package com.kyle.refreshrecyclerview.baseRefresh;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * Created by Kyle on 2018/9/19.
 */

public abstract class RefreshRecyclerView<Adapter extends BaseAdapter, Resp, Req extends PagerReq> extends RelativeLayout {
    public Req req;
    protected Context mContext;
    protected Adapter adapter;
    private RecyclerViewHandler recyclerViewHandler;
    protected LayoutRefreshRecyclerviewBinding binding;
    //是否只展示内容，意为不展示空数据页面、无网络页面、错误页面
    protected boolean onlyShowContent = false;

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = View.inflate(context, R.layout.layout_refresh_recyclerview, null);
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        addView(view, params);
        binding = DataBindingUtil.bind(view);
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
        if(onlyShowContent){
            return;
        }
        binding.list.setVisibility(View.GONE);
        binding.viewStatus.setVisibility(View.VISIBLE);
        inflate(mContext, getNoNetViewId(), binding.viewStatus);
    }

    public void showError() {
        if(onlyShowContent){
            return;
        }
        binding.list.setVisibility(View.GONE);
        binding.viewStatus.setVisibility(View.VISIBLE);
        inflate(mContext, getErrorViewId(), binding.viewStatus);
    }

    public void showEmpty() {
        if(onlyShowContent){
            return;
        }
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

    public void onSuccess(PagerResp<Resp> resp) {
        List<Resp> data = resp.getData();
        if (data == null || data.size() == 0) {
            if (req.getPage() > 1) {
                req.setPage(req.getPage() - 1);
            }
        }
        if (req.getPage() == 1) {
            if (data.size() == 0) {
                setNewData(new ArrayList());
                showEmpty();
                onFinish();
                return;
            }
        }
        showContent();
        addData(data);
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
