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

import java.util.List;

public class DetailItemAdapter extends RecyclerView.Adapter<DetailItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> guige;
    private OnItemClickListener onclickListener;
    private int position=0;

    public DetailItemAdapter(Context mContext, List<String> guige) {
        this.mContext = mContext;
        this.guige = guige;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_color.setText(guige.get(i));

       if (position==i){
           myViewHolder.tv_color.setTextColor(mContext.getResources().getColor(R.color.btn_color));
           myViewHolder.tv_color.setBackgroundResource(R.drawable.detail_item_bg);
       }else {
           myViewHolder.tv_color.setTextColor(mContext.getResources().getColor(R.color.black));
           myViewHolder.tv_color.setBackgroundResource(R.drawable.detail_item_no);
       }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener!=null){
                    onclickListener.onClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return guige.size();
    }

    public void setSelect(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_color;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_color = itemView.findViewById(R.id.tv_color);
        }
    }

    public void setOnclickListener(OnItemClickListener onclickListener){
        this.onclickListener = onclickListener;
    }
}
