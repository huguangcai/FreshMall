package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.fragment.AllOrderFragment;
import com.ysxsoft.freshmall.fragment.O2OEvaluateFragment;
import com.ysxsoft.freshmall.fragment.O2OGoodsFragment;
import com.ysxsoft.freshmall.fragment.O2OMallFragment;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.CircleImageView;
import com.ysxsoft.freshmall.widget.banner.Banner;
import com.ysxsoft.freshmall.widget.banner.GlideImageLoader;
import com.ysxsoft.freshmall.widget.banner.OnBannerListener;
import com.ysxsoft.freshmall.widget.slidingtablayout.OnTabSelectListener;
import com.ysxsoft.freshmall.widget.slidingtablayout.SlidingTabLayout;
import com.ysxsoft.freshmall.widget.slidingtablayout.ViewFindUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OMallDetailActivity extends BaseActivity implements View.OnClickListener,OnBannerListener {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Fragment currentFragment = new Fragment();//（全局）
    private CheckBox cb_title_right;
    private Banner vp_banner;
    private CircleImageView img_tupian;
    private TextView tv_mall_name, tv_distance, tv_pingfen, tv_yuexiao, tv_fangwen, tv_gengxin,
            tv_sum_num, tv_shop_car_state, tv_sum_money, tv_go_buy;
    private LinearLayout ll_gengxin;
    private FrameLayout fl_content;
    private O2OGoodsFragment goodsFragment = new O2OGoodsFragment();
    private O2OEvaluateFragment evaluateFragment = new O2OEvaluateFragment();
    private O2OMallFragment mallFragment = new O2OMallFragment();
    private String[] mTitles=new String[]{"商品", "评价", "商家"};
    private ViewPager vp_content;
    private MyPagerAdapter mAdapter;
    private String shangjia,latitude,longitude,mallId;
    private ArrayList<String> imgs=new ArrayList<>();
    private MallDetailBean.DataBean data;
    private SlidingTabLayout stl_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        shangjia = intent.getStringExtra("shangjia");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        mallId = intent.getStringExtra("mallId");
        setBackVisibily();
        intiView();
        requestData();
    }

    private void intiView() {
        cb_title_right = getViewById(R.id.cb_title_right);
        vp_banner = getViewById(R.id.vp_banner);
        img_tupian = getViewById(R.id.img_tupian);
        tv_mall_name = getViewById(R.id.tv_mall_name);
        tv_distance = getViewById(R.id.tv_distance);
        tv_pingfen = getViewById(R.id.tv_pingfen);
        tv_yuexiao = getViewById(R.id.tv_yuexiao);
        tv_fangwen = getViewById(R.id.tv_fangwen);
        ll_gengxin = getViewById(R.id.ll_gengxin);
        tv_gengxin = getViewById(R.id.tv_gengxin);
        fl_content = getViewById(R.id.fl_content);
        tv_sum_num = getViewById(R.id.tv_sum_num);
        tv_shop_car_state = getViewById(R.id.tv_shop_car_state);
        tv_sum_money = getViewById(R.id.tv_sum_money);
        tv_go_buy = getViewById(R.id.tv_go_buy);

        cb_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_title_right.isChecked()) {
                    SelectOrCancle();
                } else {
                    SelectOrCancle();
                }
            }
        });
        for (String titile : mTitles) {
            mFragments.add(new AllOrderFragment());
        }
        View decorView = getWindow().getDecorView();
        vp_content = ViewFindUtils.find(decorView, R.id.vp_content);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(mAdapter);
        stl_tab = ViewFindUtils.find(decorView, R.id.stl_tab);
        stl_tab.setViewPager(vp_content);


        stl_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        ll_gengxin.setVisibility(View.GONE);
                        switchFragment(goodsFragment).commit();
                        Bundle bundle=new Bundle();
                        bundle.putString("latitude",latitude);
                        bundle.putString("longitude",longitude);
                        bundle.putString("mallId",mallId);
                        goodsFragment.setArguments(bundle);
                        break;
                    case 1:
                        ll_gengxin.setVisibility(View.GONE);
                        switchFragment(evaluateFragment).commit();
                        Bundle bundle1=new Bundle();
                        bundle1.putString("latitude",latitude);
                        bundle1.putString("longitude",longitude);
                        bundle1.putString("mallId",mallId);
                        evaluateFragment.setArguments(bundle1);
                        break;
                    case 2:
                        ll_gengxin.setVisibility(View.GONE);
                        switchFragment(mallFragment).commit();
                        Bundle bundle2=new Bundle();
                        bundle2.putString("latitude",latitude);
                        bundle2.putString("longitude",longitude);
                        bundle2.putString("mallId",mallId);
                        mallFragment.setArguments(bundle2);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void SelectOrCancle() {
        NetWork.getService(ImpService.class)
                .CancleCollectData(SpUtils.getSp(mContext,"uid"),mallId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
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

    private void requestData() {
        NetWork.getService(ImpService.class)
                .MallDetailData(SpUtils.getSp(mContext,"uid"),mallId,longitude,latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallDetailBean>() {
                    private MallDetailBean mallDetailBean;

                    @Override
                    public void onCompleted() {
                       if (mallDetailBean.getCode()==0){
                           data = mallDetailBean.getData();
                           String issc = mallDetailBean.getData().getIssc();
                           if ("1".equals(issc)){
                               cb_title_right.setChecked(true);
                           }else {
                               cb_title_right.setChecked(false);
                           }
                           imgs.add(mallDetailBean.getData().getPics());
                           vp_banner.setImages(imgs)
                                   .setImageLoader(new GlideImageLoader())
                                   .setOnBannerListener(O2OMallDetailActivity.this)
                                   .start();
                           ImageLoadUtil.GlideGoodsImageLoad(mContext,mallDetailBean.getData().getImgfm(),img_tupian);
                           tv_mall_name.setText(mallDetailBean.getData().getDname());
                           tv_distance.setText(mallDetailBean.getData().getShowjl());
                           DecimalFormat decimalFormat=new DecimalFormat("0.0");
                           tv_pingfen.setText(decimalFormat.format(Double.valueOf(mallDetailBean.getData().getPingfen())));
                           tv_yuexiao.setText(mallDetailBean.getData().getDdxl());
                           tv_fangwen.setText(mallDetailBean.getData().getFwl());
                           if ("shangjia".equals(shangjia)){
                               stl_tab.setCurrentTab(2);
                               ll_gengxin.setVisibility(View.GONE);
                               switchFragment(mallFragment).commit();
                               Bundle bundle=new Bundle();
                               bundle.putString("latitude",latitude);
                               bundle.putString("longitude",longitude);
                               mallFragment.setArguments(bundle);
                           }else {
                               switchFragment(goodsFragment).commit();
                               Bundle bundle=new Bundle();
                               bundle.putString("latitude",latitude);
                               bundle.putString("longitude",longitude);
                               bundle.putString("mallId",mallId);
                               goodsFragment.setArguments(bundle);
                           }
                           tv_distance.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intent=new Intent(mContext,BaiduMapActivity.class);
                                   intent.putExtra("latitude",latitude);
                                   intent.putExtra("longitude",longitude);
                                   intent.putExtra("mallId",mallId);
                                   startActivity(intent);
                               }
                           });

                       }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MallDetailBean mallDetailBean) {

                        this.mallDetailBean = mallDetailBean;
                    }
                });
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_mall_detail_layout;
    }

    @Override
    public void OnBannerClick(int position) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_distance:
                Intent intent=new Intent(mContext,BaiduMapActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("mallId",mallId);
                startActivity(intent);
                break;
            case R.id.tv_go_buy:
                final O2OPayDialog dialog = new O2OPayDialog(mContext);
                LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                TextView tv_money = dialog.findViewById(R.id.tv_money);
//                tv_money.setText(tv_order_sum_money.getText().toString().trim());
                TextView tv_check_pay = dialog.findViewById(R.id.tv_check_pay);
                ll_alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                        img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                    }
                });
                ll_wechatpay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                        img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                    }
                });
                tv_check_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showToastMessage("支付");
                    }
                });
                dialog.show();
                showToastMessage("立即支付");
                break;
        }
    }
}
