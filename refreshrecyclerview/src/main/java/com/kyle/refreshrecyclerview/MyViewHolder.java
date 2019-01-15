package com.kyle.refreshrecyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;


/**
 * Created by Kyle on 2018/9/13.
 */

public class MyViewHolder<D extends ViewDataBinding> extends BaseViewHolder {
    public D binding;

    public MyViewHolder(View view) {
        super(view);
        binding = DataBindingUtil.bind(view);
    }

}
