package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ApplyRefundMoneyAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By 胡
 * on 2020/1/8 0008
 * 发票
 */
public class ReceiptActivity extends BaseActivity {

    private String orderId;
    private TextView tvReceiptType;
    private TextView tvUnit;
    private TextView tvUnitName;
    private TextView tvNsNum;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvReceiptContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        setBackVisibily();
        setTitle("发票");
        initView();
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
                            String fptypes = orderDetailBean.getData().getFptypes();
                            switch (fptypes) {
                                case "0":
                                    tvUnit.setText("无");
                                    break;
                                case "1":
                                    tvUnit.setText("个人");
                                    break;
                                case "2":
                                    tvUnit.setText("单位");
                                    break;

                            }
                            tvUnitName.setText("单位名称："+orderDetailBean.getData().getDwname());
                            tvNsNum.setText("纳税人识别号："+orderDetailBean.getData().getNsrsbh());
                            tvPhone.setText("收票人手机*："+orderDetailBean.getData().getSprphone());
                            tvEmail.setText("收票人邮箱："+orderDetailBean.getData().getSpryx());
                            switch (orderDetailBean.getData().getFpneir()){
                                case "0":
                                    tvReceiptContent.setText("商品明细");
                                    break;
                                case "1":
                                    tvReceiptContent.setText("商品类别");
                                    break;
                            }
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

    private void initView() {
        tvReceiptType = getViewById(R.id.tvReceiptType);
        tvUnit = getViewById(R.id.tvUnit);
        tvUnitName = getViewById(R.id.tvUnitName);
        tvNsNum = getViewById(R.id.tvNsNum);
        tvPhone = getViewById(R.id.tvPhone);
        tvEmail = getViewById(R.id.tvEmail);
        tvReceiptContent = getViewById(R.id.tvReceiptContent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_receipt;
    }
}
