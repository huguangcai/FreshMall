package com.ysxsoft.freshmall.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.AllFragmentAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.modle.ShopAlipayBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.view.ApplyRefundMoneyActivity;
import com.ysxsoft.freshmall.view.CompleteActivity;
import com.ysxsoft.freshmall.view.LogisticsDetailActivity;
import com.ysxsoft.freshmall.view.RechargeAbleCardActivity;
import com.ysxsoft.freshmall.view.RefundDetialActivity;
import com.ysxsoft.freshmall.view.WaitEvaluateActivity;
import com.ysxsoft.freshmall.view.WaitFaHouActivity;
import com.ysxsoft.freshmall.view.WaitPayActivity;
import com.ysxsoft.freshmall.view.WaitShouHuoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 全部
 */
public class AllFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private AllFragmentAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler();
    private String uid;
    private OrderListBean orderListBean;
    private int payType = 1;
    private TextView tv_check_pay;
    private double totalPrice = 0.00;// 购买的商品总价
    private IWXAPI api;
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
                .setColorResource(R.color.transparent)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String orderId = dataBean.getId();
                switch (dataBean.getOrder_status()) {
                    case 1://1:待付款
                        Intent intent1 = new Intent(getActivity(), WaitPayActivity.class);
                        intent1.putExtra("orderId", orderId);
                        startActivity(intent1);
                        break;
                    case 2://待发货
                        Intent intent = new Intent(getActivity(), WaitFaHouActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        break;
                    case 3://待收货
                        Intent intent3 = new Intent(getActivity(), WaitShouHuoActivity.class);
                        intent3.putExtra("orderId", orderId);
                        startActivity(intent3);
                        break;
                    case 4://待评价
                        Intent intent4 = new Intent(getActivity(), WaitEvaluateActivity.class);
                        intent4.putExtra("orderId", orderId);
                        startActivity(intent4);
                        break;
                    case 5://已完成
                        Intent intent5 = new Intent(getActivity(), CompleteActivity.class);
                        intent5.putExtra("orderId", orderId);
                        startActivity(intent5);
                        break;
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
                        intent8.putExtra("type", "2");
                        startActivity(intent8);
                        break;
                    case 9://退款已完成
                        Intent intent9 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent9.putExtra("orderId", orderId);
                        intent9.putExtra("type", "3");
                        startActivity(intent9);
                        break;
                }
            }
        });

        mDataAdapter.setOnClickListener(new AllFragmentAdapter.OnClickListener() {
            @Override
            public void cancleClick(int position) {
                OrderListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String orderId = dataBean.getId();
                switch (dataBean.getOrder_status()) {
                    case 1://1:待付款
                        CancleOrder(orderId);
                        break;
                    case 2://待发货
                        Intent intent = new Intent(getActivity(), ApplyRefundMoneyActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        break;
                    case 3://待收货
                        Intent intent3 = new Intent(getActivity(), LogisticsDetailActivity.class);
                        intent3.putExtra("orderId", orderId);
                        startActivity(intent3);
                        break;
                    case 4://待评价
                        break;
                    case 5://已完成
                        break;
                    case 6://售后中
                        break;
                }
            }

            @Override
            public void checkClick(int position) {
                OrderListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                final String orderId = dataBean.getId();
                switch (dataBean.getOrder_status()) {
                    case 1://1:待付款
                        totalPrice = 0.00;
                        if (dataBean.getOrder_status() == 1) {
                            for (int i = 0; i < dataBean.getGoods().size(); i++) {
                                OrderListBean.DataBean.GoodsBean goodsBean = dataBean.getGoods().get(i);
                                totalPrice += Double.valueOf(goodsBean.getMoney()) * Double.valueOf(goodsBean.getNum());
                            }
                        }
                        final O2OPayDialog dialog = new O2OPayDialog(getActivity());
                        LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                        final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                        LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                        final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                        LinearLayout ll_balance_money = dialog.findViewById(R.id.ll_balance_money);
                        ll_balance_money.setVisibility(View.VISIBLE);
                        final ImageView img_balance_money = dialog.findViewById(R.id.img_balance_money);
                        final TextView tv_sum_money = dialog.findViewById(R.id.tv_money);
                        tv_sum_money.setText(totalPrice + "");
                        tv_check_pay = dialog.findViewById(R.id.tv_check_pay);
                        ll_alipay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payType = 1;
                                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                                img_balance_money.setBackgroundResource(R.mipmap.img_normal_dui);
                            }
                        });
                        ll_wechatpay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payType = 2;
                                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                                img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                                img_balance_money.setBackgroundResource(R.mipmap.img_normal_dui);
                            }
                        });
                        ll_balance_money.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payType = 3;
                                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                                img_balance_money.setBackgroundResource(R.mipmap.img_ok_dui);
                            }
                        });

                        tv_check_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                switch (payType) {
                                    case 1:
                                        tv_check_pay.setEnabled(false);
                                        SubmitData(orderId);
                                        break;
                                    case 2:
                                        payType = 1;
                                        WxChatPay(orderId);
                                        break;
                                    case 3:
                                        OrderPay(dialog,orderId);
                                        payType = 1;
                                        break;
                                }

                            }
                        });
                        dialog.show();
                        break;
                    case 2://待发货
                        TipFahuo(dataBean.getId());
                        break;
                    case 3://待收货
                        CheckShouHuo(dataBean.getId());
                        break;
                    case 4://待评价
                        break;
                    case 5://已完成
                        Intent intent5 = new Intent(getActivity(), CompleteActivity.class);
                        intent5.putExtra("orderId", orderId);
                        startActivity(intent5);
                        break;
                    case 6://售后
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
                        intent8.putExtra("type", "4");
                        startActivity(intent8);
                        break;

                    case 9://退款已完成
                        Intent intent9 = new Intent(getActivity(), RefundDetialActivity.class);
                        intent9.putExtra("orderId", orderId);
                        intent9.putExtra("type", "3");
                        startActivity(intent9);
                        break;
                }
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

    private void WxChatPay(String orderId) {
        final CustomDialog wxPay = new CustomDialog(getActivity(), "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .Orderwxpay(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WxPayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WxPayBean wxPayBean) {
                        if ("0".equals(wxPayBean.getCode())) {
                            PayReq req = new PayReq();
                            req.appId = wxPayBean.getData().getAppid();
                            req.partnerId = wxPayBean.getData().getPartnerid();
                            req.prepayId = wxPayBean.getData().getPrepayid();
                            req.nonceStr = wxPayBean.getData().getNoncestr();
                            req.timeStamp = String.valueOf(wxPayBean.getData().getTimestamp());
                            req.packageValue = wxPayBean.getData().getPackageX();
                            req.sign = wxPayBean.getData().getSign();
                            req.extData = "app data"; // optional
//                            showToastMessage("正常调起支付");
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                            wxPay.dismiss();
                        }


                    }
                });

    }

    /**
     * 余额支付
     * @param dialog
     * @param orderId
     */
    private void OrderPay(final O2OPayDialog dialog, String orderId) {
        NetWork.getService(ImpService.class)
                .OrderBalanceData(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        dialog.dismiss();
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

    private void TipFahuo(String id) {
        NetWork.getService(ImpService.class)
                .TipFaHuo(id, SpUtils.getSp(getActivity(), "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            onRefresh();
                        }
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

    /**
     * q确认收货
     *
     * @param id
     */
    private void CheckShouHuo(String id) {
        NetWork.getService(ImpService.class)
                .CheckShouHuo(id, SpUtils.getSp(getActivity(), "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            onRefresh();
                        }
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

    private void SubmitData(String orderId) {
        NetWork.getService(ImpService.class)
                .OrderAlipay(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopAlipayBean>() {
                    private ShopAlipayBean shopAlipayBean;

                    @Override
                    public void onCompleted() {
                        if (shopAlipayBean.getCode() == 0) {
                            AliPay(shopAlipayBean.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ShopAlipayBean shopAlipayBean) {
                        this.shopAlipayBean = shopAlipayBean;
                    }
                });


    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToastMessage("支付成功");

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
                    tv_check_pay.setEnabled(true);
                    break;
                }
            }
        }
    };

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                log(result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                Handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 取消订单
     * @param orderId
     */
    private void CancleOrder(String orderId) {
        NetWork.getService(ImpService.class)
                .delOrder(orderId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });
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
                .getOrderList(uid, "0", String.valueOf(page))
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
        api = WXAPIFactory.createWXAPI(getContext(), "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        uid = SpUtils.getSp(getContext(), "uid");
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
