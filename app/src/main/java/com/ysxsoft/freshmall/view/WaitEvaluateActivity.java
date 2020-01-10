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
import com.ysxsoft.freshmall.adapter.WaitEvaluateItemAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WaitEvaluateActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_wait_evaluate, tv_kd_number, tv_kd_address, tv_name, tv_phone_num,
            tv_address, tv_content, tv_color, tv_size, tv_price, tv_goods_num, tv_apply,
            tv_goods_money, tv_yunfei, tv_order_sum_money, tv_order_number, tv_order_time,
            tv_pay_type, tv_look_logistics, tv_check_shouhou, tv_evaluate, tv_no_address;
    private ImageView img_tupian;
    private FrameLayout fl_arrow;
    private LinearLayout ll_have_address;
    private String uid;
    private double totalPrice = 0.00;// 购买的商品总价
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
        setTitle("待评价");
        requestData();
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
//                            ApplyRefundMoneyAdapter adapter = new ApplyRefundMoneyAdapter(mContext, data.getGoods());
//                            recyclerdata.setAdapter(adapter);
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

                            final WaitEvaluateItemAdapter adapter = new WaitEvaluateItemAdapter(mContext, data.getGoods());
                            recyclerdata.setAdapter(adapter);
                            adapter.setOnClickListener(new WaitEvaluateItemAdapter.OnClickListener() {
                                @Override
                                public void cancleClick(int position) {
//                                    showToastMessage("点击了哈哈哈哈");
                                }

                                @Override
                                public void checkClick(int position) {
                                    Intent intent1 = new Intent(mContext, EvaluateActivity.class);
                                    intent1.putExtra("goods_id",data.getGoods().get(position).getSid());
                                    intent1.putExtra("order_id",data.getGoods().get(position).getOrder_id());
                                    intent1.putExtra("mall", "mall");
                                    mContext.startActivity(intent1);
                                }
                            });

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
        tv_wait_evaluate = getViewById(R.id.tv_wait_evaluate);
        tv_kd_number = getViewById(R.id.tv_kd_number);
        tv_kd_address = getViewById(R.id.tv_kd_address);
        fl_arrow = getViewById(R.id.fl_arrow);
        tv_name = getViewById(R.id.tv_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_address = getViewById(R.id.tv_address);

        tv_apply = getViewById(R.id.tv_apply);
        tv_goods_money = getViewById(R.id.tv_goods_money);
        tv_yunfei = getViewById(R.id.tv_yunfei);
        tv_order_sum_money = getViewById(R.id.tv_order_sum_money);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_order_time = getViewById(R.id.tv_order_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_look_logistics = getViewById(R.id.tv_look_logistics);
        tv_evaluate = getViewById(R.id.tv_evaluate);
        ll_have_address = getViewById(R.id.ll_have_address);
        tv_no_address = getViewById(R.id.tv_no_address);
        recyclerdata = getViewById(R.id.recyclerdata);

//        tv_order_number.setText(pgApplyRefundBean.getOrderNum());
//        tv_order_time.setText(pgApplyRefundBean.getOrderTime());
//        switch (pgApplyRefundBean.getPayType()) {
//            case "1":
//                tv_pay_type.setText("支付宝支付");
//                break;
//            case "2":
//                tv_pay_type.setText("微信支付");
//                break;
//            case "3":
//                tv_pay_type.setText("余额支付");
//                break;
//        }
////        tv_apply_time.setText(pgApplyRefundBean.getPayTime());
//        totalPrice = 0.00;
//        for (int i = 0; i < pgApplyRefundBean.getDataBean().size(); i++) {
//            PgApplyRefundBean.DataBean dataBean = pgApplyRefundBean.getDataBean().get(i);
//            totalPrice += Double.valueOf(dataBean.getMoney()) * Double.valueOf(dataBean.getNumber());
//        }
//        tv_order_sum_money.setText(totalPrice + "");
//        tv_goods_money.setText(totalPrice + "");

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);


    }

    private void initListener() {
        fl_arrow.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        tv_look_logistics.setOnClickListener(this);
        tv_evaluate.setOnClickListener(this);
        tv_no_address.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.wait_evaluate_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_arrow:
                Intent intent2 = new Intent(mContext, LogisticsDetailActivity.class);
                intent2.putExtra("orderId",orderId);
                startActivity(intent2);
                break;
            case R.id.tv_apply:
                Intent intent = new Intent(mContext, AllOrderActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
                break;
            case R.id.tv_look_logistics:
                Intent intent3 = new Intent(mContext, LogisticsDetailActivity.class);
                intent3.putExtra("orderId",orderId);
                startActivity(intent3);
                break;
            case R.id.tv_evaluate:
//                startActivity(EvaluateActivity.class);
                Intent intent1 = new Intent(mContext, EvaluateActivity.class);
//                intent1.putExtra("goods_id","");
//                intent1.putExtra("order_id",pgApplyRefundBean.get);
                intent1.putExtra("mall", "mall");
                mContext.startActivity(intent1);
                break;
            case R.id.tv_no_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
        }
    }
}
