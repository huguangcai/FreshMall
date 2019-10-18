package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ApplyRefundMoneyAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.modle.ShopAlipayBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WaitPayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_fahuo, tv_wait_time, tv_name, tv_phone_num, tv_address, tv_content, tv_color,
            tv_size, tv_price, tv_goods_money, tv_yunfei, tv_order_sum_money, tv_apply_time,
            tv_refund_number, tv_pay_type, tv_cancle_order, tv_pay, tv_no_address;
    private ImageView img_tupian;
    private LinearLayout ll_have_address;
    private String uid;
    private MyRecyclerView recyclerdata;
    private int payType = 1;
    private TextView tv_check_pay;
    private String orderId;
    private OrderDetailBean.DataBean data;
    private double totalPrice = 0.00;// 购买的商品总价
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        requestData();
        setBackVisibily();
        setTitle("待支付");
        initView();
        RequestAddressData();
        initListener();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .orderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderDetailBean>() {
                    private OrderDetailBean orderDetailBean;

                    @Override
                    public void onCompleted() {
                        if (orderDetailBean.getCode() == 0) {
                            data = orderDetailBean.getData();
                            ApplyRefundMoneyAdapter adapter = new ApplyRefundMoneyAdapter(mContext, data.getGoods());
                            recyclerdata.setAdapter(adapter);
                            totalPrice = 0.00;
                            for (int i = 0; i < data.getGoods().size(); i++) {
                                OrderDetailBean.DataBean.GoodsBean goodsBean = data.getGoods().get(i);
                                totalPrice += Double.valueOf(goodsBean.getMoney()) * Double.valueOf(goodsBean.getNum());
                            }
                            tv_refund_number.setText(data.getOrder_num());
                            tv_apply_time.setText(data.getPay_time());
                            switch (data.getPay_type()) {
                                case "1":
                                    tv_pay_type.setText("支付宝支付");
                                    break;
                                case "2":
                                    tv_pay_type.setText("微信支付");
                                    break;
                                case "3":
                                    tv_pay_type.setText("余额支付");
                                    break;
                            }
//                            tv_order_sum_money.setText(data.getOrder_money());
//                            tv_goods_money.setText(data.getOrder_money());
                            tv_order_sum_money.setText(totalPrice+"");
                            tv_goods_money.setText(totalPrice+"");

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderDetailBean orderDetailBean) {

                        this.orderDetailBean = orderDetailBean;
                    }
                });
    }

    private void RequestAddressData() {
        NetWork.getService(ImpService.class)
                .getDefaultAddressData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAddressListBean>() {
                    private GetAddressListBean getAddressListBean;

                    @Override
                    public void onCompleted() {
                        if (getAddressListBean.getCode() == 0) {
                            int size = getAddressListBean.getData().size();
                            if (size <= 0) {
                                tv_no_address.setVisibility(View.VISIBLE);
                                ll_have_address.setVisibility(View.GONE);
                            } else {
                                tv_no_address.setVisibility(View.GONE);
                                ll_have_address.setVisibility(View.VISIBLE);
                            }
                            tv_name.setText(getAddressListBean.getData().get(0).getShname());
                            tv_phone_num.setText(getAddressListBean.getData().get(0).getShphone());
                            tv_address.setText(getAddressListBean.getData().get(0).getShxxdz());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetAddressListBean getAddressListBean) {

                        this.getAddressListBean = getAddressListBean;
                    }
                });
    }

    private void initView() {
        tv_wait_fahuo = getViewById(R.id.tv_wait_fahuo);
        tv_wait_time = getViewById(R.id.tv_wait_time);
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);
        tv_goods_money = getViewById(R.id.tv_goods_money);
        tv_yunfei = getViewById(R.id.tv_yunfei);
        tv_order_sum_money = getViewById(R.id.tv_order_sum_money);
        tv_apply_time = getViewById(R.id.tv_apply_time);
        tv_refund_number = getViewById(R.id.tv_refund_number);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_cancle_order = getViewById(R.id.tv_cancle_order);
        tv_pay = getViewById(R.id.tv_pay);
        ll_have_address = getViewById(R.id.ll_have_address);
        tv_no_address = getViewById(R.id.tv_no_address);
        recyclerdata = getViewById(R.id.recyclerdata);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);

    }

    private void initListener() {
        tv_cancle_order.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        tv_no_address.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.wait_pay_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle_order:
                CancleOrder(data.getId());
                break;
            case R.id.tv_pay:
                final O2OPayDialog dialog = new O2OPayDialog(mContext);
                LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                LinearLayout ll_balance_money = dialog.findViewById(R.id.ll_balance_money);
                ll_balance_money.setVisibility(View.VISIBLE);
                final ImageView img_balance_money = dialog.findViewById(R.id.img_balance_money);
                TextView tv_money = dialog.findViewById(R.id.tv_money);
                tv_money.setText(/*tv_order_sum_money.getText().toString().trim()*/+totalPrice+"");
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

                        switch (payType) {
                            case 1:
                                tv_check_pay.setEnabled(false);
                                SubmitData(data.getId());
                                break;
                            case 2:
                                payType = 1;
                                WxChatPay(data.getId());
                                break;
                            case 3:
                                startActivity(RechargeAbleCardActivity.class);
                                payType = 1;
                                break;
                        }


                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.tv_no_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
        }
    }

    private void WxChatPay(String orderId) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
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
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ShopAlipayBean shopAlipayBean) {
                        this.shopAlipayBean = shopAlipayBean;
                    }
                });
    }

    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private android.os.Handler Handler = new Handler() {
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
                        Intent intent = new Intent(mContext, WaitFaHouActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
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
                PayTask alipay = new PayTask(WaitPayActivity.this);
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
                            finish();
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
}
