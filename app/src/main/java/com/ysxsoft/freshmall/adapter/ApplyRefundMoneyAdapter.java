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
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;

public class ApplyRefundMoneyAdapter extends RecyclerView.Adapter<ApplyRefundMoneyAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderDetailBean.DataBean.GoodsBean> dataBean;

    public ApplyRefundMoneyAdapter(Context mContext, List<OrderDetailBean.DataBean.GoodsBean> dataBean) {
        this.mContext = mContext;
        this.dataBean = dataBean;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_check_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.get(i).getPic(),myViewHolder.img_tupian);
        myViewHolder.tv_color.setText(dataBean.get(i).getGuige());
        myViewHolder.tv_content.setText(dataBean.get(i).getName());
        myViewHolder.tv_price.setText(dataBean.get(i).getMoney());
        myViewHolder.tv_number.setText("X"+dataBean.get(i).getNum());
    }

    @Override
    public int getItemCount() {
        return dataBean.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_tupian;
        private final TextView tv_content,tv_color,tv_price,tv_number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_number = itemView.findViewById(R.id.tv_number);
        }
    }
}
