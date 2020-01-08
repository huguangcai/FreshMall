package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ViewPagerFragmentAdapter;
import com.ysxsoft.freshmall.fragment.Tab1ExchangeFragment;
import com.ysxsoft.freshmall.fragment.Tab2ExchangeFragment;
import com.ysxsoft.freshmall.fragment.Tab3ExchangeFragment;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;

import java.util.ArrayList;

/**
 * Create By 胡
 * on 2020/1/7 0007
 */
public class ExchangedActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("已兑换");
        initTitle();
    }

    private void initTitle() {
        tabLayout = getViewById(R.id.tabLayout);
        viewPager = getViewById(R.id.viewPager);
        tabLayout.removeAllTabs();
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        titles.add("待发货");
        titles.add("待收货");
        titles.add("已完成");
        fragments.add(new Tab1ExchangeFragment());
        fragments.add(new Tab2ExchangeFragment());
        fragments.add(new Tab3ExchangeFragment());
        initViewPager(fragments,titles);
        initTabLayout(titles);
    }

    private void initTabLayout(ArrayList<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab =tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.view_tab);
            TextView textView = tab.getCustomView().findViewById(R.id.tab);
            textView.setWidth(AppUtil.getScreenWidth(mContext) * 1 / 4);
            textView.setText(titles.get(i));
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.btn_color));
                textView.setTextSize(17);
            } else {
                textView.setTextColor(getResources().getColor(R.color.color_989898));
                textView.setTextSize(15);
            }
        }
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(17);
            tv.setTextColor(getResources().getColor(R.color.btn_color));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getCustomView() == null) {
                return;
            }
            TextView tv = tab.getCustomView().findViewById(R.id.tab);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.color_989898));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void initViewPager(ArrayList<Fragment> fragments, ArrayList<String> titles) {
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragments, titles));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public int getLayout() {
        return R.layout.activity_exchange_layout;
    }
}
