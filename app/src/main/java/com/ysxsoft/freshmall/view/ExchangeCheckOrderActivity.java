package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.RBaseAdapter;
import com.ysxsoft.freshmall.com.RViewHolder;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By 胡
 * on 2020/1/8 0008
 * 订单确认
 */
public class ExchangeCheckOrderActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tv_no_address;
    private TextView tvTime;
    private int addressId;
    private ConstraintLayout cL1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("订单确认");
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        RequestAddressData();
    }

    private void RequestAddressData() {
        NetWork.getService(ImpService.class)
                .getDefaultAddressData(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetAddressListBean>() {
                    private GetAddressListBean getAddressListBean;

                    @Override
                    public void onCompleted() {
                        if (getAddressListBean.getCode() == 0) {
                            addressId = getAddressListBean.getData().get(0).getId();
                            int size = getAddressListBean.getData().size();
                            if (size <= 0) {
                                tv_no_address.setVisibility(View.VISIBLE);
                                cL1.setVisibility(View.GONE);
                            } else {
                                tv_no_address.setVisibility(View.GONE);
                                cL1.setVisibility(View.VISIBLE);
                            }
                            tvName.setText(getAddressListBean.getData().get(0).getShname());
                            tvPhone.setText(getAddressListBean.getData().get(0).getShphone());
                            tvAddress.setText(getAddressListBean.getData().get(0).getShxxdz());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetAddressListBean getAddressListBean) {

                        this.getAddressListBean = getAddressListBean;
                    }
                });
    }

    private void initView() {
        cL1 = getViewById(R.id.cL1);
        tvName = getViewById(R.id.tvName);
        tvPhone = getViewById(R.id.tvPhone);
        tvAddress = getViewById(R.id.tvAddress);
        tvAddress = getViewById(R.id.tvAddress);
        tv_no_address = getViewById(R.id.tv_no_address);
        tvTime = getViewById(R.id.tvTime);
        RecyclerView recyclerView = getViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        TextView tvOk = getViewById(R.id.tvOk);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            strings.add(String.valueOf(i));
        }
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_item_tab_exchange_layout, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                ImageView iv = holder.getView(R.id.iv);
//                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.getSppic(), iv);
//                holder.setText(R.id.tvDesc, item.getSpname());
//                holder.setText(R.id.tvColor, item.getSpgg());
//                holder.setText(R.id.tvNum, item.getDdid()+"个");
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_exchange_check_order;
    }
}
