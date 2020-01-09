package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.GetTypeListBean;

import java.util.List;

public class AllClassifyTitleAdapter extends RecyclerView.Adapter<AllClassifyTitleAdapter.TitleViewHordle>{

    private OnItemClickListener onItemClickListener;
    private int posotion=0;
    private Context context;
    private List<GetTypeListBean.DataBean> list;

    public AllClassifyTitleAdapter(Context context, List<GetTypeListBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AllClassifyTitleAdapter.TitleViewHordle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.classify_title_item_layout, null);
        return new AllClassifyTitleAdapter.TitleViewHordle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllClassifyTitleAdapter.TitleViewHordle titleViewHordle, final int i) {
        titleViewHordle.tv_title_classify.setText(list.get(i).getTypename());
        if (posotion==i){
            titleViewHordle.tv_title_classify.setBackgroundResource(R.color.white);
            titleViewHordle.tv_title_classify.setTextColor(context.getResources().getColor(R.color.btn_color));
        }else {
            titleViewHordle.tv_title_classify.setBackgroundResource(R.color.color_f7f7f7);
            titleViewHordle.tv_title_classify.setTextColor(context.getResources().getColor(R.color.color_282828));
        }
        titleViewHordle.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(i);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelect(int posotion) {
        this.posotion = posotion;
        notifyDataSetChanged();
    }

    public class TitleViewHordle extends RecyclerView.ViewHolder{

        private final TextView tv_title_classify;

        public TitleViewHordle(@NonNull View itemView) {
            super(itemView);
            tv_title_classify = itemView.findViewById(R.id.tv_title_classify);
        }
    }
    public interface OnItemClickListener{
        void onClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
