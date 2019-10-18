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
import com.ysxsoft.freshmall.modle.HomeNewBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class HomeNewAdapter extends RecyclerView.Adapter<HomeNewAdapter.HomeNewHolder> {

    private Context context;
    public List<HomeNewBean.DataBean> data;
    private OnItemClickListener onItemClickListener;

    public HomeNewAdapter(Context context, List<HomeNewBean.DataBean> data) {

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HomeNewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_new_item_layout, viewGroup, false);
        return new HomeNewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeNewHolder homeNewHolder, final int i) {
        ImageLoadUtil.GlideGoodsImageLoad(context,data.get(i).getPic(),homeNewHolder.img_tupian);
        homeNewHolder.tv_desc.setText(data.get(i).getSpname());
        homeNewHolder.tv_price.setText("¥"+data.get(i).getVipjiage());
        homeNewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public class HomeNewHolder extends RecyclerView.ViewHolder{

        private final ImageView img_tupian;
        private final TextView tv_desc,tv_price;

        public HomeNewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }

    public interface OnItemClickListener{
        void itemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
