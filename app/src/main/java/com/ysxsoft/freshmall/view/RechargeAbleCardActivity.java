package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RechargeAbleCardActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_card_num;
    private EditText ed_pwd;
    private Button btn_submit;
    private String pay, addressId, json, shopCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("充值卡充值");
        setBackVisibily();
        initView();
        initListener();
    }

    private void initView() {
        ed_card_num = getViewById(R.id.ed_card_num);
        ed_pwd = getViewById(R.id.ed_pwd);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.rechargeable_card_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_card_num.getText().toString().trim())) {
                    showToastMessage("充值卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
                    showToastMessage("密码不能为空");
                    return;
                }
                submitData();
                break;
        }
    }


    private void submitData() {
        NetWork.getService(ImpService.class)
                .RechagerCarData(SpUtils.getSp(mContext, "uid"), ed_card_num.getText().toString().trim(), ed_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            finish();
                        }
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
