package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;

public class O2OBuyScuessActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_right, tv_identity_code, tv_buy, tv_look_order_detial, tv_order_number, tv_time, tv_pay_type;
    private ImageView img_qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("");
        initView();
        initListener();
    }

    private void initView() {
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("完成");
        tv_title_right.setTextColor(mContext.getResources().getColor(R.color.btn_color));
        tv_identity_code = getViewById(R.id.tv_identity_code);
        img_qrCode = getViewById(R.id.img_qrCode);
        tv_buy = getViewById(R.id.tv_buy);
        tv_look_order_detial = getViewById(R.id.tv_look_order_detial);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_time = getViewById(R.id.tv_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
    }

    private void initListener() {
        tv_title_right.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        tv_look_order_detial.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_buy_scuess_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                finish();
                break;
            case R.id.tv_buy:
                startActivity(O2OMallDetailActivity.class);
                break;
            case R.id.tv_look_order_detial:
               startActivity(O2OWaitUseOrderActivity.class);
                break;
        }
    }
}
