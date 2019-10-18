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
import com.ysxsoft.freshmall.modle.HomeSeckillBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class HomeSeckillAdapter extends RecyclerView.Adapter<HomeSeckillAdapter.HomeSeckillHolder> {

    private Context context;
    public List<HomeSeckillBean.DataBean.SplistBean> splist;
    private OnItemClickListener onItemClickListener;

    public HomeSeckillAdapter(Context context, List<HomeSeckillBean.DataBean.SplistBean> splist) {

        this.context = context;
        this.splist = splist;
    }

    @NonNull
    @Override
    public HomeSeckillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_seckill_item_layout, viewGroup, false);
        return new HomeSeckillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSeckillHolder homeSeckillHolder, final int i) {
        HomeSeckillBean.DataBean.SplistBean splistBean = splist.get(i);
        ImageLoadUtil.GlideGoodsImageLoad(context,splistBean.getPic(),homeSeckillHolder.img_tupian);
        homeSeckillHolder.tv_goods_desc.setText(splistBean.getSpname());
        homeSeckillHolder.tv_vip_price.setText("¥"+splistBean.getVipjiage());
        homeSeckillHolder.tv_old_price.setText("¥"+splistBean.getJiage());
        homeSeckillHolder.tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        homeSeckillHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return splist.size();
    }

    public class HomeSeckillHolder extends RecyclerView.ViewHolder{

        private final ImageView img_tupian;
        private final TextView tv_goods_desc,tv_vip_price,tv_old_price;

        public HomeSeckillHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_goods_desc = itemView.findViewById(R.id.tv_goods_desc);
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
