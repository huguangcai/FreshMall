package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OGoodsFragmentAdapter;
import com.ysxsoft.freshmall.adapter.O2OShopeListAdapter;
import com.ysxsoft.freshmall.dialog.ShopeCarDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;
import com.ysxsoft.freshmall.modle.ShopCarListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.O2OMallDetailActivity;
import com.ysxsoft.freshmall.view.O2OOrderCheckActivity;
import com.ysxsoft.freshmall.widget.MyRecyclerView;
import com.ysxsoft.freshmall.widget.banner.GlideImageLoader;

import java.io.Serializable;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * O2O商家详情  商品的Fragment
 */
public class O2OGoodsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, O2OGoodsFragmentAdapter.ModifyCountInterface, View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private O2OGoodsFragmentAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler();
    private MallDetailBean.DataBean data;
    private double totalPrice = 0.00;// 购买的商品总价
    private TextView tv_sum_num, tv_sum_money, tv_shop_car_state, tv_go_buy;
    private ImageView img_is_shop_car;
    private LinearLayout ll_go_buy;
    private String mallId, latitude, longitude;
    private List<O2OShopeCarListBean.DataBean> listBeanData;
    private int num = 0;
    private int number = 0;
    private int miuscurrentCount;
    private int isAddcurrentCount;

    @Override
    protected void initData() {
        Bundle  arguments = getArguments();
        if (arguments != null) {
            latitude = arguments.getString("latitude");
            longitude = arguments.getString("longitude");
            mallId = arguments.getString("mallId");
        }
        tv_sum_num = getViewById(R.id.tv_sum_num);
        tv_sum_money = getViewById(R.id.tv_sum_money);
        tv_shop_car_state = getViewById(R.id.tv_shop_car_state);
        tv_go_buy = getViewById(R.id.tv_go_buy);
        ll_go_buy = getViewById(R.id.ll_go_buy);
        img_is_shop_car = getViewById(R.id.img_is_shop_car);
        tv_sum_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"0".equals(tv_sum_num.getText().toString())) {
                    tv_shop_car_state.setVisibility(View.GONE);
                    tv_sum_money.setVisibility(View.VISIBLE);
                    img_is_shop_car.setBackgroundResource(R.mipmap.img_join_shop_car);
                    tv_go_buy.setBackgroundResource(R.color.btn_color);
                    tv_go_buy.setEnabled(true);
                } else {
                    tv_sum_money.setVisibility(View.GONE);
                    tv_shop_car_state.setVisibility(View.VISIBLE);
                    img_is_shop_car.setBackgroundResource(R.mipmap.img_shop_car_no_select);
                    tv_go_buy.setBackgroundResource(R.color.hint_text_color);
                    tv_go_buy.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) getViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) getViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new O2OGoodsFragmentAdapter(getActivity());
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
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");

    }

    @Override
    protected void initListenser() {
        mDataAdapter.setModifyCountInterface(this);
        img_is_shop_car.setOnClickListener(this);
        tv_go_buy.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.o2o_goods_layout;
    }

    @Override
    public void onRefresh() {
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

    @Override
    public void doIncrease(int position, final View showCountView) {
        final MallDetailBean.DataBean.SplistBean splistBean = mDataAdapter.getDataList().get(position);
        int isAddBackcurrentCount = Integer.valueOf(splistBean.getGwc());
        isAddBackcurrentCount++;
        splistBean.setGwc(isAddBackcurrentCount);
        ((TextView) showCountView).setText(isAddBackcurrentCount + "");
        tv_sum_num.setText(isAddBackcurrentCount + "");
        statistics();
        SubmitData("2", splistBean.getId(), isAddBackcurrentCount + "", mallId);

    }

    @Override
    public void doDecrease(int position, View showCountView) {
        final MallDetailBean.DataBean.SplistBean splistBean = mDataAdapter.getDataList().get(position);
        int isMinusBackcurrentCount = Integer.valueOf(splistBean.getGwc());
        if (isMinusBackcurrentCount == -1||isMinusBackcurrentCount==0) {
            return;
        }
        isMinusBackcurrentCount--;
        splistBean.setGwc(isMinusBackcurrentCount);
        ((TextView) showCountView).setText(isMinusBackcurrentCount + "");
        tv_sum_num.setText(isMinusBackcurrentCount + "");
        statistics();
        SubmitData("2", splistBean.getId(), isMinusBackcurrentCount + "", mallId);
    }

    private void SubmitData(String type, String id, String shuliang, String did) {
        String uid = SpUtils.getSp(getActivity(), "uid");
        NetWork.getService(ImpService.class)
                .O2OShopeCarData(type, id, shuliang, uid, did)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(commonBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });
    }

    private void statistics() {
        totalPrice = 0.00;
        number=0;
        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
            MallDetailBean.DataBean.SplistBean splistBean = mDataAdapter.getDataList().get(i);
            totalPrice += Double.valueOf(splistBean.getSpjiage()) * Double.valueOf(splistBean.getGwc());
            number+=Integer.valueOf(splistBean.getGwc());
        }
        tv_sum_money.setText(totalPrice + "");
        tv_sum_num.setText(number+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_is_shop_car:
                if ("0".equals(tv_sum_num.getText().toString())) {
                    showToastMessage("购物车不能为空");
                    return;
                }
                NetWork.getService(ImpService.class)
                        .O2OShopeCarList(SpUtils.getSp(getActivity(), "uid"), mallId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<O2OShopeCarListBean>() {
                            private O2OShopeCarListBean o2OShopeCarListBean;

                            @Override
                            public void onCompleted() {
                                if (o2OShopeCarListBean.getCode() == 0) {
                                    listBeanData = o2OShopeCarListBean.getData();

                                    final ShopeCarDialog dialog = new ShopeCarDialog(getActivity());
                                    TextView tv_clean_shop_car = dialog.findViewById(R.id.tv_clean_shop_car);
                                    MyRecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
                                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(manager);
                                    final O2OShopeListAdapter adapter = new O2OShopeListAdapter(getContext(), listBeanData);
                                    recyclerView.setAdapter(adapter);
                                    adapter.setOnModifyListener(new O2OShopeListAdapter.OnModifyListener() {
                                        private boolean isMinus = false;

                                        @Override
                                        public void MiusClick(int position, View showCountView) {
                                            final O2OShopeCarListBean.DataBean dataBean = adapter.listBeanData.get(position);
                                            miuscurrentCount = Integer.valueOf(((TextView) showCountView).getText().toString());
                                            if (miuscurrentCount == -1||miuscurrentCount==0) {
                                                return;
                                            }
                                            miuscurrentCount--;
                                            ((TextView) showCountView).setText(String.valueOf(miuscurrentCount));
                                            tv_sum_num.setText(miuscurrentCount + "");
                                            SubmitData("3", dataBean.getId(), miuscurrentCount + "", mallId);
                                            onRefresh();
                                        }

                                        @Override
                                        public void AddClick(int position, View showCountView) {
                                            final O2OShopeCarListBean.DataBean dataBean = adapter.listBeanData.get(position);
                                            isAddcurrentCount = Integer.valueOf(((TextView) showCountView).getText().toString());
                                            isAddcurrentCount++;
                                            ((TextView) showCountView).setText(String.valueOf(isAddcurrentCount));
                                            tv_sum_num.setText(isAddcurrentCount + "");
                                            SubmitData("3", dataBean.getId(), isAddcurrentCount + "", mallId);
                                            onRefresh();
                                        }
                                    });

                                    tv_clean_shop_car.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            tv_sum_money.setVisibility(View.GONE);
                                            tv_shop_car_state.setVisibility(View.VISIBLE);
                                            img_is_shop_car.setBackgroundResource(R.mipmap.img_shop_car_no_select);
                                            tv_go_buy.setBackgroundResource(R.color.hint_text_color);
                                            tv_go_buy.setEnabled(false);
                                            SubmitData("1", null, null, mallId);
                                            onRefresh();
                                        }
                                    });
                                    dialog.show();

                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(O2OShopeCarListBean o2OShopeCarListBean) {

                                this.o2OShopeCarListBean = o2OShopeCarListBean;
                            }
                        });
                break;
            case R.id.tv_go_buy:
                Intent intent=new Intent(getActivity(),O2OOrderCheckActivity.class);
                intent.putExtra("mallId",mallId);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
                break;
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
                        mRecyclerView.refreshComplete(data.getSplist().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(data.getSplist().size());
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
                .MallDetailData(SpUtils.getSp(getActivity(), "uid"), mallId, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallDetailBean>() {
                    private MallDetailBean mallDetailBean;

                    @Override
                    public void onCompleted() {
                        if (mallDetailBean.getCode() == 0) {
                            data = mallDetailBean.getData();

                            totalPrice = 0.00;
                            num = 0;
                            for (int i = 0; i < data.getSplist().size(); i++) {
                                num += Integer.valueOf(data.getSplist().get(i).getGwc());
                                totalPrice += Double.valueOf(data.getSplist().get(i).getSpjiage()) * Double.valueOf(data.getSplist().get(i).getGwc());
                            }
                            tv_sum_money.setText(totalPrice + "");
                            tv_sum_num.setText(num + "");


                            mDataAdapter.addAll(data.getSplist());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.getSplist().size());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MallDetailBean mallDetailBean) {

                        this.mallDetailBean = mallDetailBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
