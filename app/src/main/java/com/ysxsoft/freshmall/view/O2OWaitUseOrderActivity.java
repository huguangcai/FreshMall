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
import com.ysxsoft.freshmall.adapter.O2OWaitPayAdapter;
import com.ysxsoft.freshmall.dialog.ShowQrCodeDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OWaitUseOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_mall_name, tv_address_mall, tv_start_time, tv_end_time, tv_num, tv_money,
            tv_refund_money, tv_second_buy, tv_name, tv_distance, tv_address, tv_order_number,
            tv_time, tv_pay_type, tv_scan_check;
    private FrameLayout fl_qrcode;
    private LinearLayout ll_mall_info;
    private MyRecyclerView recyclerView;
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
        setBackVisibily();
        setTitle("待使用订单");
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
                .O2OOederDetail(order_id,  longitude,latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OOederDetailBean>() {
                    private O2OOederDetailBean o2OOederDetailBean;

                    @Override
                    public void onCompleted() {
                        if (o2OOederDetailBean.getCode() == 0) {
                            data = o2OOederDetailBean.getData();
                            if("3".equals(data.getDdsates())){
                                //退款中
                                tv_refund_money.setVisibility(View.GONE);
                            }
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
                            tv_num.setText(String.valueOf(sum));
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
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(O2OOederDetailBean o2OOederDetailBean) {

                        this.o2OOederDetailBean = o2OOederDetailBean;
                    }
                });
    }

    private void initView() {
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_address_mall = getViewById(R.id.tv_address_mall);
        tv_start_time = getViewById(R.id.tv_start_time);
        tv_end_time = getViewById(R.id.tv_end_time);
        recyclerView = getViewById(R.id.recyclerView);
        fl_qrcode = getViewById(R.id.fl_qrcode);
        tv_num = getViewById(R.id.tv_num);
        tv_money = getViewById(R.id.tv_money);
        tv_refund_money = getViewById(R.id.tv_refund_money);
        tv_second_buy = getViewById(R.id.tv_second_buy);
        tv_name = getViewById(R.id.tv_name);
        tv_distance = getViewById(R.id.tv_distance);
        tv_address = getViewById(R.id.tv_address);
        ll_mall_info = getViewById(R.id.ll_mall_info);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_time = getViewById(R.id.tv_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_scan_check = getViewById(R.id.tv_scan_check);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
    }

    private void initListener() {
        fl_qrcode.setOnClickListener(this);
        tv_refund_money.setOnClickListener(this);
        tv_second_buy.setOnClickListener(this);
        ll_mall_info.setOnClickListener(this);
        tv_scan_check.setOnClickListener(this);

    }

    @Override
    public int getLayout() {
        return R.layout.o2o_wait_use_order_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refund_money:
                Intent refundintent=new Intent(mContext,O2OApplyRefundMoneyActivity.class);
                refundintent.putExtra("order_id",order_id);
                refundintent.putExtra("latitude",String.valueOf(latitude));
                refundintent.putExtra("longitude",String.valueOf(longitude));
                startActivity(refundintent);
                break;
            case R.id.tv_second_buy:
                Intent intent = new Intent(mContext, O2OMallDetailActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("mallId", data.getDid());
                startActivity(intent);
                break;
            case R.id.fl_qrcode:
                Intent intent1=new Intent(mContext,O2OMallDetailActivity.class);
                intent1.putExtra("shangjia","shangjia");
                intent1.putExtra("latitude", String.valueOf(latitude));
                intent1.putExtra("longitude", String.valueOf(longitude));
                intent1.putExtra("mallId", data.getDid());
                startActivity(intent1);
                break;
            case R.id.ll_mall_info:
                Intent intent2=new Intent(mContext,O2OMallDetailActivity.class);
                intent2.putExtra("shangjia","shangjia");
                intent2.putExtra("latitude", String.valueOf(latitude));
                intent2.putExtra("longitude", String.valueOf(longitude));
                intent2.putExtra("mallId", data.getDid());
                startActivity(intent2);
                break;
            case R.id.tv_scan_check:
                ShowQrCodeDialog dialog=new ShowQrCodeDialog(mContext);
                ImageView img_qrCode = dialog.findViewById(R.id.img_qrCode);
                ImageLoadUtil.GlideGoodsImageLoad(mContext,data.getZfewm(),img_qrCode);
                TextView tv_phone_num = dialog.findViewById(R.id.tv_phone_num);
                tv_phone_num.setText(data.getYzms());
                dialog.show();
                break;
        }
    }
}
