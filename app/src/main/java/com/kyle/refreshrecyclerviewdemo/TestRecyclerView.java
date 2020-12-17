package com.kyle.refreshrecyclerviewdemo;

import android.content.Context;
import android.util.AttributeSet;

import com.kyle.baserecyclerview.BaseAdapter;
import com.kyle.refreshrecyclerview.baseRefresh.RefreshRecyclerView;
import com.kyle.refreshrecyclerview.interfaces.PagerReq;
import com.kyle.refreshrecyclerview.interfaces.PagerResp;
import com.kyle.refreshrecyclerviewdemo.databinding.ItemTestBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * author : kyle
 * e-mail : 1239878682@qq.com
 * date   : 12/17/20
 * 看了我的代码，感动了吗?
 */
class TestRecyclerView extends RefreshRecyclerView<TestRecyclerView.TestAdapter, PagerReq<String>> {

    public TestRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PagerReq<String> getReq() {
        return new PagerReq<String>() {
            @Override
            public int getPage() {
                return 1;
            }

            @Override
            public int getLimit() {
                return 20;
            }

            @Override
            public void setPage(int page) {

            }
        };
    }

    @Override
    public TestAdapter getAdapter() {
        return new TestAdapter(R.layout.item_test);
    }

    @Override
    public void loadData() {
        PagerResp<String> resp=new PagerResp<String>() {
            @Override
            public List<String> getData() {
                List<String>list=new ArrayList<>();
                list.add("");
                list.add("");
                list.add("");
                list.add("");
                list.add("");
                return list;
            }

            @Override
            public int getPage() {
                return 1;
            }

            @Override
            public int getTotalPages() {
                return 10;
            }
        };
        onSuccess(resp);
    }

    public class TestAdapter extends BaseAdapter<String, ItemTestBinding> {
        public TestAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(ItemTestBinding binding, int position, String item) {

        }
    }
}
