package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OWaitPayAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OApplyRefundMoneyActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView img_tupian;
    private TextView tv_mall_name, tv_num, tv_money, tv_refund_money, ed_refund_reason,
            tv_order_number, tv_order_time, tv_pay_type, tv_pay_time;
    private FrameLayout fl_arrow;
    private EditText ed_refund_illustrate;
    private Button btn_submit;
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
        setTitle("申请退款");
        initView();
        initListener();
    }
    private void requestData() {
        NetWork.getService(ImpService.class)
                .O2OOederDetail(order_id, longitude,latitude)
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
                            tv_num.setText(String.valueOf(sum));
                            tv_order_number.setText(data.getDdh());
                            tv_order_time.setText(data.getXxtime());
                            tv_pay_time.setText(data.getZftime());
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

    private void initView() {
        img_tupian = getViewById(R.id.img_tupian);
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_num = getViewById(R.id.tv_num);
        tv_money = getViewById(R.id.tv_money);
        tv_refund_money = getViewById(R.id.tv_refund_money);
        ed_refund_reason = getViewById(R.id.ed_refund_reason);
        fl_arrow = getViewById(R.id.fl_arrow);
        ed_refund_illustrate = getViewById(R.id.ed_refund_illustrate);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_order_time = getViewById(R.id.tv_order_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_pay_time = getViewById(R.id.tv_pay_time);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        fl_arrow.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_apply_refund_money_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_arrow:
                showToastMessage("选择退款原因");
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_refund_reason.getText().toString().trim())){
                    showToastMessage("退款原因不能为空");
                    return;
                }
                 if (TextUtils.isEmpty(ed_refund_illustrate.getText().toString().trim())){
                    showToastMessage("退款说明不能为空");
                    return;
                }
                showToastMessage("提交O2O退款申请");
                submitData();
                startActivity(RefundingMoneyActivity.class);
                finish();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .ApplyRefundMoney(order_id,tv_refund_money.getText().toString().trim(),ed_refund_illustrate.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });


    }
}
