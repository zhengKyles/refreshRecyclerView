# refreshRecyclerView
refreshRecyclerView


#目的

统一上拉加载下拉刷新样式和网络请求出错显示。

#使用

    api 'com.kyle:refreshRecyclerView:1.0.6'
    
编写一个网络请求的请求bean和返回bean分别实现PagerReq和PagerResp，比如：

PagerReq：

```java
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
```

 PagerResp:
 
 ```java
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
```
然后编写一个adapter继承BaseAdapter(本库中的BaseAdapter)如：

```java
public class GoDoorTimeAdapter extends BaseAdapter<String,ItemGoDoorDateListBinding> {
    public GoDoorTimeAdapter() {
        super(R.layout.xxx);
    }

    @Override
    protected void convert(ItemGoDoorDateListBinding binding, int position, GoDoorTimeResp item) {
       ...
    }
}
```
ItemGoDoorDateListBinding是databinding根据布局生产出来的一个类，databinding用法:https://blog.csdn.net/qby_nianjun/article/details/79198166

最后编写一个类继承RefreshRecyclerView，如下:

```java
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
```
然后在布局中使用：
```java
  <com.lejias.cproject.ui.orderlist.detail.godoortime.GoDoorTimeListView
            android:id="@+id/list"
            android:layout_marginTop="10dp"
            app:direction="vertical"
            app:divider_height_vertical="0dp"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
 ```
 
 最后在代码中调用请求：
 
 ```java
 list.loadData();
```
可用的参数:
```java
<declare-styleable name="RefreshRecyclerView">
        <attr name="divider_width_horizontal"/> 当recyclerView为水平时的item的水平间距
        <attr name="divider_height_vertical"/>  当recyclerView为竖直时的item的竖直间距
        <attr name="direction"/>                recyclerview的方向：vertical竖直  horizontal水平  grid网格
        <attr name="span_count"/>                recyclerview的方向为grid时的列数
        <attr name="recycler_divider"/>          recyclerview的分割线样式、一般是颜色
        <attr name="empty_id"/>                  recyclerview的空布局的view
        <attr name="lastEnable"/>                recyclerview的最后一个item的分割线是否可见
    </declare-styleable>
```

