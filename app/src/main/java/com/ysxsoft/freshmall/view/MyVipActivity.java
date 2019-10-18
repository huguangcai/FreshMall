package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.AliPayBean;
import com.ysxsoft.freshmall.modle.VipDataBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyVipActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_no_vip, ll_yes_vip, ll_alipay, ll_wechatpay;
    private TextView tv_one_month_money, tv_two_month_money, tv_three_month_money, tv_time,
            tv_vip_day, tv_end_time;
    private ImageView img_alipay, img_wechatpay;
    private Button btn_submit;
    private VipDataBean.DataBean data;
    private int payType = 1;
    private int type = 1;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        setHalfTransparent();
        setFitSystemWindow(false);
        setBackVisibily();
        int stateBar = getStateBar();
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .VipData(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VipDataBean>() {
                    private VipDataBean vipDataBean;

                    @Override
                    public void onCompleted() {
                        if (vipDataBean.getCode() == 0) {
                            data = vipDataBean.getData();
                            String isvip = vipDataBean.getData().getIsvip();
                            if ("0".equals(isvip) || isvip == null) {
                                ll_no_vip.setVisibility(View.VISIBLE);
                                ll_yes_vip.setVisibility(View.GONE);
                            } else {
                                ll_no_vip.setVisibility(View.GONE);
                                ll_yes_vip.setVisibility(View.VISIBLE);
                            }
                            tv_one_month_money.setText("¥" + vipDataBean.getData().getHuiyuan1());
                            tv_two_month_money.setText("¥" + vipDataBean.getData().getHuiyuan6());
                            tv_three_month_money.setText("¥" + vipDataBean.getData().getHuiyuan12());
                            tv_time.setText(vipDataBean.getData().getViptotime());
                            tv_end_time.setText(vipDataBean.getData().getViptotime());
//                            if (vipDataBean.getData().getViptotime()!=null) {
//                                tv_time.setText(vipDataBean.getData().getViptotime());
//                            }else {
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.add(Calendar.MONTH, 1);//time，是用户充值的月数
//                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                                Date date = calendar.getTime();
//                                String format1 = format.format(date);
//                                tv_time.setText(format1);
//                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(VipDataBean vipDataBean) {

                        this.vipDataBean = vipDataBean;
                    }
                });
    }

    private void initView() {
        ll_no_vip = getViewById(R.id.ll_no_vip);
        ll_yes_vip = getViewById(R.id.ll_yes_vip);
        tv_vip_day = getViewById(R.id.tv_vip_day);
        tv_end_time = getViewById(R.id.tv_end_time);
        tv_one_month_money = getViewById(R.id.tv_one_month_money);
        tv_one_month_money.setBackgroundResource(R.drawable.theme_color_shape);
        tv_two_month_money = getViewById(R.id.tv_two_month_money);
        tv_three_month_money = getViewById(R.id.tv_three_month_money);
        tv_time = getViewById(R.id.tv_time);
        ll_alipay = getViewById(R.id.ll_alipay);
        img_alipay = getViewById(R.id.img_alipay);
        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
        ll_wechatpay = getViewById(R.id.ll_wechatpay);
        img_wechatpay = getViewById(R.id.img_wechatpay);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        tv_one_month_money.setOnClickListener(this);
        tv_two_month_money.setOnClickListener(this);
        tv_three_month_money.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wechatpay.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.my_vip_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one_month_money:
                type = 1;
                tv_one_month_money.setBackgroundResource(R.drawable.theme_color_shape);
                tv_one_month_money.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_two_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_two_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_three_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_three_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 1);//time，是用户充值的月数
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = calendar.getTime();
                String format1 = format.format(date);
//                tv_time.setText(format1);
                break;
            case R.id.tv_two_month_money:
                type = 6;
                tv_one_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_one_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_two_month_money.setBackgroundResource(R.drawable.theme_color_shape);
                tv_two_month_money.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_three_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_three_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.add(Calendar.MONTH, 6);//time，是用户充值的月数
                SimpleDateFormat format12 = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = calendar1.getTime();
                String format34 = format12.format(date1);
//                tv_time.setText(format34);
                break;
            case R.id.tv_three_month_money:
                type = 12;
                tv_one_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_one_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_two_month_money.setBackgroundResource(R.drawable.theme_color_strocke_shape);
                tv_two_month_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_three_month_money.setBackgroundResource(R.drawable.theme_color_shape);
                tv_three_month_money.setTextColor(mContext.getResources().getColor(R.color.white));
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.MONTH, 12);//time，是用户充值的月数
                SimpleDateFormat format13 = new SimpleDateFormat("yyyy-MM-dd");
                Date date3 = calendar2.getTime();
                String format35 = format13.format(date3);
//                tv_time.setText(format35);
                break;
            case R.id.ll_alipay:
                payType = 1;
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                break;
            case R.id.ll_wechatpay:
                payType = 2;
                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                break;
            case R.id.btn_submit:
                switch (payType) {
                    case 1:
                        AliPayData();
                        break;
                    case 2:
                       WxChatData();
                        break;
                }
                break;
        }
    }

    private void WxChatData() {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .WxChatVip(SpUtils.getSp(mContext,"uid"),String.valueOf(type))
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

    private void AliPayData() {
        NetWork.getService(ImpService.class)
                .AliPayVip(SpUtils.getSp(mContext,"uid"),String.valueOf(type))
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
                            if (aliPayBean.getCode()==0){
                                AliPay(aliPayBean.getData());
                            }
                    }
                });

    }

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(MyVipActivity.this);
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
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
                    break;
                }
            }
        }
    };
}
