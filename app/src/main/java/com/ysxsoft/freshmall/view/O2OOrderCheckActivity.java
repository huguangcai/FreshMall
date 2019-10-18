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
import com.ysxsoft.freshmall.adapter.O2OOrderCheckAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.modle.O2OAlipayBean;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;
import com.ysxsoft.freshmall.modle.ScOrderDataBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OOrderCheckActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_mall_name, tv_address_mall, tv_start_time, tv_end_time, tv_is_youhui, tv_money, tv_sum_money, tv_pay;
    private MyRecyclerView recyclerView;
    private String mallId, latitude, longitude;
    private double totalPrice = 0.00;// 购买的商品总价
    private IWXAPI api;
    private int payType = 1;
    private TextView tv_check_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        Intent intent = getIntent();
        mallId = intent.getStringExtra("mallId");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        setBackVisibily();
        setTitle("确认订单");
        requestData();
        resquestMall();
        initView();
        initListener();
    }

    private void resquestMall() {
        NetWork.getService(ImpService.class)
                .MallDetailData(SpUtils.getSp(mContext, "uid"), mallId, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallDetailBean>() {
                    private MallDetailBean mallDetailBean;

                    @Override
                    public void onCompleted() {
                        if (mallDetailBean.getCode() == 0) {
                            MallDetailBean.DataBean data = mallDetailBean.getData();
                            tv_mall_name.setText(data.getDname());
                            tv_start_time.setText(data.getYytime());
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

    private void requestData() {
        NetWork.getService(ImpService.class)
                .O2OShopeCarList(SpUtils.getSp(mContext, "uid"), mallId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OShopeCarListBean>() {
                    private O2OShopeCarListBean o2OShopeCarListBean;

                    @Override
                    public void onCompleted() {
                        if (o2OShopeCarListBean.getCode() == 0) {
                            List<O2OShopeCarListBean.DataBean> listBeanData = o2OShopeCarListBean.getData();
                            final O2OOrderCheckAdapter adapter = new O2OOrderCheckAdapter(mContext, listBeanData);
                            recyclerView.setAdapter(adapter);

                            totalPrice = 0.00;
                            for (int i = 0; i < listBeanData.size(); i++) {
                                O2OShopeCarListBean.DataBean dataBean = listBeanData.get(i);
                                totalPrice += Double.valueOf(dataBean.getSpjiage()) * Double.valueOf(dataBean.getSliang());
                            }
                            tv_sum_money.setText(totalPrice + "");
                            tv_money.setText(totalPrice + "");
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


    }

    private void initView() {
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_address_mall = getViewById(R.id.tv_address_mall);
        tv_start_time = getViewById(R.id.tv_start_time);
        tv_end_time = getViewById(R.id.tv_end_time);
        recyclerView = getViewById(R.id.recyclerView);
        tv_is_youhui = getViewById(R.id.tv_is_youhui);
        tv_money = getViewById(R.id.tv_money);
        tv_sum_money = getViewById(R.id.tv_sum_money);
        tv_pay = getViewById(R.id.tv_pay);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
    }

    private void initListener() {
        tv_is_youhui.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_order_check_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_is_youhui:
                showToastMessage("暂无优惠");
                break;
            case R.id.tv_pay:
                if ("0".equals(SpUtils.getSp(mContext,"useType"))){
                    if ("0".equals(SpUtils.getSp(mContext, "vip"))) {
                        showToastMessage("请开通会员");
                        startActivity(MyVipActivity.class);
                        return;
                    }
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .ScOrderData(mallId, SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ScOrderDataBean>() {
                    private ScOrderDataBean scOrderDataBean;

                    @Override
                    public void onCompleted() {
                        if (scOrderDataBean.getCode() == 0) {
                            final String data = scOrderDataBean.getData();
                            final O2OPayDialog dialog = new O2OPayDialog(mContext);
                            LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                            final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                            img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                            LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                            final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                            TextView tv_money = dialog.findViewById(R.id.tv_money);
                            tv_money.setText(tv_sum_money.getText().toString().trim());
                            tv_check_pay = dialog.findViewById(R.id.tv_check_pay);
                            ll_alipay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    payType = 1;
                                    img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                                    img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                                }
                            });
                            ll_wechatpay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    payType = 2;
                                    img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                                    img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                                }
                            });
                            tv_check_pay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    switch (payType) {
                                        case 1:
                                            tv_check_pay.setEnabled(false);
                                            AlipayData(data);
                                            break;
                                        case 2:
                                            WxChatPay(data);
                                            break;
                                    }
                                }
                            });
                            dialog.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ScOrderDataBean scOrderDataBean) {

                        this.scOrderDataBean = scOrderDataBean;
                    }
                });

    }

    private void WxChatPay(String data) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .O2OWxChatPay(data)
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

    private void AlipayData(String orderInfo) {
        NetWork.getService(ImpService.class)
                .O2OAlipay(orderInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OAlipayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(O2OAlipayBean o2OAlipayBean) {
                        if (o2OAlipayBean.getCode()==0){
                            AliPay(o2OAlipayBean.getData());
                        }
                    }
                });
    }

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(O2OOrderCheckActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                log(result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
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
                        finish();
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
}
