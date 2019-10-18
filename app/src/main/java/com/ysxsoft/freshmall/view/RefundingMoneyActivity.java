package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OWaitPayAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RefundingMoneyActivity extends BaseActivity {

    private TextView tv_refunding_money,tv_order_number,tv_order_time,tv_mall_name,
            tv_goods_num,tv_money,tv_refund_type,tv_refund_reason,tv_refund_illustrate,
            tv_refund_money,tv_refund_num,tv_apply_time,tv_refund_number;
    private CircleImageView img_tupian;
    private String order_id;
    private String latitude;
    private String longitude,ok;
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
        ok = intent.getStringExtra("ok");
        requestData();
        setBackVisibily();
        if ("ok".equals(ok)){
            setTitle("退款完成");
        }else {
            setTitle("退款中");
        }
        initView();

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
                            ImageLoadUtil.GlideGoodsImageLoad(mContext,data.getImgfm(),img_tupian);
                            totalPrice = 0.00;
                            sum = 0;
                            for (int i = 0; i < data.getSplist().size(); i++) {
                                O2OOederDetailBean.DataBean.SplistBean splistBean = data.getSplist().get(i);
                                totalPrice += Double.valueOf(splistBean.getSpjiage()) * Double.valueOf(splistBean.getSpshuliang());
                                sum += Integer.valueOf(splistBean.getSpshuliang());
                            }
                            tv_money.setText(totalPrice + "");
                            tv_refund_money.setText(totalPrice + "");
                            tv_mall_name.setText(data.getDname());
                            tv_goods_num.setText(String.valueOf(sum));
                            tv_refund_num.setText(String.valueOf(sum));
                            tv_order_number.setText(data.getDdh());
                            tv_order_time.setText(data.getXxtime());
                            tv_refund_number.setText(data.getDdh());
                            tv_refund_reason.setText(data.getTkyuanyin());
                            tv_refund_illustrate.setText(data.getTkshuomign());
                            tv_apply_time.setText(data.getTktime());
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
        tv_refunding_money = getViewById(R.id.tv_refunding_money);
        if ("ok".equals(ok)){
            tv_refunding_money.setText("退款完成");
        }else {
            tv_refunding_money.setText("退款中");
        }
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_order_time = getViewById(R.id.tv_order_time);
        img_tupian = getViewById(R.id.img_tupian);
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_goods_num = getViewById(R.id.tv_goods_num);
        tv_money = getViewById(R.id.tv_money);
        tv_refund_type = getViewById(R.id.tv_refund_type);
        tv_refund_reason = getViewById(R.id.tv_refund_reason);
        tv_refund_illustrate = getViewById(R.id.tv_refund_illustrate);
        tv_refund_money = getViewById(R.id.tv_refund_money);
        tv_refund_num = getViewById(R.id.tv_refund_num);
        tv_apply_time = getViewById(R.id.tv_apply_time);
        tv_refund_number = getViewById(R.id.tv_refund_number);
    }

    @Override
    public int getLayout() {
        return R.layout.refunding_money_layout;
    }
}
