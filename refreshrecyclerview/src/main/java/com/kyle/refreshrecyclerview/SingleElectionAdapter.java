package com.kyle.refreshrecyclerview;

import android.databinding.ViewDataBinding;


/***
 * 单选adapter
 * @param <T>
 * @param <D>
 */
public class SingleElectionAdapter<T, D extends ViewDataBinding> extends BaseAdapter<T, D> {
    private OnItemChildClickListener listener;
    protected int curPosition = -1;

    public SingleElectionAdapter(int layoutResId) {
        super(layoutResId);
    }

    public T getCheckedData() {
        if (curPosition == -1) return null;

        return getData().get(curPosition);
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    @Override
    protected void convert(D binding, int position, T item) {
        binding.getRoot().setOnClickListener(v -> {
            if(curPosition==position){
                curPosition=-1;
                SingleElectionAdapter.this.notifyItemChanged(position);
            }else {
                curPosition = position;
                SingleElectionAdapter.this.notifyDataSetChanged();
            }
            if(listener!=null){
                listener.onItemChildClick(this,binding.getRoot(),position);
            }
        });
    }


    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        this.listener=listener;
    }
}
