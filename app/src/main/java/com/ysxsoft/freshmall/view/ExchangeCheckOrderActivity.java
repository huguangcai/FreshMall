package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.RBaseAdapter;
import com.ysxsoft.freshmall.com.RViewHolder;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommentResponse;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.GoodsListBean;
import com.ysxsoft.freshmall.modle.TimeResponse;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.Call;
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
    private ArrayList<GoodsListBean.DataBean> datas;
    private StringBuffer ids = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoodsListBean bean = (GoodsListBean) getIntent().getSerializableExtra("datas");
        datas = bean.getData();
        setBackVisibily();
        setTitle("订单确认");
        initView();
        request();
    }

    private void request() {
        OkHttpUtils.post()
                .url(ImpService.GET_TIME)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TimeResponse resp = JsonUtils.parseByGson(response, TimeResponse.class);
                        if (resp != null) {
                            if (resp.getCode() == 200) {
                                tvTime.setText(resp.getData());
                            }
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestAddressData();
    }

    private void RequestAddressData() {
        NetWork.getService(ImpService.class)
                .getDefaultAddressData(SpUtils.getSp(mContext, "uid"))
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
                        } else if (getAddressListBean.getCode() == 1) {
                            tv_no_address.setVisibility(View.VISIBLE);
                            cL1.setVisibility(View.GONE);
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
        RBaseAdapter<GoodsListBean.DataBean> adapter = new RBaseAdapter<GoodsListBean.DataBean>(mContext, R.layout.item_item_tab_exchange_layout, datas) {
            @Override
            protected void fillItem(RViewHolder holder, GoodsListBean.DataBean item, int position) {
                ImageView iv = holder.getView(R.id.iv);
                ImageLoadUtil.GlideGoodsImageLoad(mContext, item.getPic(), iv);
                holder.setText(R.id.tvDesc, item.getDesc());
                holder.setText(R.id.tvColor, item.getGuige());
                holder.setText(R.id.tvNum, "1个");
            }

            @Override
            protected int getViewType(GoodsListBean.DataBean item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);


        tv_no_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GetGoodsAddressActivity.class);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressId <= 0) {
                    showToastMessage("地址不能为空");
                    return;
                }
                if (ids.length() != 0) {
                    ids.setLength(0);
                }
                for (int i = 0; i < datas.size(); i++) {
                    ids.append(datas.get(i).getId()).append(",");
                }
                String pids = ids.deleteCharAt(ids.length() - 1).toString();
                submitData(pids);
            }
        });

    }

    private void submitData(String pids) {
        OkHttpUtils.post()
                .url(ImpService.DOWN_ORDER)
                .addParams("uid", SpUtils.getSp(mContext, "uid"))
                .addParams("pids", pids)
                .addParams("aid", String.valueOf(addressId))
                .addParams("sum", String.valueOf(datas.size()))
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            showToastMessage(resp.getMsg());
                            if (resp.getCode() == 200) {
                                finish();
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_exchange_check_order;
    }
}
