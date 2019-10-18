package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.AliPayBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.MoneyTextWatcher;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_money;
    private ImageView img_alipay, img_wechatpay, img_card_pay;
    private LinearLayout ll_alipay, ll_wechatpay, ll_card_pay;
    private Button btn_submit;
    private int type = 1;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        setBackVisibily();
        setTitle("余额充值");
        initView();
        initListener();
    }

    private void initView() {
        ed_money = getViewById(R.id.ed_money);
        ed_money.addTextChangedListener(new MoneyTextWatcher(ed_money));
        ll_alipay = getViewById(R.id.ll_alipay);
        img_alipay = getViewById(R.id.img_alipay);
        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
        ll_wechatpay = getViewById(R.id.ll_wechatpay);
        img_wechatpay = getViewById(R.id.img_wechatpay);
        ll_card_pay = getViewById(R.id.ll_card_pay);
        img_card_pay = getViewById(R.id.img_card_pay);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        ll_alipay.setOnClickListener(this);
        ll_wechatpay.setOnClickListener(this);
        ll_card_pay.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.recharge_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alipay:
                type = 1;
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_card_pay.setBackgroundResource(R.mipmap.img_normal_dui);
                break;
            case R.id.ll_wechatpay:
                type = 2;
                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                img_card_pay.setBackgroundResource(R.mipmap.img_normal_dui);
                break;
            case R.id.ll_card_pay:
                type = 3;
                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_card_pay.setBackgroundResource(R.mipmap.img_ok_dui);
                startActivity(RechargeAbleCardActivity.class);
                break;
            case R.id.btn_submit:
                if (type != 3) {
                    if (TextUtils.isEmpty(ed_money.getText().toString().trim())) {
                        showToastMessage("充值金额不能为空");
                        return;
                    }
                    if (Double.valueOf(ed_money.getText().toString().trim()) <= 0) {
                        showToastMessage("充值金额不能为0");
                        return;
                    }
                }
                switch (type) {
                    case 1:
                        AliPayData(SpUtils.getSp(mContext, "uid"), ed_money.getText().toString().trim());
                        break;
                    case 2:
                        WxChatPay(SpUtils.getSp(mContext, "uid"), ed_money.getText().toString().trim());
                        break;
                    case 3:
                        startActivity(RechargeAbleCardActivity.class);
                        break;
                }
                break;
        }
    }

    private void WxChatPay(String uid, String trim) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .WxChatRecharge(uid, trim)
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

    private void AliPayData(String uid, String trim) {
        NetWork.getService(ImpService.class)
                .AliPayRecharge(uid, trim)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AliPayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AliPayBean aliPayBean) {
                        if (aliPayBean.getCode() == 0) {
                            AliPay(aliPayBean.getData());
                        }
                    }
                });
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
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
                    break;
                }
            }
        }
    };

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
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
}
