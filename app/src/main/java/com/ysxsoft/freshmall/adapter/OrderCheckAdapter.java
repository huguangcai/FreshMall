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
import com.ysxsoft.freshmall.modle.PackageDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class OrderCheckAdapter extends RecyclerView.Adapter<OrderCheckAdapter.MyViewHolder> {

    private Context mContext;
    private List<PackageDetailBean.DetailData> dataList;

    public OrderCheckAdapter(Context mContext, List<PackageDetailBean.DetailData> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_check_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataList.get(i).getUrl(),myViewHolder.img_tupian);
        myViewHolder.tv_color.setText(dataList.get(i).getGuige());
        myViewHolder.tv_content.setText(dataList.get(i).getContent());
        myViewHolder.tv_price.setText(dataList.get(i).getPrice());
        myViewHolder.tv_number.setText("X"+dataList.get(i).getNumber());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
