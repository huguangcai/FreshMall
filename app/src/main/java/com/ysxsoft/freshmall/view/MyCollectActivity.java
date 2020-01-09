package com.ysxsoft.freshmall.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.luck.picture.lib.permissions.RxPermissions;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.MyCollectAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.ColleteListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaiduLocationUtils;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyCollectActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private MyCollectAdapter mDataAdapter;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private ColleteListBean colleteListBean;
    private RxPermissions rxPermissions;
    private double latitude;
    private double longitude;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("我的收藏");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new MyCollectAdapter(mContext);
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
                ColleteListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                Intent intent = new Intent(mContext, O2OMallDetailActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("mallId", dataBean.getDid());
                startActivity(intent);
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");

    }


    @Override
    public int getLayout() {
        return R.layout.my_collect_layout;
    }

    @Override
    public void onRefresh() {
//        page = 1;
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
        private WeakReference<MyCollectActivity> ref;

        PreviewHandler(MyCollectActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyCollectActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(colleteListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(colleteListBean.getData().size());
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
        NetWork.getService(ImpService.class)
                .ColleteListData(SpUtils.getSp(mContext, "uid"), String.valueOf(longitude), String.valueOf(latitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ColleteListBean>() {
                    private ColleteListBean colleteListBean;

                    @Override
                    public void onCompleted() {
                        if (colleteListBean.getCode() == 0) {
                            showData(colleteListBean);
                            List<ColleteListBean.DataBean> data = colleteListBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else {
                            showToastMessage(colleteListBean.getMsg());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                       Log.e("tag",e.getMessage().toString());
                    }

                    @Override
                    public void onNext(ColleteListBean colleteListBean) {

                        this.colleteListBean = colleteListBean;
                    }
                });

    }

    private void showData(ColleteListBean colleteListBean) {
        this.colleteListBean = colleteListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaiduLocationUtils.initBdMapLocaton(mContext, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                latitude = bdLocation.getLatitude();
                longitude = bdLocation.getLongitude();
                onRefresh();
            }
        });
    }
}
