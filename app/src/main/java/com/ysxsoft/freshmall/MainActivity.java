package com.ysxsoft.freshmall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.luck.picture.lib.permissions.RxPermissions;
import com.ysxsoft.freshmall.fragment.GoodsFragment;
import com.ysxsoft.freshmall.fragment.HomeFragment;
import com.ysxsoft.freshmall.fragment.InfoFragment;
import com.ysxsoft.freshmall.fragment.MyFragment;
import com.ysxsoft.freshmall.fragment.O2OFragment;
import com.ysxsoft.freshmall.fragment.ShopCarFragment;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.LoginActivity;
import com.ysxsoft.freshmall.widget.MyViewPager;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private MyViewPager vp_content;
    private RadioGroup rg_home;
    private RadioButton rb_home, rb_o2o, rb_info, rb_shop_card, rb_my;
    private RxPermissions rxPermissions;
    private GoodsFragment homeFragment = new GoodsFragment();
    private O2OFragment o2OFragment = new O2OFragment();
    private InfoFragment infoFragment = new InfoFragment();
    private ShopCarFragment shopCarFragment = new ShopCarFragment();
    private MyFragment myFragment = new MyFragment();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String info;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        info = intent.getStringExtra("info");
        setHalfTransparent();
        setFitSystemWindow(false);
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            showToastMessage("允许了权限!");

                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToastMessage("未授权权限，部分功能不能使用");
                        }
                    }
                });
        initView();
        initData();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private void initView() {
        vp_content = getViewById(R.id.vp_content);
        rg_home = getViewById(R.id.rg_home);
        rb_home = getViewById(R.id.rb_home);
        rb_o2o = getViewById(R.id.rb_o2o);
        rb_info = getViewById(R.id.rb_info);
        rb_shop_card = getViewById(R.id.rb_shop_card);
        rb_my = getViewById(R.id.rb_my);

        if ("1".equals(SpUtils.getSp(mContext, "useType"))) {
            rb_o2o.setVisibility(View.GONE);
            vp_content.setOffscreenPageLimit(4);
        } else {
            rb_o2o.setVisibility(View.VISIBLE);
            vp_content.setOffscreenPageLimit(5);
        }

    }

    private void initData() {
        fragments.add(homeFragment);
        fragments.add(o2OFragment);
        fragments.add(infoFragment);
        fragments.add(shopCarFragment);
        fragments.add(myFragment);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);

        rg_home.check(R.id.rb_home);

        if ("info".equals(info)) {
            vp_content.setCurrentItem(2);
            rg_home.check(R.id.rb_info);
        } else {
            vp_content.setCurrentItem(0);
        }
        rg_home.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0);
                        break;
                    case R.id.rb_o2o:
                        vp_content.setCurrentItem(1);
                        break;
                    case R.id.rb_info:
                        vp_content.setCurrentItem(2);
                        break;
                    case R.id.rb_shop_card:
                        if (TextUtils.isEmpty(SpUtils.getSp(mContext, "uid")) || SpUtils.getSp(mContext, "uid") == null) {
                            startActivity(LoginActivity.class);
                            finish();
                        } else {
                            vp_content.setCurrentItem(3);
                        }
                        break;

                    case R.id.rb_my:
                        if (TextUtils.isEmpty(SpUtils.getSp(mContext, "uid")) || SpUtils.getSp(mContext, "uid") == null) {
                            startActivity(LoginActivity.class);
                            finish();
                        } else {
                            vp_content.setCurrentItem(4);
                        }
                        break;

                }
            }
        });
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    private boolean isBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBack) {
                finish();
            } else {
                showToastMessage("再按一次退出");
                isBack = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBack = false;
                    }
                }, 3000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
