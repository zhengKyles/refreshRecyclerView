# refreshRecyclerView
refreshRecyclerView

#使用

    api 'com.kyle:refreshRecyclerView:1.0.6'
    
编写一个网络请求的请求bean和返回bean分别实现PagerReq和PagerResp，比如：

PagerReq：

public class PagerReq implements com.kyle.refreshrecyclerview.interfaces.PagerReq {
    private int page=1;
    private int limit=10;


    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setPage(int page) {
        this.page=page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

 PagerResp:
 
public class PagerResp implements com.kyle.refreshrecyclerview.interfaces.PagerResp<String> {
    private List<String> data;
    private int curPage;
    private int totalPages;
    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public int getPage() {
        return curPage;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

然后编写一个adapter继承BaseAdapter(本库中的BaseAdapter)如：
public class GoDoorTimeAdapter extends BaseAdapter<String,ItemGoDoorDateListBinding> {
    public GoDoorTimeAdapter() {
        super(R.layout.xxx);
    }

    @Override
    protected void convert(ItemGoDoorDateListBinding binding, int position, GoDoorTimeResp item) {
       ...
    }
}

ItemGoDoorDateListBinding是databinding根据布局生产出来的一个类，databinding用法:https://blog.csdn.net/qby_nianjun/article/details/79198166

最后编写一个类继承RefreshRecyclerView，如下:

public class GoDoorTimeListView extends RefreshRecyclerView<GoDoorTimeAdapter,PagerReq>{
    public GoDoorTimeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected PagerReq getReq() {
        return new PagerReq();
    }

    @Override
    public GoDoorTimeAdapter getAdapter() {
        return new GoDoorTimeAdapter();
    }

    @Override
    public void loadData() {
   请求(){
   成功调用onSuccess(传入PagerResp对象)
   失败调用onError()
   可以使用proteted的req和adapter、resp对象
   }
    }
}


