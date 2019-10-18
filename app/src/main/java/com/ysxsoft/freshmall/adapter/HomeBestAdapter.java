package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.HomeBestBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class HomeBestAdapter extends RecyclerView.Adapter<HomeBestAdapter.HomeBestHolder> {

    private Context context;
    public List<HomeBestBean.DataBean> data;
    private OnItemClickListener onItemClickListener;

    public HomeBestAdapter(Context context, List<HomeBestBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HomeBestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.home_best_item_layout, viewGroup, false);
        return new HomeBestHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBestHolder homeBestHolder, final int i) {
        HomeBestBean.DataBean dataBean = data.get(i);
        ImageLoadUtil.GlideGoodsImageLoad(context,dataBean.getPic(),homeBestHolder.img_tupian);
        homeBestHolder.tv_vip_price.setText("¥"+dataBean.getVipjiage());
        homeBestHolder.tv_old_price.setText("¥"+dataBean.getJiage());
        homeBestHolder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        homeBestHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.itemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HomeBestHolder extends RecyclerView.ViewHolder{

        private final ImageView img_tupian;
        private final TextView tv_desc,tv_vip_price,tv_old_price;

        public HomeBestHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_vip_price = itemView.findViewById(R.id.tv_vip_price);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
        }
    }
    public interface OnItemClickListener{
        void itemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
