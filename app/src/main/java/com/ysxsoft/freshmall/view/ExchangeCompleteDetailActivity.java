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
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By 胡
 * on 2020/1/8 0008
 * 待发货详情
 */
public class ExchangeCompleteDetailActivity extends BaseActivity {

    private TextView tvName,tvNum,tvKd;
    private TextView tvPhone;
    private TextView tvAddress;
    private RecyclerView recyclerView;
    private TextView tvOrder;
    private TextView tvTime;
    private ConstraintLayout cL1;
    private ConstraintLayout cL2;
    private TextView tv_no_address;
    private int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("已完成");
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
                                cL2.setVisibility(View.GONE);
                            } else {
                                tv_no_address.setVisibility(View.GONE);
                                cL2.setVisibility(View.VISIBLE);
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
        tvName = getViewById(R.id.tvName);
        cL1 = getViewById(R.id.cL1);
        cL2 = getViewById(R.id.cL2);
        tvNum = getViewById(R.id.tvNum);
        tv_no_address = getViewById(R.id.tv_no_address);
        tvKd = getViewById(R.id.tvKd);
        tvPhone = getViewById(R.id.tvPhone);
        tvAddress = getViewById(R.id.tvAddress);
        recyclerView = getViewById(R.id.recyclerView);
        tvOrder = getViewById(R.id.tvOrder);
        tvTime = getViewById(R.id.tvTime);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            strings.add(String.valueOf(i));
        }
        RBaseAdapter<String> adapter = new RBaseAdapter<String>(mContext, R.layout.item_item_tab_exchange_layout, strings) {
            @Override
            protected void fillItem(RViewHolder holder, String item, int position) {
                ImageView iv = holder.getView(R.id.iv);
//                ImageLoadUtil.GlideGoodsImageLoad(mContext,item,iv);
//                holder.setText(R.id.tvDesc,"");
//                holder.setText(R.id.tvColor,"");
//                holder.setText(R.id.tvNum,"");
            }

            @Override
            protected int getViewType(String item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
        cL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LogisticsDetailActivity.class);
            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_exchange_complete_detail;
    }
}
