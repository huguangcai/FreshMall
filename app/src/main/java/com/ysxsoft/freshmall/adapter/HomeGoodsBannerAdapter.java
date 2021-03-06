package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.widget.CircleImageView;

import java.util.List;

public class HomeGoodsBannerAdapter extends RecyclerView.Adapter<HomeGoodsBannerAdapter.MyViewHolder> {
    private Context context;
    private List<GetTypeListBean.DataBean> data;
    private OnItemClickListener onclickListener;

    public HomeGoodsBannerAdapter(Context context, List<GetTypeListBean.DataBean> data) {

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_goods_banner_classify_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        if (i > 9) {
            myViewHolder.img_tupian.setVisibility(View.GONE);
            myViewHolder.tv_content.setVisibility(View.GONE);
        } else if (i == 9) {
//            myViewHolder.img_tupian.setBackgroundResource(R.mipmap.img_more);
            ImageLoadUtil.GlideGoodsImageLoad(context, data.get(i).getTypepic(), myViewHolder.img_tupian);
            myViewHolder.tv_content.setText("更多");
        } else {
            myViewHolder.tv_content.setText(data.get(i).getTypename());
            ImageLoadUtil.GlideGoodsImageLoad(context, data.get(i).getTypepic(), myViewHolder.img_tupian);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener != null) {
                    onclickListener.onClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView img_tupian;
        private final TextView tv_content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

}
