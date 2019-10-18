package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.MoneyTextWatcher;

public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener {
    private EditText ed_money;
    private ImageView img_alipay, img_wechatpay;
    private LinearLayout ll_alipay, ll_wechatpay;
    private Button btn_submit;
    private TextView tv_balance_money, tv_title_right,tv_all_withdraw_cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("提现");
        setBackVisibily();
        initView();
        initListener();
    }

    private void initView() {
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("提现规则");
        ed_money = getViewById(R.id.ed_money);
        ed_money.addTextChangedListener(new MoneyTextWatcher(ed_money));
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_all_withdraw_cash = getViewById(R.id.tv_all_withdraw_cash);
        ll_alipay = getViewById(R.id.ll_alipay);
        img_alipay = getViewById(R.id.img_alipay);
        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
        ll_wechatpay = getViewById(R.id.ll_wechatpay);
        img_wechatpay = getViewById(R.id.img_wechatpay);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        tv_title_right.setOnClickListener(this);
        tv_all_withdraw_cash.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wechatpay.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.withdraw_cash_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                startActivity(WithdrawalRulesActivity.class);
                break;
            case R.id.tv_all_withdraw_cash:
                ed_money.setText(tv_balance_money.getText().toString().trim());
                break;
            case R.id.ll_alipay:
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                break;
            case R.id.ll_wechatpay:
                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_money.getText().toString().trim())) {
                    showToastMessage("提现金额不能为空");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) <= 0) {
                    showToastMessage("提现金额不能为0");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) > Double.valueOf(tv_balance_money.getText().toString().trim())) {
                    showToastMessage("输入金额不能大于账户余额");
                    return;
                }
                showToastMessage("提现");
                break;
        }
    }
}
