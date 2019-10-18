package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.freshmall.MainActivity;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.LoginBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private EditText ed_user, ed_pwd;
    private CircleImageView img_logo;
    private TextView tv_login, tv_forget_pwd, tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFitSystemWindow(true);
        setStatusBarFullTransparent();
        initView();
        initListener();
    }

    @Override
    public int getLayout() {
        return R.layout.login_layout;
    }

    private void initView() {
        img_logo = getViewById(R.id.img_logo);
        ed_user = getViewById(R.id.ed_user);
        ed_pwd = getViewById(R.id.ed_pwd);
        tv_login = getViewById(R.id.tv_login);
        tv_forget_pwd = getViewById(R.id.tv_forget_pwd);
        tv_register = getViewById(R.id.tv_register);
        img_logo.setImageBitmap(AppUtil.getLogoBitmap(mContext));
    }

    private void initListener() {
        tv_login.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                if (checkData()) return;
                LoginData();
                break;
            case R.id.tv_forget_pwd:
                startActivity(ForgetActivity.class);
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
        }
    }

    /**
     * 登录
     */
    private void LoginData() {
        NetWork.getService(ImpService.class)
                .Login(ed_user.getText().toString().trim(),
                        ed_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    private LoginBean loginBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(loginBean.getMsg());
                        if (loginBean.getCode() == 0) {
                            if ("1".equals(loginBean.getData().getUsertype())) {
                                if (loginBean.getData().getShstate() != null) {
                                    switch (loginBean.getData().getShstate()) {
                                        case "0":
                                            showToastMessage("正在审核");
                                            break;
                                        case "1":
                                            startActivity(MainActivity.class);
                                            SpUtils.saveSp(mContext, "uid", loginBean.getData().getId());
                                            SpUtils.saveSp(mContext, "useType", loginBean.getData().getUsertype());
                                            SpUtils.saveSp(mContext, "vip", loginBean.getData().getIsvip());
                                            finish();
                                            break;
                                        case "2":
                                            showToastMessage("审核未通过");
                                            break;
                                        case "3":
                                            Intent intent = new Intent(mContext, RegisterBusnessActivity.class);
                                            intent.putExtra("uid", loginBean.getData().getId());
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            }else {
                                startActivity(MainActivity.class);
                                SpUtils.saveSp(mContext, "uid", loginBean.getData().getId());
                                SpUtils.saveSp(mContext, "useType", loginBean.getData().getUsertype());
                                SpUtils.saveSp(mContext, "vip", loginBean.getData().getIsvip());
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {

                        this.loginBean = loginBean;
                    }
                });
    }

    /**
     * 核对数据
     *
     * @return
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_user.getText().toString().trim())) {
            showToastMessage("账号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_user.getText().toString().trim())) {
            showToastMessage("手机号输入不正确");
            return true;
        }
        if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
            showToastMessage("密码不能为空");
            return true;
        }
        if (ed_pwd.getText().toString().trim().length() < 6) {
            showToastMessage("密码不能少于六位");
            return true;
        }
        return false;
    }
}
