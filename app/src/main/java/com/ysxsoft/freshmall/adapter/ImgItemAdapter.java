package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class ImgItemAdapter extends RecyclerView.Adapter<ImgItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> exit_pics;

    public ImgItemAdapter(Context mContext, List<String> exit_pics) {
        this.mContext = mContext;
        this.exit_pics = exit_pics;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_img_layout, viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String s = exit_pics.get(i);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,s,myViewHolder.img_tupian);
    }

    @Override
    public int getItemCount() {
        return exit_pics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final ImageView img_tupian;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
        }
    }
}
