package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.AddressManagerAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.DeleteAddressBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetGoodsAddressActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private Button btn_submit;
    private TextView tv_new_add_address;
    private LinearLayout ll_no_address;
    private FrameLayout fl_have_address;
    private String uid;
    private TextView tv_title;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private AddressManagerAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private GetAddressListBean getAddressListBean;
    private int is_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        setBackVisibily();
        setTitle("收货地址");
        initView();
        initListener();
    }

    @Override
    public int getLayout() {
        return R.layout.get_goods_address_layout;
    }


    private void initView() {
        ll_no_address = getViewById(R.id.ll_no_address);
        btn_submit = getViewById(R.id.btn_submit);
        fl_have_address = getViewById(R.id.fl_have_address);
        tv_new_add_address = getViewById(R.id.tv_new_add_address);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new AddressManagerAdapter(this);
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
        mDataAdapter.setOnDeleteClickListener(new AddressManagerAdapter.OnDeleteClickListener() {
            @Override
            public void deleteItem(int position) {
                GetAddressListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int aid = dataBean.getId();
                int is_ture = dataBean.getIsmr();
                if (is_ture == 1) {
                    showToastMessage("默认地址不能删除");
                } else {
                    DeleteItemData(aid);
                }
            }

            @Override
            public void editorItem(int position) {
                GetAddressListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int aid = dataBean.getId();
                String area = dataBean.getShszq();
                String address = dataBean.getShxxdz();
                String linkname = dataBean.getShname();
                String phone = dataBean.getShphone();
                int is_ture = dataBean.getIsmr();
                Intent intent = new Intent(mContext, AddressManagerActivity.class);
                intent.putExtra("aid", String.valueOf(aid));
                intent.putExtra("city", area);
                intent.putExtra("address", address);
                intent.putExtra("linkname", linkname);
                intent.putExtra("phone", phone);
                intent.putExtra("is_ture", is_ture);
                startActivity(intent);
            }
        });
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            int currentNum = -1;
            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().get(position).getIsmr() == 1) {
                    return;
                }
                for (GetAddressListBean.DataBean data : mDataAdapter.getDataList()) {
                    data.setIsmr(0);
                }
                if (currentNum == -1) { //选中
                    mDataAdapter.getDataList().get(position).setIsmr(1);
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    for (GetAddressListBean.DataBean date : mDataAdapter.getDataList()) { //遍历list集合中的数据
                        date.setIsmr(0);//全部设为未选中
                    }
                    currentNum = -1;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (GetAddressListBean.DataBean date : mDataAdapter.getDataList()) { //遍历list集合中的数据
                        date.setIsmr(0);//全部设为未选中
                    }
                    mDataAdapter.getDataList().get(position).setIsmr(1);
                    currentNum = position;
                }
                isSettingNormal(position);
                mDataAdapter.notifyDataSetChanged();
                mLuRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    /**
     * 删除地址
     *
     * @param aid
     */
    private void DeleteItemData(int aid) {
        NetWork.getService(ImpService.class)
                .DeleteAddressData(uid, String.valueOf(aid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteAddressBean>() {
                    private DeleteAddressBean delMyAreaBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(delMyAreaBean.getMsg());
                        if (delMyAreaBean.getCode()==0) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteAddressBean delMyAreaBean) {
                        this.delMyAreaBean = delMyAreaBean;
                    }
                });


    }

    /**
     * 是否设置默认
     *
     * @param position
     */
    private void isSettingNormal(int position) {
        NetWork.getService(ImpService.class)
                .setDefaultAddressData(uid, String.valueOf(mDataAdapter.getDataList().get(position).getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteAddressBean>() {
                    private DeleteAddressBean isTrueAddressBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(isTrueAddressBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteAddressBean isTrueAddressBean) {
                        this.isTrueAddressBean = isTrueAddressBean;
                    }
                });

    }

    private void initListener() {
        btn_submit.setOnClickListener(this);
        tv_new_add_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                startActivity(AddressManagerActivity.class);
                break;
            case R.id.tv_new_add_address:
                startActivity(AddressManagerActivity.class);
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
        private WeakReference<GetGoodsAddressActivity> ref;

        PreviewHandler(GetGoodsAddressActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GetGoodsAddressActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(getAddressListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(getAddressListBean.getData().size());
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

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getData() {
        NetWork.getService(ImpService.class)
                .GetAddressListData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAddressListBean>() {
                    private GetAddressListBean getAddressListBean;

                    @Override
                    public void onCompleted() {
                        if (getAddressListBean.getCode()==0) {
                            int size = getAddressListBean.getData().size();
                            if (size <= 0) {
                                ll_no_address.setVisibility(View.VISIBLE);
                                fl_have_address.setVisibility(View.GONE);
                            } else {
                                ll_no_address.setVisibility(View.GONE);
                                fl_have_address.setVisibility(View.VISIBLE);
                            }
                            showData(getAddressListBean);
                            List<GetAddressListBean.DataBean> data = getAddressListBean.getData();
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
                    public void onNext(GetAddressListBean getAddressListBean) {
                        this.getAddressListBean = getAddressListBean;
                    }
                });


    }

    private void showData(GetAddressListBean getAddressListBean) {
        this.getAddressListBean = getAddressListBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
