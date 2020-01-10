package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.OrderCheckAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CheckMoneyBean;
import com.ysxsoft.freshmall.modle.CommentResponse;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.PackageDetailBean;
import com.ysxsoft.freshmall.modle.ShopAlipayBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.MyRecyclerView;
import com.ysxsoft.freshmall.widget.ReceiptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OrderCheckActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_name, tv_phone_num, tv_address, tv_minus, tv_youMoney, tv_free_shipping,
            tv_num, tv_add, tv_sum_num, tv_money, tv_pay, tv_no_address, tvReceipt;
    private EditText ed_mark;
    private LinearLayout ll_have_address;
    private String uid;
    private int payType = 1;
    private MyRecyclerView recyclerView;
    private PackageDetailBean detailBean;
    private int addressId;
    private StringBuffer sectionId = new StringBuffer();
    private String shopCardId;
    private TextView tv_check_pay;
    private IWXAPI api;
    private DecimalFormat decimalFormat;


    private String fptypes="0";
    private String dwname;
    private String nsrsbh;
    private String sprphone;
    private String spryx;
    private String fpneir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        detailBean = (PackageDetailBean) intent.getSerializableExtra("goods");
        setBackVisibily();
        setTitle("订单确认");
        initView();
        initListener();
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
                            addressId = getAddressListBean.getData().get(0).getId();
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
        tvReceipt = getViewById(R.id.tvReceipt);
        tv_free_shipping = getViewById(R.id.tv_free_shipping);
        tv_youMoney = getViewById(R.id.tv_youMoney);
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);
        recyclerView = getViewById(R.id.recyclerView);
        tv_minus = getViewById(R.id.tv_minus);
        tv_num = getViewById(R.id.tv_num);
        tv_add = getViewById(R.id.tv_add);
        ed_mark = getViewById(R.id.ed_mark);
        tv_sum_num = getViewById(R.id.tv_sum_num);
        tv_money = getViewById(R.id.tv_money);
        tv_pay = getViewById(R.id.tv_pay);
        ll_have_address = getViewById(R.id.ll_have_address);
        tv_no_address = getViewById(R.id.tv_no_address);


        tv_sum_num.setText(String.valueOf(detailBean.getDataList().size()));
        decimalFormat = new DecimalFormat("0.00");
//        tv_money.setText(decimalFormat.format(Double.valueOf(detailBean.getSum())));
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        OrderCheckAdapter adapter = new OrderCheckAdapter(mContext, detailBean.getDataList());
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        tv_minus.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        tv_no_address.setOnClickListener(this);
        ll_have_address.setOnClickListener(this);
        tvReceipt.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.order_check_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_have_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
            case R.id.tv_minus:
                if (tv_num.getText().toString().equals("1")) {
                    tv_num.setText(1 + "");
                } else {
                    tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) - 1));
                }
                break;
            case R.id.tv_add:
                tv_num.setText(String.valueOf(Integer.valueOf(tv_num.getText().toString()) + 1));
                break;
            case R.id.tv_pay:
                if ("0".equals(SpUtils.getSp(mContext, "useType"))) {
                    if ("0".equals(SpUtils.getSp(mContext, "vip"))) {
                        startActivity(MyVipActivity.class);
                        showToastMessage("请开通会员");
                        return;
                    }
                }
                if (addressId == 0) {
                    showToastMessage("地址不能为空");
                    return;
                }

//                if (TextUtils.isEmpty(sprphone)) {
//                    showToastMessage("发票不能为空");
//                    return;
//                }

                final O2OPayDialog dialog = new O2OPayDialog(mContext);
                LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                LinearLayout ll_balance_money = dialog.findViewById(R.id.ll_balance_money);
                ll_balance_money.setVisibility(View.VISIBLE);
                final ImageView img_balance_money = dialog.findViewById(R.id.img_balance_money);
                final TextView tv_sum_money = dialog.findViewById(R.id.tv_money);
                tv_sum_money.setText(tv_money.getText().toString().trim());
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
                                SubmitData();
                                break;
                            case 2:
