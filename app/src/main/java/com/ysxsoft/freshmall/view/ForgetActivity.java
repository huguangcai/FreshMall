package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.RepasswordBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CountDownTimeHelper;
import com.ysxsoft.freshmall.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgetActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_phone_num, ed_identifying_code, ed_setting_login_pwd, ed_second_login_pwd;
    private TextView get_identifying_code;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("忘记密码");
        setBackVisibily();
        initView();
        initListener();
    }

    private void initView() {
        ed_phone_num = getViewById(R.id.ed_phone_num);
        ed_identifying_code = getViewById(R.id.ed_identifying_code);
        get_identifying_code = getViewById(R.id.get_identifying_code);
        ed_setting_login_pwd = getViewById(R.id.ed_setting_login_pwd);
        ed_second_login_pwd = getViewById(R.id.ed_second_login_pwd);
        btn_submit = getViewById(R.id.btn_submit);

    }

    private void initListener() {
        get_identifying_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.forget_password_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_identifying_code:
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号输入不正确");
                    return;
                }
                CountDownTimeHelper count = new CountDownTimeHelper(60, get_identifying_code);
                sendMessage(ed_phone_num.getText().toString().trim());
                break;
            case R.id.btn_submit:
                checkData();
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .Repassword(ed_phone_num.getText().toString().trim(),
                        ed_identifying_code.getText().toString().trim(),
                        ed_setting_login_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RepasswordBean>() {
                    private RepasswordBean repasswordBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(repasswordBean.getMsg());
                        if (repasswordBean.getCode() == 0) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(RepasswordBean repasswordBean) {

                        this.repasswordBean = repasswordBean;
                    }
                });
    }

    /**
     * 核对数据
     */
    private void checkData() {
        if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
            showToastMessage("手机号输入不正确");
            return;
        }
        if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_identifying_code.getText().toString().trim())) {
            showToastMessage("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(ed_setting_login_pwd.getText().toString().trim())) {
            showToastMessage("设置密码不能为空");
            return;
        }
        if (ed_setting_login_pwd.getText().toString().trim().length() < 6) {
            showToastMessage("密码不能少于六位数");
            return;
        }
        if (TextUtils.isEmpty(ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("确认密码不能为空");
            return;
        }
        if (!TextUtils.equals(ed_setting_login_pwd.getText().toString().trim(), ed_second_login_pwd.getText().toString().trim())) {
            showToastMessage("两次密码输入不一致");
            return;
        }
    }
}
