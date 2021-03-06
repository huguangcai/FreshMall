package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ApplyRefundMoneyAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WaitShouHuoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_shouhuo, tv_kd_number, tv_kd_address, tv_name, tv_phone_num,
            tv_address, tv_content, tv_color, tv_size, tv_price, tv_goods_num, tv_tui_huan,
            tv_goods_money, tv_yunfei, tv_order_sum_money, tv_order_number, tv_order_time,
            tv_pay_type, tv_look_logistics, tv_check_shouhou,tv_no_address;
    private ImageView img_tupian;
    private FrameLayout fl_arrow;
    private String uid;
    private LinearLayout ll_have_address;
    private MyRecyclerView recyclerdata;
    private String orderId;
    private OrderDetailBean.DataBean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        setBackVisibily();
        setTitle("待收货");
        initView();
        RequestAddressData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
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
                            if("6".equals(data.getOrder_status())){
                                //如果订单处于退款申请中
                                tv_tui_huan.setVisibility(View.GONE);
                            }
                            ApplyRefundMoneyAdapter adapter = new ApplyRefundMoneyAdapter(mContext, data.getGoods());
                            recyclerdata.setAdapter(adapter);

                            tv_order_number.setText(data.getOrder_num());
                            tv_order_time.setText(data.getPay_time());
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
                            tv_order_sum_money.setText(data.getOrder_money());
                            tv_goods_money.setText(data.getOrder_money());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
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
        tv_wait_shouhuo = getViewById(R.id.tv_wait_shouhuo);
        tv_kd_number = getViewById(R.id.tv_kd_number);
        tv_kd_address = getViewById(R.id.tv_kd_address);
        fl_arrow = getViewById(R.id.fl_arrow);
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);
        img_tupian = getViewById(R.id.img_tupian);
        tv_content = getViewById(R.id.tv_content);
        tv_color = getViewById(R.id.tv_color);
        tv_size = getViewById(R.id.tv_size);
        tv_price = getViewById(R.id.tv_price);
        tv_goods_num = getViewById(R.id.tv_goods_num);
        tv_tui_huan = getViewById(R.id.tv_tui_huan);
        tv_goods_money = getViewById(R.id.tv_goods_money);
        tv_yunfei = getViewById(R.id.tv_yunfei);
        tv_order_sum_money = getViewById(R.id.tv_order_sum_money);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_order_time = getViewById(R.id.tv_order_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_look_logistics = getViewById(R.id.tv_look_logistics);
        tv_check_shouhou = getViewById(R.id.tv_check_shouhou);
        ll_have_address = getViewById(R.id.ll_have_address);
        tv_no_address = getViewById(R.id.tv_no_address);

        recyclerdata = getViewById(R.id.recyclerdata);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);
    }

    private void initListener() {
        fl_arrow.setOnClickListener(this);
        tv_tui_huan.setOnClickListener(this);
        tv_look_logistics.setOnClickListener(this);
        tv_check_shouhou.setOnClickListener(this);
        tv_no_address.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.wait_shouhuo_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_arrow:
                Intent intent2 = new Intent(mContext, LogisticsDetailActivity.class);
                intent2.putExtra("orderId",orderId);
                startActivity(intent2);
                break;
            case R.id.tv_tui_huan:
                Intent intent=new Intent(mContext,ApplyRefundMoneyActivity.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
                break;
            case R.id.tv_look_logistics:
                Intent intent3 = new Intent(mContext, LogisticsDetailActivity.class);
                intent3.putExtra("orderId",orderId);
                startActivity(intent3);
                break;
            case R.id.tv_check_shouhou:
                CheckShouHuo(orderId);
                break;
            case R.id.tv_no_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
        }
    }

    private void CheckShouHuo(String orderId) {
        NetWork.getService(ImpService.class)
                .CheckShouHuo(orderId, uid)
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
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        this.commonBean = commonBean;
                    }
                });
    }
}
