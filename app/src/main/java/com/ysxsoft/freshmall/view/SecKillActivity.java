package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.MyOrderAdapter;
import com.ysxsoft.freshmall.adapter.SecKillAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.SecikllMoreBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecKillActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private SecKillAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private SecikllMoreBean secikllMoreBean;
    private TextView tv_time,tv_desc,tv_qianggou;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("秒杀");
//        refund_bg_shape
//        seckill_item_layout
        initView();
    }

    private void initView() {
        tv_time = getViewById(R.id.tv_time);
        tv_desc = getViewById(R.id.tv_desc);
        tv_qianggou = getViewById(R.id.tv_qianggou);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new SecKillAdapter(mContext);
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
                String id = mDataAdapter.getDataList().get(position).getId();
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goodsId", id);
                intent.putExtra("seckill", "seckill");
                startActivity(intent);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (secikllMoreBean != null) {
                    if (page < secikllMoreBean.getData().getPages()) {
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

    @Override
    public int getLayout() {
        return R.layout.seckill_layout;
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
        private WeakReference<SecKillActivity> ref;

        PreviewHandler(SecKillActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SecKillActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(secikllMoreBean.getData().getSplist().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(secikllMoreBean.getData().getSplist().size());
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
                .SecikllMoreData(String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SecikllMoreBean>() {
                    private SecikllMoreBean secikllMoreBean;

                    @Override
                    public void onCompleted() {
                        if (secikllMoreBean.getCode() == 0) {
                            showData(secikllMoreBean);
                            List<SecikllMoreBean.DataBean.SplistBean> splist = secikllMoreBean.getData().getSplist();
                            mDataAdapter.addAll(splist);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(splist.size());
                            notifyDataSetChanged();

                            timer = new CountDownTimer(Long.valueOf(Integer.valueOf(secikllMoreBean.getData().getSytime())*1000), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    tv_time.setText(AppUtil.secToTime(Integer.valueOf(String.valueOf(millisUntilFinished)) / 1000));
                                }

                                @Override
                                public void onFinish() {
                                    tv_time.setText("00:00:00");
                                }
                            }.start();
                            switch (secikllMoreBean.getData().getStates()){
                                case "1":
                                    tv_desc.setText("距离本场开始");
                                    tv_time.setVisibility(View.VISIBLE);
                                    tv_qianggou.setVisibility(View.VISIBLE);
                                    break;
                                case "2":
                                    tv_desc.setText("距离本场结束");
                                    tv_time.setVisibility(View.VISIBLE);
                                    tv_qianggou.setVisibility(View.VISIBLE);
                                    break;
                                case "3":
                                    tv_desc.setText(secikllMoreBean.getData().getMsstart());
                                    tv_time.setVisibility(View.GONE);
                                    tv_qianggou.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SecikllMoreBean secikllMoreBean) {

                        this.secikllMoreBean = secikllMoreBean;
                    }
                });
    }

    private void showData(SecikllMoreBean secikllMoreBean) {

        this.secikllMoreBean = secikllMoreBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
