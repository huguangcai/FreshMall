package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

public class O2OWaitEvaluateActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_pay, tv_mall_name, tv_address_mall, tv_start_time, tv_end_time,
            tv_goods_num, tv_money, tv_cancle_order, tv_evaluate, tv_name, tv_distance, tv_address,
            tv_order_number, tv_time, tv_pay_type;
    private MyRecyclerView recyclerView;
    private LinearLayout ll_mall_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("待评价");
        intiView();
        initListener();
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
//        tv_cancle_order = getViewById(R.id.tv_cancle_order);
        tv_evaluate = getViewById(R.id.tv_evaluate);
        ll_mall_info = getViewById(R.id.ll_mall_info);
        tv_name = getViewById(R.id.tv_name);
        tv_distance = getViewById(R.id.tv_distance);
        tv_address = getViewById(R.id.tv_address);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_time = getViewById(R.id.tv_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
    }

    private void initListener() {
//        tv_cancle_order.setOnClickListener(this);
        tv_evaluate.setOnClickListener(this);
        ll_mall_info.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_wait_evaluate_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_cancle_order:
//                showToastMessage("取消订单");
//                break;
            case R.id.tv_evaluate:
                startActivity(EvaluateActivity.class);
                break;
            case R.id.ll_mall_info:
                Intent intent=new Intent(mContext,O2OMallDetailActivity.class);
                intent.putExtra("shangjia","shangjia");
                startActivity(intent);
                break;

        }
    }
}
