package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ClassifyGoodsAdapter;
import com.ysxsoft.freshmall.adapter.SearchResultAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.SearchResultBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.pageslidingtableview.PageSlidingTableView;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView tv_title_right;
    private PageSlidingTableView slidingTableView;
    private String content,lid1;
    private EditText ed_title_search;
    private int page=1;
    private int type=0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private SearchResultAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private SearchResultBean searchResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        Intent intent = getIntent();
        lid1 = intent.getStringExtra("lid1");
        content = intent.getStringExtra("content");
        initView();
        initListener();
    }

    private void initView() {
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("搜索");
        ed_title_search = getViewById(R.id.ed_title_search);
        ed_title_search.setText(content);
        slidingTableView = getViewById(R.id.pstv_indicator);
        slidingTableView.setTabTitles(new String[]{"综合", "销量", "最新"});
        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
                        type=0;
                        page=1;
                        onRefresh();
                        break;
                    case 1:
                        type=1;
                        page=1;
                        onRefresh();
                        break;
                    case 2:
                        type=2;
                        page=1;
                        onRefresh();
                        break;

                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new SearchResultAdapter(mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                startActivity(GoodsDetailActivity.class);
                String id = mDataAdapter.getDataList().get(position).getId();
                Intent intent=new Intent(mContext,GoodsDetailActivity.class);
                intent.putExtra("goodsId",id);
                startActivity(intent);

            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (searchResultBean != null) {
                    if (page < searchResultBean.getPages()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    private void initListener() {
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.search_result_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_title_right:
                if (TextUtils.isEmpty(ed_title_search.getText().toString())){
                    showToastMessage("搜索不能为空");
                    return;
                }
                AppUtil.colsePhoneKeyboard(this);
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(mContext)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {
        private WeakReference<SearchResultActivity> ref;
        PreviewHandler(SearchResultActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SearchResultActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    if (!TextUtils.isEmpty(ed_title_search.getText().toString())){
                        getData();
                    }else {
                        if (lid1!=null&&!TextUtils.isEmpty(lid1)) {
                            RequestSecondData(lid1);
                        }
                    }
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(searchResultBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(searchResultBean.getData().size());
                                activity.notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        String trim = ed_title_search.getText().toString().trim();
        NetWork.getService(ImpService.class)
                .SearchResultData(String.valueOf(page),String.valueOf(type),trim, SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResultBean>() {
                    private SearchResultBean searchResultBean;

                    @Override
                    public void onCompleted() {
                        if (searchResultBean.getCode()==0){
                            showData(searchResultBean);
                            List<SearchResultBean.DataBean> data = searchResultBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SearchResultBean searchResultBean) {

                        this.searchResultBean = searchResultBean;
                    }
                });

    }

    private void RequestSecondData(String id) {
        NetWork.getService(ImpService.class)
                .SearchGetTypeListData(SpUtils.getSp(mContext,"uid"), id,String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResultBean>() {
                    private SearchResultBean searchResultBean;

                    @Override
                    public void onCompleted() {
                        if (searchResultBean.getCode() == 0) {
                            showData(searchResultBean);
                            List<SearchResultBean.DataBean> data = searchResultBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SearchResultBean getTypeListBean) {
                        this.searchResultBean = getTypeListBean;
                    }
                });

    }

    private void showData(SearchResultBean searchResultBean) {

        this.searchResultBean = searchResultBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
