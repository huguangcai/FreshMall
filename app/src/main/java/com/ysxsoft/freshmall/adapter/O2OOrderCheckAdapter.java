package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class O2OOrderCheckAdapter extends RecyclerView.Adapter<O2OOrderCheckAdapter.O2OOrderHolder> {

    private Context mContext;
    private List<O2OShopeCarListBean.DataBean> listBeanData;

    public O2OOrderCheckAdapter(Context mContext, List<O2OShopeCarListBean.DataBean> listBeanData) {
        this.mContext = mContext;
        this.listBeanData = listBeanData;
    }

    @NonNull
    @Override
    public O2OOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.o2o_order_check_item_layout, viewGroup,false);
        return new O2OOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull O2OOrderHolder o2OOrderHolder, int i) {
        O2OShopeCarListBean.DataBean dataBean = listBeanData.get(i);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getFmtp(),o2OOrderHolder.img_tupian);
        o2OOrderHolder.tv_goods_name.setText(dataBean.getSpname());
        o2OOrderHolder.tv_number.setText("x"+dataBean.getSliang());
        o2OOrderHolder.tv_price.setText("¥ "+dataBean.getSpjiage());
    }

    @Override
    public int getItemCount() {
        return listBeanData.size();
    }

    public class O2OOrderHolder extends RecyclerView.ViewHolder {

        private final ImageView img_tupian;
        private final TextView tv_goods_name;
        private final TextView tv_number;
        private final TextView tv_price;

        public O2OOrderHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
