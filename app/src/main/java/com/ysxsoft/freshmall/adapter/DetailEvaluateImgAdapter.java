package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.GoodDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class DetailEvaluateImgAdapter extends RecyclerView.Adapter<DetailEvaluateImgAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> pingjia;

    public DetailEvaluateImgAdapter(Context mContext, List<String> pingjia) {
        this.mContext = mContext;
        this.pingjia = pingjia;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_img_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext,pingjia.get(i),myViewHolder.img_tupian);
    }

    @Override
    public int getItemCount() {
        return pingjia.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView img_tupian;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
        }
    }
}
