package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.AllFragmentAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.LogisticsDetailActivity;
import com.ysxsoft.freshmall.view.RefundDetialActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 退
 */
public class ReturnGoodsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private AllFragmentAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler();
    private String uid;
    private OrderListBean orderListBean;

    @Override
    protected void initData() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) getViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) getViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new AllFragmentAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(getActivity(), mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        mDataAdapter.setOnClickListener(new AllFragmentAdapter.OnClickListener() {
            @Override
            public void cancleClick(int position) {
            }

            @Override
            public void checkClick(int position) {
                String orderId = mDataAdapter.getDataList().get(position).getId();
                int order_status = mDataAdapter.getDataList().get(position).getOrder_status();
                switch (order_status) {
                    case 6://售后中
                        Intent intent6 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent6.putExtra("orderId", orderId);
                        intent6.putExtra("type", "2");
                        startActivity(intent6);
                        break;
                    case 7://待退款寄回
                        Intent intent7 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent7.putExtra("orderId", orderId);
                        intent7.putExtra("type", "6");
                        startActivity(intent7);
                        break;
                    case 8://退款寄回待确认
                        Intent intent8 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent8.putExtra("orderId", orderId);
                        intent8.putExtra("type", "5");
                        startActivity(intent8);
                        break;
                    case 9://退款已完成
                        Intent intent9 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent9.putExtra("orderId", orderId);
                        intent9.putExtra("type", "3");
                        startActivity(intent9);
                        break;
                }
//                Intent intent6 = new Intent(getActivity(), RefundDetialActivity.class);
//                intent6.putExtra("orderId", mDataAdapter.getDataList().get(position).getId());
//                intent6.putExtra("type", "2");
//                startActivity(intent6);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (orderListBean != null) {
                    if (page < orderListBean.getPages()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.all_fragment_layout;
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
        if (AppUtil.isNetworkAvaiable(getActivity())) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.refreshComplete(orderListBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(orderListBean.getData().size());
                                notifyDataSetChanged();
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
                .getOrderList(uid, "6", String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderListBean>() {
                    private OrderListBean orderListBean;

                    @Override
                    public void onCompleted() {
                        if (orderListBean.getCode() == 0) {
                            showData(orderListBean);
                            List<OrderListBean.DataBean> data = orderListBean.getData();
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
                    public void onNext(OrderListBean orderListBean) {

                        this.orderListBean = orderListBean;
                    }
                });
    }

    private void showData(OrderListBean orderListBean) {
        this.orderListBean = orderListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = SpUtils.getSp(getActivity(), "uid");
        onRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
        } else {  // 在最前端显示 相当于调用了onResume();
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
    }
}
