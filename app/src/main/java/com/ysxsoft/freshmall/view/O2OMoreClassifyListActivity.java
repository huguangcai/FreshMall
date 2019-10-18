package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OMoreClassifyListAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.O2OSearchDataBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;

import java.lang.ref.WeakReference;
import java.nio.channels.AsynchronousFileChannel;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OMoreClassifyListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ImageView img_title_right, img_juli_up, img_juli_down, img_price_up,
            img_price_down, img_xiaoliang_up, img_xiaoliang_down;
    private LinearLayout ll_juli, ll_price, ll_xiaoliang;
    private TextView tv_juli, tv_price, tv_xiaoliang;
    private boolean isJuli = true;
    private boolean isPrice = true;
    private boolean isXL = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private String latitude, longitude, content, sid2, sid1;
    private int type = 1;
    private int px = 1;
    private O2OMoreClassifyListAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private O2OSearchDataBean o2OSearchDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        content = intent.getStringExtra("content");
        sid2 = intent.getStringExtra("sid2");
        sid1 = intent.getStringExtra("sid1");
        setBackVisibily();
        setTitle("附近");
        initView();
        initListener();
    }

    private void initView() {
        img_title_right = getViewById(R.id.img_title_right);
        ll_juli = getViewById(R.id.ll_juli);
        img_juli_up = getViewById(R.id.img_juli_up);
        img_juli_down = getViewById(R.id.img_juli_down);
        ll_price = getViewById(R.id.ll_price);
        img_price_up = getViewById(R.id.img_price_up);
        img_price_down = getViewById(R.id.img_price_down);
        ll_xiaoliang = getViewById(R.id.ll_xiaoliang);
        img_xiaoliang_up = getViewById(R.id.img_xiaoliang_up);
        img_xiaoliang_down = getViewById(R.id.img_xiaoliang_down);
        tv_juli = getViewById(R.id.tv_juli);
        tv_price = getViewById(R.id.tv_price);
        tv_xiaoliang = getViewById(R.id.tv_xiaoliang);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new O2OMoreClassifyListAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                O2OSearchDataBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                Intent intent = new Intent(mContext, O2OMallDetailActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("mallId", dataBean.getId());
                startActivity(intent);
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    private void initListener() {
        img_title_right.setOnClickListener(this);
        ll_juli.setOnClickListener(this);
        ll_price.setOnClickListener(this);
        ll_xiaoliang.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_more_list_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_title_right:
                finish();
                break;
            case R.id.ll_juli:
                type = 1;
                isPrice = true;
                isXL = true;
                if (isJuli) {
                    px = 1;
                    isJuli = false;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_ok);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                } else {
                    px = 2;
                    isJuli = true;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_ok);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                }
                onRefresh();
                break;
            case R.id.ll_price:
                type = 2;
                isJuli = true;
                isXL = true;
                if (isPrice) {
                    px = 1;
                    isPrice = false;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_ok);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                } else {
                    px = 2;
                    isPrice = true;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_ok);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);

                }
                onRefresh();
                break;
            case R.id.ll_xiaoliang:
                type = 3;
                isJuli = true;
                isPrice = true;
                if (isXL) {
                    px = 1;
                    isXL = false;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_ok);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                } else {
                    px = 2;
                    isXL = true;
                    img_juli_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_juli_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_price_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_price_down.setBackgroundResource(R.mipmap.img_o2o_down_normal);
                    img_xiaoliang_up.setBackgroundResource(R.mipmap.img_o2o_up_normal);
                    img_xiaoliang_down.setBackgroundResource(R.mipmap.img_o2o_down_ok);
                }
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
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

        private WeakReference<O2OMoreClassifyListActivity> ref;

        PreviewHandler(O2OMoreClassifyListActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final O2OMoreClassifyListActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    if (!TextUtils.isEmpty(content)) {
                        getData();
                    } else if (!TextUtils.isEmpty(sid1)) {
                        getFirstData();
                    } else if (!TextUtils.isEmpty(sid2)) {
                        getSecondData();
                    }
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(o2OSearchDataBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(o2OSearchDataBean.getData().size());
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

    private void getSecondData() {
        NetWork.getService(ImpService.class)
                .SecondO2OSearchData(sid2, String.valueOf(type), String.valueOf(px), String.valueOf(longitude), String.valueOf(latitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OSearchDataBean>() {
                    private O2OSearchDataBean o2OSearchDataBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(o2OSearchDataBean.getMsg());
                        if (o2OSearchDataBean.getCode() == 0) {
                            px = 1;
                            showData(o2OSearchDataBean);
                            List<O2OSearchDataBean.DataBean> data = o2OSearchDataBean.getData();
//                            mDataAdapter.addAll(data);
                            mDataAdapter.setDataList(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(O2OSearchDataBean o2OSearchDataBean) {

                        this.o2OSearchDataBean = o2OSearchDataBean;
                    }
                });

    }

    private void getFirstData() {
        NetWork.getService(ImpService.class)
                .FirstO2OSearchData(sid1, String.valueOf(type), String.valueOf(px), String.valueOf(longitude), String.valueOf(latitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OSearchDataBean>() {
                    private O2OSearchDataBean o2OSearchDataBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(o2OSearchDataBean.getMsg());
                        if (o2OSearchDataBean.getCode() == 0) {
                            px = 1;
                            showData(o2OSearchDataBean);
                            List<O2OSearchDataBean.DataBean> data = o2OSearchDataBean.getData();
//                            mDataAdapter.addAll(data);
                            mDataAdapter.setDataList(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(O2OSearchDataBean o2OSearchDataBean) {

                        this.o2OSearchDataBean = o2OSearchDataBean;
                    }
                });

    }

    private void getData() {
        NetWork.getService(ImpService.class)
                .O2OSearchData(content, String.valueOf(type), String.valueOf(px), String.valueOf(longitude), String.valueOf(latitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OSearchDataBean>() {
                    private O2OSearchDataBean o2OSearchDataBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(o2OSearchDataBean.getMsg());
                        if (o2OSearchDataBean.getCode() == 0) {
                            px = 1;
                            showData(o2OSearchDataBean);
                            List<O2OSearchDataBean.DataBean> data = o2OSearchDataBean.getData();
//                            mDataAdapter.addAll(data);
                            mDataAdapter.setDataList(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(O2OSearchDataBean o2OSearchDataBean) {

                        this.o2OSearchDataBean = o2OSearchDataBean;
                    }
                });
    }

    private void showData(O2OSearchDataBean o2OSearchDataBean) {

        this.o2OSearchDataBean = o2OSearchDataBean;
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
