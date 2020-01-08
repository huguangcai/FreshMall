package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.RBaseAdapter;
import com.ysxsoft.freshmall.com.RViewHolder;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.Tab1ExchangeResponse;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class Tab1ExchangeAdapter extends ListBaseAdapter<Tab1ExchangeResponse.DataBeanX.DataBean> {

    private OnAdapterClickListener onAdapterClickListener;

    public Tab1ExchangeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_tab_exchange_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        final Tab1ExchangeResponse.DataBeanX.DataBean bean = mDataList.get(position);
        List<Tab1ExchangeResponse.DataBeanX.DataBean.ProductBean> product = bean.getProduct();
        TextView tv_order_number = holder.getView(R.id.tv_order_number);
        tv_order_number.setText(bean.getDdsh());
        TextView tvName = holder.getView(R.id.tvName);
        tvName.setText("待发货");
        TextView tvLook = holder.getView(R.id.tvLook);
        tvLook.setVisibility(View.GONE);
        TextView tvTips = holder.getView(R.id.tvTips);
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RBaseAdapter<Tab1ExchangeResponse.DataBeanX.DataBean.ProductBean> adapter = new RBaseAdapter<Tab1ExchangeResponse.DataBeanX.DataBean.ProductBean>(mContext, R.layout.item_item_tab_exchange_layout, product) {
            @Override
            protected void fillItem(RViewHolder holder, Tab1ExchangeResponse.DataBeanX.DataBean.ProductBean item, int position) {
                ImageView iv = holder.getView(R.id.iv);
                ImageLoadUtil.GlideGoodsImageLoad(mContext,item.getSppic(),iv);
                holder.setText(R.id.tvDesc,item.getSpname());
                holder.setText(R.id.tvColor,item.getSpgg());
                holder.setText(R.id.tvNum,item.getDdid()+"个");
            }

            @Override
            protected int getViewType(Tab1ExchangeResponse.DataBeanX.DataBean.ProductBean item, int position) {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
        tvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener!=null){
                    onAdapterClickListener.onItemClick(String.valueOf(bean.getId()));
                }
            }
        });
    }
    public interface OnAdapterClickListener{
       void onItemClick(String id);
    }
    public void setOnClickListener(OnAdapterClickListener onAdapterClickListener){
        this.onAdapterClickListener = onAdapterClickListener;
    }

}
