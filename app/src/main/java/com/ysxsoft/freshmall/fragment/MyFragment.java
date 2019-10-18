package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.NumberBean;
import com.ysxsoft.freshmall.modle.UserInfoBean;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.AllOrderActivity;
import com.ysxsoft.freshmall.view.MyCollectActivity;
import com.ysxsoft.freshmall.view.MyDistributionActivity;
import com.ysxsoft.freshmall.view.MyOrderActivity;
import com.ysxsoft.freshmall.view.MyVipActivity;
import com.ysxsoft.freshmall.view.MyWalletActivity;
import com.ysxsoft.freshmall.view.PersonDataActivity;
import com.ysxsoft.freshmall.view.SettingActivity;
import com.ysxsoft.freshmall.view.UserAgreementActivity;
import com.ysxsoft.freshmall.widget.CircleImageView;
import com.ysxsoft.freshmall.widget.segmenttablayout.MsgView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView img_head;
    private TextView tv_nikeName, tv_phone_num, tv_my_wallet, tv_wait_get_goods, tv_vip,
            tv_distribution, tv_all_order, tv_wait_pay, tv_wait_fahuo, tv_wait_evaluate, tv_return_goods;
    private LinearLayout ll_o2o_order, ll_my_collect, ll_agreement, ll_setting;
    private String uid;
    private String headurl;
    private String phone;
    private String nikename;
    private MsgView msg_wait_pay,msg_return_goods,msg_wait_fahuo,msg_wait_get_goods,msg_wait_evaluate;

    @Override
    protected void initData() {
        img_head = getViewById(R.id.img_head);
        tv_nikeName = getViewById(R.id.tv_nikeName);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_my_wallet = getViewById(R.id.tv_my_wallet);
        tv_vip = getViewById(R.id.tv_vip);
        tv_distribution = getViewById(R.id.tv_distribution);
        tv_all_order = getViewById(R.id.tv_all_order);
        tv_wait_pay = getViewById(R.id.tv_wait_pay);
        tv_wait_fahuo = getViewById(R.id.tv_wait_fahuo);
        tv_wait_get_goods = getViewById(R.id.tv_wait_get_goods);
        tv_wait_evaluate = getViewById(R.id.tv_wait_evaluate);
        tv_return_goods = getViewById(R.id.tv_return_goods);
        ll_o2o_order = getViewById(R.id.ll_o2o_order);
        ll_my_collect = getViewById(R.id.ll_my_collect);
        ll_agreement = getViewById(R.id.ll_agreement);
        ll_setting = getViewById(R.id.ll_setting);

        msg_wait_pay = getViewById(R.id.msg_wait_pay);
        msg_wait_fahuo = getViewById(R.id.msg_wait_fahuo);
        msg_wait_get_goods = getViewById(R.id.msg_wait_get_goods);
        msg_wait_evaluate = getViewById(R.id.msg_wait_evaluate);
        msg_return_goods = getViewById(R.id.msg_return_goods);

        View view_line1 = getViewById(R.id.view_line1);
        View view_line2 = getViewById(R.id.view_line2);

        if ("1".equals(SpUtils.getSp(getContext(), "useType"))) {
            ll_o2o_order.setVisibility(View.GONE);
            ll_my_collect.setVisibility(View.GONE);
            view_line1.setVisibility(View.INVISIBLE);
            view_line2.setVisibility(View.GONE);
            tv_vip.setVisibility(View.GONE);
            tv_distribution.setVisibility(View.GONE);
        } else {
            ll_o2o_order.setVisibility(View.VISIBLE);
            ll_my_collect.setVisibility(View.VISIBLE);
            view_line1.setVisibility(View.VISIBLE);
            view_line2.setVisibility(View.VISIBLE);
            tv_vip.setVisibility(View.VISIBLE);
            tv_distribution.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListenser() {
        img_head.setOnClickListener(this);
        tv_my_wallet.setOnClickListener(this);
        tv_vip.setOnClickListener(this);
        tv_distribution.setOnClickListener(this);
        tv_all_order.setOnClickListener(this);
        tv_wait_pay.setOnClickListener(this);
        tv_wait_fahuo.setOnClickListener(this);
        tv_wait_get_goods.setOnClickListener(this);
        tv_wait_evaluate.setOnClickListener(this);
        tv_return_goods.setOnClickListener(this);
        ll_o2o_order.setOnClickListener(this);
        ll_my_collect.setOnClickListener(this);
        ll_agreement.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.my_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:
                Intent intent = new Intent(getActivity(), PersonDataActivity.class);
                intent.putExtra("headurl", headurl);
                intent.putExtra("nikename", tv_nikeName.getText().toString());
                intent.putExtra("phone", phone);
                startActivity(intent);
                break;
            case R.id.tv_my_wallet://我的钱包
                startActivity(MyWalletActivity.class);
//                startActivity(RegisterBusnessActivity.class);
                break;
            case R.id.tv_vip://我的会员
                startActivity(MyVipActivity.class);
                break;
            case R.id.tv_distribution://我的分销
                startActivity(MyDistributionActivity.class);
                break;
            case R.id.tv_all_order://全部订单
                startActivity(AllOrderActivity.class);
                break;
            case R.id.tv_wait_pay://待付款
                Intent intent1 = new Intent(getActivity(), AllOrderActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);
                break;
            case R.id.tv_wait_fahuo://待发货
                Intent intent2 = new Intent(getActivity(), AllOrderActivity.class);
                intent2.putExtra("type", "2");
                startActivity(intent2);
                break;
            case R.id.tv_wait_get_goods://待收货
                Intent intent3 = new Intent(getActivity(), AllOrderActivity.class);
                intent3.putExtra("type", "3");
                startActivity(intent3);

                break;
            case R.id.tv_wait_evaluate://待评价
                Intent intent4 = new Intent(getActivity(), AllOrderActivity.class);
                intent4.putExtra("type", "4");
                startActivity(intent4);

                break;
            case R.id.tv_return_goods://退货 售后
                Intent intent5 = new Intent(getActivity(), AllOrderActivity.class);
                intent5.putExtra("type", "5");
                startActivity(intent5);
                break;
            case R.id.ll_o2o_order://o2o 订单
                startActivity(MyOrderActivity.class);
                break;
            case R.id.ll_my_collect://我的收藏
                Intent myintent = new Intent(getActivity(), MyCollectActivity.class);
                startActivity(myintent);
                break;
            case R.id.ll_agreement://用户守则
                startActivity(UserAgreementActivity.class);
                break;
            case R.id.ll_setting://设置
                startActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = SpUtils.getSp(getActivity(), "uid");
        if (uid != null && !TextUtils.isEmpty(uid)) {
            requestData();
            requestDotData();
        }
    }

    private void requestDotData() {
        NetWork.getService(ImpService.class)
                .getNumber(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumberBean>() {
                    private NumberBean numberBean;

                    @Override
                    public void onCompleted() {
                        if (numberBean.getCode()==0){
                            String dfkcount = numberBean.getData().getDfkcount();
                            if ("0".equals(dfkcount)){
                                msg_wait_pay.setVisibility(View.GONE);
                            }else {
                                msg_wait_pay.setVisibility(View.VISIBLE);
                                msg_wait_pay.setText(dfkcount);
                            }
                            String dfhcount = numberBean.getData().getDfhcount();
                            if ("0".equals(dfhcount)){
                                msg_wait_fahuo.setVisibility(View.GONE);
                            }else {
                                msg_wait_fahuo.setVisibility(View.VISIBLE);
                                msg_wait_fahuo.setText(dfhcount);
                            }
                            String dshcount = numberBean.getData().getDshcount();
                            if ("0".equals(dshcount)){
                                msg_wait_get_goods.setVisibility(View.GONE);
                            }else {
                                msg_wait_get_goods.setVisibility(View.VISIBLE);
                                msg_wait_get_goods.setText(dshcount);
                            }
                            String dpjcount = numberBean.getData().getDpjcount();
                            if ("0".equals(dpjcount)){
                                msg_wait_evaluate.setVisibility(View.GONE);
                            }else {
                                msg_wait_evaluate.setVisibility(View.VISIBLE);
                                msg_wait_evaluate.setText(dpjcount);
                            }
                            String shcount = numberBean.getData().getShcount();
                            if ("0".equals(shcount)){
                                msg_return_goods.setVisibility(View.GONE);
                            }else {
                                msg_return_goods.setVisibility(View.VISIBLE);
                                msg_return_goods.setText(shcount);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NumberBean numberBean) {

                        this.numberBean = numberBean;
                    }
                });
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .getUserInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoBean>() {
                    private UserInfoBean userInfoBean;

                    @Override
                    public void onCompleted() {
                        if (userInfoBean.getCode() == 0) {
                            tv_nikeName.setText(userInfoBean.getData().getUsername());
                            headurl = userInfoBean.getData().getPic();
                            nikename = userInfoBean.getData().getRename();
                            phone = userInfoBean.getData().getPhone();
                            ImageLoadUtil.GlideHeadImageLoad(getActivity(), userInfoBean.getData().getPic(), img_head);
                            tv_phone_num.setText(userInfoBean.getData().getPhone());
                            SpUtils.saveSp(getActivity(), "vip", userInfoBean.getData().getIsvip());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {

                        this.userInfoBean = userInfoBean;
                    }
                });
    }
}
