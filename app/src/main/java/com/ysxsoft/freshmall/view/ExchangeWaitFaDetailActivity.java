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
import com.ysxsoft.freshmall.modle.ExchangeResponse;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create By 胡
 * on 2020/1/8 0008
 * 待发货详情
 */
public class ExchangeWaitFaDetailActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private RecyclerView recyclerView;
    private TextView tvOrder;
    private TextView tvTime;
    private TextView tvTips;
    private String oid;
    private ConstraintLayout cL1;
    private TextView tv_no_address;
    private int addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oid = getIntent().getStringExtra("oid");
        setBackVisibily();
        setTitle("待发货");
        initView();
        requestData();
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

    private void requestData() {
        OkHttpUtils.post()
                .url(ImpService.ORDER_DETAIL)
                .addParams("oid", oid)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ExchangeResponse resp = JsonUtils.parseByGson(response, ExchangeResponse.class);
                        if (resp != null) {
                            if (resp.getCode() == 200) {
                                List<ExchangeResponse.DataBeanX.DataBean.ProductBean> product = resp.getData().getData().get(0).getProduct();
                                RBaseAdapter<ExchangeResponse.DataBeanX.DataBean.ProductBean> adapter = new RBaseAdapter<ExchangeResponse.DataBeanX.DataBean.ProductBean>(mContext, R.layout.item_item_tab_exchange_layout, product) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, ExchangeResponse.DataBeanX.DataBean.ProductBean item, int position) {
                                        ImageView iv = holder.getView(R.id.iv);
                                        ImageLoadUtil.GlideGoodsImageLoad(mContext, item.getSppic(), iv);
                                        holder.setText(R.id.tvDesc, item.getSpname());
                                        holder.setText(R.id.tvColor, item.getSpgg());
                                        holder.setText(R.id.tvNum, item.getDdid()+"个");
                                    }

                                    @Override
                                    protected int getViewType(ExchangeResponse.DataBeanX.DataBean.ProductBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                });


    }

    private void initView() {
        tvName = getViewById(R.id.tvName);
        cL1 = getViewById(R.id.cL1);
        tv_no_address = getViewById(R.id.tv_no_address);
        tvPhone = getViewById(R.id.tvPhone);
        tvAddress = getViewById(R.id.tvAddress);
        recyclerView = getViewById(R.id.recyclerView);
        tvOrder = getViewById(R.id.tvOrder);
        tvTime = getViewById(R.id.tvTime);
        tvTips = getViewById(R.id.tvTips);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        tvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.activity_exchange_waitfa_detail;
    }
}