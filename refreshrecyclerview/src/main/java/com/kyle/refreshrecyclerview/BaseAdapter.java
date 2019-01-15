package com.kyle.refreshrecyclerview;




import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;


/**
 * Created by Kyle on 2018/9/13.
 */

public abstract class BaseAdapter<T, D extends ViewDataBinding> extends BaseQuickAdapter<T, MyViewHolder> {
    public BaseAdapter(int layoutResId) {
        super(layoutResId, new ArrayList<>());
    }
    @Override
    protected MyViewHolder createBaseViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    protected void convert(MyViewHolder helper, T item) {
       convert((D) helper.binding,helper.getAdapterPosition(),item);
    }
    protected abstract void convert(D binding,int position,T item);

}
