package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ImgItemAdapter;
import com.ysxsoft.freshmall.adapter.RefundDetialAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RefundDetialActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_refund_tip, tv_num, tv_time, tv_content, tv_color, tv_size, tv_price,
            tv_goods_num, tv_refund_type, tv_refund_reason, tv_refund_illustrate,
            tv_refund_money, tv_refund_num, tv_apply_time, tv_refund_number,
            tv_tv_logistisc_type, tv_logistics_number, tv_entering;
    private MyRecyclerView recyclerView;
    private ImageView img_tupian;
    private LinearLayout ll_is_gone;
    private FrameLayout fl_entering;
    private MyRecyclerView recyclerdata;
    private String orderId, exit_type;
    private int number = 0;
    private OrderDetailBean.DataBean data;
    private String type;// 1 申请退款 2、退款中 3、退款成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        type = intent.getStringExtra("type");
        exit_type = intent.getStringExtra("exit_type");
        setBackVisibily();
        setTitle("退款详情");
//        requestData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();//申请退款后 TODO:Sincerly
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
                            RefundDetialAdapter adapter = new RefundDetialAdapter(mContext, data.getGoods());
                            recyclerdata.setAdapter(adapter);
                            ImgItemAdapter itemAdapter = new ImgItemAdapter(mContext, data.getExit_pics());
                            recyclerView.setAdapter(itemAdapter);
                            if (data.getExit_type() != null) {
                                switch (data.getExit_type()) {
                                    case "1":
                                        if (type != null || !TextUtils.isEmpty(type)) {
                                            switch (type) {
                                                case "1":
                                                    tv_refund_tip.setText("仅退款 申请退款");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "2":
                                                    tv_refund_tip.setText("仅退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "3":
                                                    tv_refund_tip.setText("仅退款 退款成功");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "4":
                                                    tv_refund_tip.setText("仅退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "5":
                                                    tv_refund_tip.setText("仅退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;

                                            }
                                        }
                                        tv_refund_type.setText("仅退款");
                                        break;
                                    case "2":
                                        if (type != null || !TextUtils.isEmpty(type)) {
                                            switch (type) {
                                                case "1":
                                                    tv_refund_tip.setText("退货退款 申请退款");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "2":
                                                    tv_refund_tip.setText("退货退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.VISIBLE);
                                                    break;
                                                case "3":
                                                    tv_refund_tip.setText("退货退款 退款成功");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.VISIBLE);
                                                    break;
                                                case "4":
                                                    tv_refund_tip.setText("退货退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;

                                                case "5":
                                                    tv_refund_tip.setText("退货退款 退款中");
                                                    fl_entering.setVisibility(View.GONE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                                case "6":
                                                    tv_refund_tip.setText("退货退款 退款中");
                                                    fl_entering.setVisibility(View.VISIBLE);
                                                    ll_is_gone.setVisibility(View.GONE);
                                                    break;
                                            }
                                        }
                                        tv_refund_type.setText("退货退款");
                                        break;
                                }
                            }
                            tv_num.setText(data.getOrder_num());
                            tv_time.setText(data.getAdd_time());
                            tv_refund_reason.setText(data.getExit_title());
                            tv_refund_illustrate.setText(data.getExit_content());
                            tv_refund_money.setText(data.getOrder_money());
                            number = 0;
                            for (int i = 0; i < data.getGoods().size(); i++) {
                                number += Integer.valueOf(data.getGoods().get(i).getNum());
                            }
                            tv_refund_num.setText(String.valueOf(number));

                            tv_apply_time.setText(data.getExit_time());
                            tv_refund_number.setText(data.getExit_num());
                            if (data.getSend_wuliu() != null && !TextUtils.isEmpty(data.getSend_wuliu())) {
                                String[] split = data.getSend_wuliu().split("/");
                                tv_tv_logistisc_type.setText(split[0]);
                                tv_logistics_number.setText(split[1]);
                            }
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

    private void initView() {
        tv_refund_tip = getViewById(R.id.tv_refund_tip);
        tv_num = getViewById(R.id.tv_num);
        tv_time = getViewById(R.id.tv_time);
        tv_refund_type = getViewById(R.id.tv_refund_type);
        tv_refund_reason = getViewById(R.id.tv_refund_reason);
        tv_refund_illustrate = getViewById(R.id.tv_refund_illustrate);
        recyclerView = getViewById(R.id.recyclerView);
        tv_refund_money = getViewById(R.id.tv_refund_money);
        tv_refund_num = getViewById(R.id.tv_refund_num);
        tv_apply_time = getViewById(R.id.tv_apply_time);
        tv_refund_number = getViewById(R.id.tv_refund_number);
        ll_is_gone = getViewById(R.id.ll_is_gone);
        tv_tv_logistisc_type = getViewById(R.id.tv_tv_logistisc_type);
        tv_logistics_number = getViewById(R.id.tv_logistics_number);
        fl_entering = getViewById(R.id.fl_entering);
        tv_entering = getViewById(R.id.tv_entering);
        recyclerdata = getViewById(R.id.recyclerdata);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);


    }

    private void initListener() {
        tv_entering.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.refund_detail_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_entering:
                    Intent intent = new Intent(mContext, LogisticsInputActivity.class);
                    intent.putExtra("orderId", data.getId());
                    startActivity(intent);
                break;
        }
    }
}
