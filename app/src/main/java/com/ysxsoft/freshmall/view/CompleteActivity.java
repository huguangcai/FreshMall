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
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ApplyRefundMoneyAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.modle.ShopAlipayBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompleteActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_fahuo, tv_wait_time, tv_name, tv_phone_num, tv_address, tv_content, tv_color, tv_yfei,
            tv_size, tv_price, tv_goods_money, tv_yunfei, tv_order_sum_money, tv_apply_time,
            tv_refund_number, tv_pay_type, tv_no_address;
    private ImageView img_tupian;
    private LinearLayout ll_have_address;
    private String uid;
    private MyRecyclerView recyclerdata;
    private int payType = 1;
    private TextView tv_check_pay;
    private String orderId;
    private OrderDetailBean.DataBean data;
    private double totalPrice = 0.00;// 购买的商品总价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        requestData();
        setBackVisibily();
        setTitle("已完成");
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
//                            tv_yfei.setText(data.getYfei());
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
                            tv_order_sum_money.setText(totalPrice+"");
                            tv_goods_money.setText(totalPrice+"");

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
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
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetAddressListBean getAddressListBean) {

                        this.getAddressListBean = getAddressListBean;
                    }
                });
    }

    private void initView() {
        tv_yfei = getViewById(R.id.tv_yfei);
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
        ll_have_address = getViewById(R.id.ll_have_address);
        tv_no_address = getViewById(R.id.tv_no_address);
        recyclerdata = getViewById(R.id.recyclerdata);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);

    }

    private void initListener() {
        tv_no_address.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.complete_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
        }
    }
}
