package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.Tab1ExchangeAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.ColleteListBean;
import com.ysxsoft.freshmall.modle.Tab1ExchangeResponse;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.ExchangeWaitFaDetailActivity;
import com.ysxsoft.freshmall.view.ShoppingPacketExchangeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Create By 胡
 * on 2020/1/7 0007
 */
public class Tab1ExchangeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler();
    private Tab1ExchangeAdapter mDataAdapter;
    private int page=1;
    private Tab1ExchangeResponse.DataBeanX data;

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
        mDataAdapter = new Tab1ExchangeAdapter(getActivity());

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
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = data.getData().get(position).getId();
                Intent intent = new Intent(getActivity(), ExchangeWaitFaDetailActivity.class);
                intent.putExtra("oid",String.valueOf(id));
                startActivity(intent);
            }
        });
        mDataAdapter.setOnClickListener(new Tab1ExchangeAdapter.OnAdapterClickListener() {
            @Override
            public void onItemClick(String id) {
                Intent intent = new Intent(getActivity(), ExchangeWaitFaDetailActivity.class);
                intent.putExtra("oid",String.valueOf(id));
                startActivity(intent);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (data != null) {
                    if (page < data.getLast_page()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab_exchange;
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
                        mRecyclerView.refreshComplete(data.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(data.getData().size());
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
        OkHttpUtils.post()
                .url(ImpService.WAIT_FAHUO)
                .addParams("uid", SpUtils.getSp(getActivity(),"uid"))
                .addParams("type", "0")
                .addParams("page", String.valueOf(page))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Tab1ExchangeResponse resp = JsonUtils.parseByGson(response, Tab1ExchangeResponse.class);
                        if (resp!=null){
                            if (resp.getCode()==200){
                                showData(resp.getData());
                                List<Tab1ExchangeResponse.DataBeanX.DataBean> data = resp.getData().getData();
                                mDataAdapter.addAll(data);
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                mRecyclerView.refreshComplete(data.size());
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    private void showData(Tab1ExchangeResponse.DataBeanX data) {
        this.data = data;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

}
