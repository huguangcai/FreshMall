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
import com.ysxsoft.freshmall.adapter.O2OWaitPayAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.modle.O2OAlipayBean;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OWaitPayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_pay, tv_mall_name, tv_address_mall, tv_start_time, tv_end_time,
            tv_goods_num, tv_money, tv_cancle_order, tv_pay, tv_name, tv_distance, tv_address,
            tv_order_number, tv_time, tv_pay_type;
    private MyRecyclerView recyclerView;
    private LinearLayout ll_mall_info;
    private String order_id;
    private String latitude;
    private String longitude;
    private double totalPrice = 0.00;// 购买的商品总价
    private int sum = 0;//购买数量
    private O2OOederDetailBean.DataBean data;
    private int payType = 1;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        requestData();
        setBackVisibily();
        setTitle("待付款");
        intiView();
        initListener();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .O2OOederDetail(order_id, longitude, latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OOederDetailBean>() {
                    private O2OOederDetailBean o2OOederDetailBean;

                    @Override
                    public void onCompleted() {
                        if (o2OOederDetailBean.getCode() == 0) {
                            data = o2OOederDetailBean.getData();
                            O2OWaitPayAdapter adapter = new O2OWaitPayAdapter(mContext, data.getSplist());
                            recyclerView.setAdapter(adapter);

                            totalPrice = 0.00;
                            sum = 0;
                            for (int i = 0; i < data.getSplist().size(); i++) {
                                O2OOederDetailBean.DataBean.SplistBean splistBean = data.getSplist().get(i);
                                totalPrice += Double.valueOf(splistBean.getSpjiage()) * Double.valueOf(splistBean.getSpshuliang());
                                sum += Integer.valueOf(splistBean.getSpshuliang());
                            }
                            tv_money.setText(totalPrice + "");
                            tv_mall_name.setText(data.getDname());
                            tv_start_time.setText(data.getYytime());
                            tv_goods_num.setText(String.valueOf(sum));
                            tv_name.setText(data.getDname());
                            tv_distance.setText(data.getShowjl());
                            tv_address.setText(data.getDpjwd_address());
                            tv_order_number.setText(data.getDdh());
                            tv_time.setText(data.getXxtime());
                            if (data.getZftype() != null) {
                                switch (data.getZftype()) {
                                    case "0":
                                        tv_pay_type.setText("支付宝");
                                        break;
                                    case "1":
                                        tv_pay_type.setText("微信");
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(O2OOederDetailBean o2OOederDetailBean) {

                        this.o2OOederDetailBean = o2OOederDetailBean;
                    }
                });

    }

    private void intiView() {
        tv_wait_pay = getViewById(R.id.tv_wait_pay);
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_address_mall = getViewById(R.id.tv_address_mall);
        tv_start_time = getViewById(R.id.tv_start_time);
        tv_end_time = getViewById(R.id.tv_end_time);
        recyclerView = getViewById(R.id.recyclerView);
        tv_goods_num = getViewById(R.id.tv_goods_num);
        tv_money = getViewById(R.id.tv_money);
        tv_cancle_order = getViewById(R.id.tv_cancle_order);
        tv_pay = getViewById(R.id.tv_pay);
        ll_mall_info = getViewById(R.id.ll_mall_info);
        tv_name = getViewById(R.id.tv_name);
        tv_distance = getViewById(R.id.tv_distance);
        tv_address = getViewById(R.id.tv_address);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_time = getViewById(R.id.tv_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
    }

    private void initListener() {
        tv_cancle_order.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        ll_mall_info.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_wait_pay_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle_order:
                CancleOrder(order_id);
                break;
            case R.id.tv_pay:
                final O2OPayDialog dialog = new O2OPayDialog(mContext);
                LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                TextView tv_money = dialog.findViewById(R.id.tv_money);
                tv_money.setText(/*tv_money.getText().toString().trim()*/totalPrice + "");
                TextView tv_check_pay = dialog.findViewById(R.id.tv_check_pay);
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
                                AliPayData(order_id);
                                break;
                            case 2:
                                WxChatPay(order_id);
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.ll_mall_info:
                Intent intent = new Intent(mContext, O2OMallDetailActivity.class);
                intent.putExtra("shangjia", "shangjia");
                intent.putExtra("mallId", data.getDid());
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                break;

        }
    }

    private void WxChatPay(String order_id) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .O2OWxChatPay(order_id)
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

    private void AliPayData(String orderInfo) {
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
                PayTask alipay = new PayTask(O2OWaitPayActivity.this);
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
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
//                    tv_check_pay.setEnabled(true);
                    break;
                }
            }
        }
    };
    private void CancleOrder(String id) {
        NetWork.getService(ImpService.class)
                .O2OCancleOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
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