//                                showToastMessage("微信充值");
//                                Intent intentId = new Intent(mContext, WaitFaHouActivity.class);
//                                startActivity(intentId);
                                Gson gson1 = new Gson();
                                String json1 = gson1.toJson(detailBean.getDataList());
                                if (sectionId.length() != 0) {
                                    sectionId.setLength(0);
                                }
                                for (int i = 0; i < detailBean.getDataList().size(); i++) {
                                    if (!TextUtils.isEmpty(detailBean.getDataList().get(i).getShopCarId()) || detailBean.getDataList().get(i).getShopCarId() != null) {
                                        sectionId.append(detailBean.getDataList().get(i).getShopCarId()).append(",");
                                    }
                                }
                                if (sectionId.length() != 0) {
                                    shopCardId = sectionId.deleteCharAt(sectionId.length() - 1).toString();
                                }
                                WxChatPay(String.valueOf(addressId), json1, shopCardId);

                                break;
                            case 3:
                                Gson gson = new Gson();
                                String json = gson.toJson(detailBean.getDataList());
                                if (sectionId.length() != 0) {
                                    sectionId.setLength(0);
                                }
                                for (int i = 0; i < detailBean.getDataList().size(); i++) {
                                    if (!TextUtils.isEmpty(detailBean.getDataList().get(i).getShopCarId()) || detailBean.getDataList().get(i).getShopCarId() != null) {
                                        sectionId.append(detailBean.getDataList().get(i).getShopCarId()).append(",");
                                    }
                                }
                                if (sectionId.length() != 0) {
                                    shopCardId = sectionId.deleteCharAt(sectionId.length() - 1).toString();
                                }
                                PayData(String.valueOf(addressId), json, shopCardId);
                                break;
                        }

                    }
                });
                dialog.show();
                break;
            case R.id.tv_no_address:
                startActivity(GetGoodsAddressActivity.class);
                break;

            case R.id.tvReceipt:
                ReceiptDialog receiptDialog = new ReceiptDialog(mContext);
                receiptDialog.setOnReceiptDialogListener(new ReceiptDialog.OnReceiptDialogListener() {
                    @Override
                    public void sure(String receiptType, String UnitName, String NSNum, String phone, String email, String receiptContent) {
                        fptypes = receiptType;
                        dwname = UnitName;
                        nsrsbh = NSNum;
                        sprphone = phone;
                        spryx = email;
                        fpneir = receiptContent;
                    }
                });
                receiptDialog.show();
                break;

        }
    }

    private void WxChatPay(String s, String json1, String shopCardId) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();

        PostFormBuilder post = OkHttpUtils.post()
                .url(ImpService.SHOP_WX_PAY)
                .addParams("uid", uid);

        switch (fptypes) {
            case "0":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json1);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                break;
            case "1":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json1);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
            case "2":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json1);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("dwname",dwname);
                post.addParams("nsrsbh",nsrsbh);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
        }
        post.tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag","微信==="+e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        WxPayBean wxPayBean = JsonUtils.parseByGson(response, WxPayBean.class);
                        if (wxPayBean!=null){
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
                                Intent intentId = new Intent(mContext, WaitFaHouActivity.class);
                                startActivity(intentId);
                            }
                        }

                    }
                });
//        if (TextUtils.equals(fptypes, "1")) {
//            NetWork.getService(ImpService.class)
//                    .WxChatPay1(SpUtils.getSp(mContext, "uid"), s, json1, shopCardId, fptypes, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<WxPayBean>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            showToastMessage(e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(WxPayBean wxPayBean) {
//                            if ("0".equals(wxPayBean.getCode())) {
//                                PayReq req = new PayReq();
//                                req.appId = wxPayBean.getData().getAppid();
//                                req.partnerId = wxPayBean.getData().getPartnerid();
//                                req.prepayId = wxPayBean.getData().getPrepayid();
//                                req.nonceStr = wxPayBean.getData().getNoncestr();
//                                req.timeStamp = String.valueOf(wxPayBean.getData().getTimestamp());
//                                req.packageValue = wxPayBean.getData().getPackageX();
//                                req.sign = wxPayBean.getData().getSign();
//                                req.extData = "app data"; // optional
////                            showToastMessage("正常调起支付");
//                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                                api.sendReq(req);
//                                wxPay.dismiss();
//                                Intent intentId = new Intent(mContext, WaitFaHouActivity.class);
//                                startActivity(intentId);
//                            }
//                        }
//                    });
//        } else {
//            NetWork.getService(ImpService.class)
//                    .WxChatPay(SpUtils.getSp(mContext, "uid"), s, json1, shopCardId, fptypes, dwname, nsrsbh, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<WxPayBean>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            showToastMessage(e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(WxPayBean wxPayBean) {
//                            if ("0".equals(wxPayBean.getCode())) {
//                                PayReq req = new PayReq();
//                                req.appId = wxPayBean.getData().getAppid();
//                                req.partnerId = wxPayBean.getData().getPartnerid();
//                                req.prepayId = wxPayBean.getData().getPrepayid();
//                                req.nonceStr = wxPayBean.getData().getNoncestr();
//                                req.timeStamp = String.valueOf(wxPayBean.getData().getTimestamp());
//                                req.packageValue = wxPayBean.getData().getPackageX();
//                                req.sign = wxPayBean.getData().getSign();
//                                req.extData = "app data"; // optional
////                            showToastMessage("正常调起支付");
//                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                                api.sendReq(req);
//                                wxPay.dismiss();
//                                Intent intentId = new Intent(mContext, WaitFaHouActivity.class);
//                                startActivity(intentId);
//                            }
//                        }
//                    });
//        }
    }

    private void PayData(String addressId, String json, String shopCardId) {
        PostFormBuilder post = OkHttpUtils.post()
                .url(ImpService.SHOP_YUE_PAY)
                .addParams("uid", uid);
        switch (fptypes) {
            case "0":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                break;
            case "1":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
            case "2":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("dwname",dwname);
                post.addParams("nsrsbh",nsrsbh);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
        }
        post.tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag","余额==="+e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonBean resp = JsonUtils.parseByGson(response, CommonBean.class);
                        if (resp!=null){
                            if (resp.getCode() == 0) {
                                finish();
                            }
                        }
                    }
                });
