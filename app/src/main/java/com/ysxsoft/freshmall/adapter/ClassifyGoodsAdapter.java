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

import java.util.List;

public class ClassifyGoodsAdapter extends RecyclerView.Adapter<ClassifyGoodsAdapter.MyViewHolder> {
    private Context mContext;
    private List<GetTypeListBean.DataBean> data;
    private int posotion=0;
    private OnItemClickListener onItemClickListener;

    public ClassifyGoodsAdapter(Context mContext, List<GetTypeListBean.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.classify_goods_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_title.setText(data.get(i).getTypename());
        if (posotion==i){
            myViewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.btn_color));
            myViewHolder.view_line.setBackgroundResource(R.color.btn_color);
        }else {
            myViewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.black));
            myViewHolder.view_line.setBackgroundResource(R.color.transparent);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(i);
                }
            }
        });
    }
    public void setSelect(int posotion) {
        this.posotion = posotion;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_title;
        private final View view_line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            view_line = itemView.findViewById(R.id.view_line);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
