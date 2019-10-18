package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.fragment.AllFragment;
import com.ysxsoft.freshmall.fragment.AllOrderFragment;
import com.ysxsoft.freshmall.fragment.ReturnGoodsFragment;
import com.ysxsoft.freshmall.fragment.WaitEvaluateFragment;
import com.ysxsoft.freshmall.fragment.WaitFaHuoFragment;
import com.ysxsoft.freshmall.fragment.WaitGetGoodsFragment;
import com.ysxsoft.freshmall.fragment.WaitPayFragment;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.widget.slidingtablayout.OnTabSelectListener;
import com.ysxsoft.freshmall.widget.slidingtablayout.SlidingTabLayout;
import com.ysxsoft.freshmall.widget.slidingtablayout.ViewFindUtils;

import java.util.ArrayList;

public class AllOrderActivity extends BaseActivity {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Fragment currentFragment = new Fragment();//（全局）
    private String[] mTitles = new String[]{"全部", "待支付", "待发货", "待收货", "待评价", "退款/售后"};
    private ViewPager vp_content;
    private FrameLayout fl_content;
    private MyPagerAdapter mAdapter;
    private AllFragment allFragment = new AllFragment();
    private WaitPayFragment waitPayFragment = new WaitPayFragment();
    private WaitGetGoodsFragment waitGetGoodsFragment = new WaitGetGoodsFragment();
    private WaitEvaluateFragment waitEvaluateFragment = new WaitEvaluateFragment();
    private WaitFaHuoFragment waitFaHuoFragment = new WaitFaHuoFragment();
    private ReturnGoodsFragment returnGoodsFragment = new ReturnGoodsFragment();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        setBackVisibily();
        setTitle("我的订单");
        initView();
    }

    private void initView() {
        for (String titile : mTitles) {
            mFragments.add(new AllOrderFragment());
        }
        View decorView = getWindow().getDecorView();
        vp_content = ViewFindUtils.find(decorView, R.id.vp_content);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(mAdapter);

        SlidingTabLayout stl_tab = ViewFindUtils.find(decorView, R.id.stl_tab);
        stl_tab.setViewPager(vp_content);
        if (type!=null&& !TextUtils.isEmpty(type)) {
            switch (type) {
                case "1":
                    stl_tab.setCurrentTab(1);
                    switchFragment(waitPayFragment).commit();
                    break;
                case "2":
                    stl_tab.setCurrentTab(2);
                    switchFragment(waitFaHuoFragment).commit();
                    break;
                case "3":
                    stl_tab.setCurrentTab(3);
                    switchFragment(waitGetGoodsFragment).commit();
                    break;
                case "4":
                    stl_tab.setCurrentTab(4);
                    switchFragment(waitEvaluateFragment).commit();
                    break;
                case "5":
                    stl_tab.setCurrentTab(5);
                    switchFragment(returnGoodsFragment).commit();
                    break;
            }
        }else {
            switchFragment(allFragment).commit();
        }
        stl_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0://全部
                        switchFragment(allFragment).commit();
                        break;
                    case 1://待支付
                        switchFragment(waitPayFragment).commit();
                        break;
                    case 2://待发货
                        switchFragment(waitFaHuoFragment).commit();
                        break;
                    case 3://待收货
                        switchFragment(waitGetGoodsFragment).commit();
                        break;
                    case 4://待评价
                        switchFragment(waitEvaluateFragment).commit();
                        break;
                    case 5://退货/售后
                        switchFragment(returnGoodsFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.all_order_layout;
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
//第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