//        if (TextUtils.equals(fptypes, "1")) {
//            NetWork.getService(ImpService.class)
//                    .BalanceData1(SpUtils.getSp(mContext, "uid"), addressId, json, shopCardId, fptypes, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<CommonBean>() {
//                        private CommonBean commonBean;
//
//                        @Override
//                        public void onCompleted() {
//                            showToastMessage(commonBean.getMsg());
//                            if (commonBean.getCode() == 0) {
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(CommonBean commonBean) {
//
//                            this.commonBean = commonBean;
//                        }
//                    });
//        } else {
//            NetWork.getService(ImpService.class)
//                    .BalanceData(SpUtils.getSp(mContext, "uid"), addressId, json, shopCardId, fptypes, dwname, nsrsbh, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<CommonBean>() {
//                        private CommonBean commonBean;
//
//                        @Override
//                        public void onCompleted() {
//                            showToastMessage(commonBean.getMsg());
//                            if (commonBean.getCode() == 0) {
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(CommonBean commonBean) {
//
//                            this.commonBean = commonBean;
//                        }
//                    });
//        }

    }
    /*********************************支付宝支付***************************************/
    /**
     * 支付宝支付
     */
    private void SubmitData() {
        Gson gson = new Gson();
        String json = gson.toJson(detailBean.getDataList());
        if (sectionId.length() != 0) {
            sectionId.setLength(0);
        }
        for (int i = 0; i < detailBean.getDataList().size(); i++) {
            if (!TextUtils.isEmpty(detailBean.getDataList().get(i).getShopCarId()) || detailBean.getDataList().get(i).getShopCarId() != null) {
                sectionId.append(detailBean.getDataList().get(i).getShopCarId()).append(",");
            }
        }
        if (sectionId.length() != 0) {
            shopCardId = sectionId.deleteCharAt(sectionId.length() - 1).toString();
        }
        PostFormBuilder post = OkHttpUtils.post()
                .url(ImpService.SHOP_ALI_PAY)
                .addParams("uid", uid);

        switch (fptypes) {
            case "0":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                break;
            case "1":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
            case "2":
                post.addParams("aid",String.valueOf(addressId));
                post.addParams("goods",json);
                post.addParams("shopcar",shopCardId);
                post.addParams("fptypes",fptypes);
                post.addParams("dwname",dwname);
                post.addParams("nsrsbh",nsrsbh);
                post.addParams("sprphone",sprphone);
                post.addParams("spryx",spryx);
                post.addParams("fpneir",fpneir);
                break;
        }
        post.tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag","支付宝==="+e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ShopAlipayBean bean = JsonUtils.parseByGson(response, ShopAlipayBean.class);
                        if (bean!=null){
                            if (bean.getCode() == 0) {
                                AliPay(bean.getData());
                            }
                        }
                    }
                });

//        if (TextUtils.equals(fptypes, "1")) {
//            NetWork.getService(ImpService.class)
//                    .ShopAlipay1(this.uid, String.valueOf(addressId), json, shopCardId, fptypes, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<ShopAlipayBean>() {
//                        private ShopAlipayBean shopAlipayBean;
//
//                        @Override
//                        public void onCompleted() {
//                            if (shopAlipayBean.getCode() == 0) {
//                                AliPay(shopAlipayBean.getData());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                        showToastMessage(e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(ShopAlipayBean shopAlipayBean) {
//                            this.shopAlipayBean = shopAlipayBean;
//                        }
//                    });
//
//        } else {
//            NetWork.getService(ImpService.class)
//                    .ShopAlipay(this.uid, String.valueOf(addressId), json, shopCardId, fptypes, dwname, nsrsbh, sprphone, spryx, fpneir)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<ShopAlipayBean>() {
//                        private ShopAlipayBean shopAlipayBean;
//
//                        @Override
//                        public void onCompleted() {
//                            if (shopAlipayBean.getCode() == 0) {
//                                AliPay(shopAlipayBean.getData());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                        showToastMessage(e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(ShopAlipayBean shopAlipayBean) {
//                            this.shopAlipayBean = shopAlipayBean;
//                        }
//                    });
//        }
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

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderCheckActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        RequestAddressData();
        checkMoney();
    }

    private void checkMoney() {
        NetWork.getService(ImpService.class)
                .CheckMoney(String.valueOf(decimalFormat.format(Double.valueOf(detailBean.getSum()))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckMoneyBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CheckMoneyBean checkMoneyBean) {
                        if (checkMoneyBean.getCode() == 0) {
                            tv_money.setText(checkMoneyBean.getData().getZcount());
                            tv_youMoney.setText("¥" + checkMoneyBean.getData().getYfei());
                            tv_free_shipping.setText("(满" + checkMoneyBean.getData().getMbyou() + "包邮)");
                        }
                    }
                });

    }

    /******************************************************************************/

}
