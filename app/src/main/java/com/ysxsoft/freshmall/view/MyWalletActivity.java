package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.MyWalletAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.UserInfoBean;
import com.ysxsoft.freshmall.modle.YueInfoBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.pageslidingtableview.PageSlidingTableView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView tv_money, tv_WithdrawCash, tv_recharge;
    private PageSlidingTableView slidingTableView;
    private int stateBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private int page = 1;
    private MyWalletAdapter mDataAdapter;
    private String uid;
    private String type = "";
    private YueInfoBean yueInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        setBackVisibily();
        initView();
        initListener();
    }

    private void initView() {
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        tv_money = getViewById(R.id.tv_money);
        tv_WithdrawCash = getViewById(R.id.tv_WithdrawCash);
        tv_recharge = getViewById(R.id.tv_recharge);
        slidingTableView = getViewById(R.id.pstv_indicator);
        slidingTableView.setTabTitles(new String[]{"全部", "收入", "支出"});

        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
                        type = "";
                        onRefresh();
                        break;
                    case 1:
                        type = "1";
                        onRefresh();
                        break;
                    case 2:
                        type = "2";
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
        mDataAdapter = new MyWalletAdapter(mContext);
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

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (yueInfoBean != null) {
                    if (page < yueInfoBean.getPages()) {
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
        tv_money.setOnClickListener(this);
        tv_WithdrawCash.setOnClickListener(this);
        tv_recharge.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.my_wallet_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_money:
                break;
            case R.id.tv_WithdrawCash:
                startActivity(WithdrawCashActivity.class);
                break;
            case R.id.tv_recharge:
                startActivity(RechargeActivity.class);
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

        private WeakReference<MyWalletActivity> ref;

        PreviewHandler(MyWalletActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyWalletActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData(type);
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(yueInfoBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(yueInfoBean.getData().size());
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

    private void getData(String type) {
        NetWork.getService(ImpService.class)
                .getYueInfo(uid, String.valueOf(type), String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YueInfoBean>() {
                    private YueInfoBean yueInfoBean;

                    @Override
                    public void onCompleted() {
                        if (yueInfoBean.getCode() == 0) {
                            showData(yueInfoBean);
                            List<YueInfoBean.DataBean> data = yueInfoBean.getData();
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
                    public void onNext(YueInfoBean yueInfoBean) {
                        this.yueInfoBean = yueInfoBean;
                    }
                });


    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(YueInfoBean yueInfoBean) {
        this.yueInfoBean = yueInfoBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
        getYuEData();
    }

    private void getYuEData() {
        NetWork.getService(ImpService.class)
                .getUserInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    private UserInfoBean userInfoBean;

                    @Override
                    public void onCompleted() {
                        if (userInfoBean.getCode() == 0) {
                            DecimalFormat decimalFormat=new DecimalFormat("0.00");
                           tv_money.setText(decimalFormat.format(Double.valueOf(userInfoBean.getData().getYue())));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {

                        this.userInfoBean = userInfoBean;
                    }
                });
    }
}
