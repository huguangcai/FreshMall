package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.RegisterButtomDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.RegisterBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CountDownTimeHelper;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.ActionN;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView img_logo;
    private EditText ed_user, ed_identifying_code, ed_pwd, ed_check_pwd, ed_invitation_code;
    private TextView tv_identifying_code, tv_part, tv_register, tv_immediately_login;
    private ImageView img_select_part;
    private int type = 0;//角色 0：普通用户 1：批发商
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFitSystemWindow(true);
        setStatusBarFullTransparent();
        initBdMap();
        initView();
        initListener();
    }

    @Override
    public int getLayout() {
        return R.layout.register_layout;
    }

    private void initView() {
        img_logo = getViewById(R.id.img_logo);
        ed_user = getViewById(R.id.ed_user);
        ed_identifying_code = getViewById(R.id.ed_identifying_code);
        tv_identifying_code = getViewById(R.id.tv_identifying_code);
        ed_pwd = getViewById(R.id.ed_pwd);
        ed_check_pwd = getViewById(R.id.ed_check_pwd);
        ed_invitation_code = getViewById(R.id.ed_invitation_code);
        tv_part = getViewById(R.id.tv_part);
        img_select_part = getViewById(R.id.img_select_part);
        tv_register = getViewById(R.id.tv_register);
        tv_immediately_login = getViewById(R.id.tv_immediately_login);
        img_logo.setImageBitmap(AppUtil.getLogoBitmap(mContext));
    }

    private void initListener() {
        tv_identifying_code.setOnClickListener(this);
        img_select_part.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_immediately_login.setOnClickListener(this);
        tv_part.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_identifying_code:
                if (TextUtils.isEmpty(ed_user.getText().toString().trim())) {
                    showToastMessage("账号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_user.getText().toString().trim())) {
                    showToastMessage("手机号输入不正确");
                    return;
                }
                CountDownTimeHelper count = new CountDownTimeHelper(60, tv_identifying_code);
                sendMessage(ed_user.getText().toString().trim());
                break;
            case R.id.tv_part:
                if (checkData()) return;
                final RegisterButtomDialog dialog = new RegisterButtomDialog(mContext);
                final TextView tv_person = dialog.findViewById(R.id.tv_person);
                tv_person.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 0;
                        dialog.dismiss();
                        tv_part.setText(tv_person.getText().toString());
                    }
                });
                final TextView tv_mall = dialog.findViewById(R.id.tv_mall);
                tv_mall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 1;
                        dialog.dismiss();
                        tv_part.setText(tv_mall.getText().toString());
                    }
                });
                dialog.show();
                break;
            case R.id.tv_register:
                if (checkData()) return;
                if (TextUtils.isEmpty(tv_part.getText().toString().trim())) {
                    showToastMessage("角色不能为空");
                    return;
                }
                submintData();
                break;
            case R.id.tv_immediately_login:
                finish();
                break;
        }
    }

    /**
     * 注册
     */
    private void submintData() {
        NetWork.getService(ImpService.class)
                .Register(ed_user.getText().toString().trim(),
                        ed_identifying_code.getText().toString().trim(),
                        ed_pwd.getText().toString().trim(),
                        String.valueOf(type),
                        ed_invitation_code.getText().toString().trim(),
                        city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterBean>() {
                    private RegisterBean registerBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(registerBean.getMsg());
                        if (registerBean.getCode() == 0) {
                            if (TextUtils.equals("批发商", tv_part.getText().toString())) {
                                Intent intent = new Intent(mContext, RegisterBusnessActivity.class);
                                intent.putExtra("uid", registerBean.getData());
                                intent.putExtra("city", city);
                                startActivity(intent);
                            } else {
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {

                        this.registerBean = registerBean;
                    }
                });

    }

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private void initBdMap() {
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setScanSpan(3000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            //获取城市
            city = location.getCity();
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(ed_user.getText().toString().trim())) {
            showToastMessage("账号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_user.getText().toString().trim())) {
            showToastMessage("手机号输入不正确");
            return true;
        }

        if (TextUtils.isEmpty(ed_identifying_code.getText().toString().trim())) {
            showToastMessage("验证码不能为空");
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
        if (TextUtils.isEmpty(ed_check_pwd.getText().toString().trim())) {
            showToastMessage("确认密码不能为空");
            return true;
        }
        if (!ed_pwd.getText().toString().trim().equals(ed_check_pwd.getText().toString().trim())) {
            showToastMessage("两次输入密码不一样");
            return true;
        }
        return false;
    }
}
