package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.widget.segmenttablayout.SegmentTabLayout;
import com.ysxsoft.freshmall.widget.slidingtablayout.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends BaseFragment {


    private String[] mTitles_2 = {"商品", "批发"};
    private Fragment currentFragment = new Fragment();//（全局）
    private FrameLayout fl_content;
    private GoodsFragment goodsFragment=new GoodsFragment();
    private WholesaleFragment wholesaleFragment=new WholesaleFragment();
    @Override
    protected void initData() {
        fl_content = getViewById(R.id.fl_content);
        SegmentTabLayout tabLayout_2 = getViewById(R.id.tl_2);
        tabLayout_2.setTabData(mTitles_2);
        tabLayout_2.setVisibility(View.GONE);
        switchFragment(goodsFragment).commit();
        tabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position){
                    case 0:
                        switchFragment(goodsFragment).commit();
                        break;
                    case 1:
                        switchFragment(wholesaleFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_layout;
    }


    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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

    @Override
    public void onResume() {
        super.onResume();
        Intent intent=new Intent("IS_GONE");
        intent.putExtra("gone","0");
        getActivity().sendBroadcast(intent);
    }
}
