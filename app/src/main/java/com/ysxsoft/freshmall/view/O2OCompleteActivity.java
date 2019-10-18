package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OWaitPayAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OCompleteActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_wait_pay, tv_mall_name, tv_address_mall, tv_start_time, tv_end_time,
            tv_goods_num, tv_money,  tv_name, tv_distance, tv_address,
            tv_order_number, tv_time, tv_pay_type;
    private MyRecyclerView recyclerView;
    private LinearLayout ll_mall_info;
    private String order_id;
    private String latitude;
    private String longitude;
    private double totalPrice = 0.00;// 购买的商品总价
    private int sum = 0;//购买数量
    private O2OOederDetailBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        requestData();
        setBackVisibily();
        setTitle("已完成");
        intiView();
        initListener();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .O2OOederDetail(order_id,longitude,latitude)
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
                        showToastMessage(e.getMessage());
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
        ll_mall_info.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_complete_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

}
