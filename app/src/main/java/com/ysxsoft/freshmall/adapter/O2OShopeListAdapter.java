package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;

import java.util.List;

public class O2OShopeListAdapter extends RecyclerView.Adapter<O2OShopeListAdapter.MyListHolder> {

    private Context context;
    public List<O2OShopeCarListBean.DataBean> listBeanData;
    private OnModifyListener onModifyListener;

    public O2OShopeListAdapter(Context mContext, List<O2OShopeCarListBean.DataBean> listBeanData) {
        context = mContext;
        this.listBeanData = listBeanData;
    }

    @NonNull
    @Override
    public MyListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.o2o_shope_list_item_layout, null);
        return new MyListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyListHolder myListHolder, final int i) {
        O2OShopeCarListBean.DataBean dataBean = listBeanData.get(i);
        myListHolder.tv_content.setText(dataBean.getSpname());
        myListHolder.tv_num.setText(dataBean.getSliang());
        myListHolder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onModifyListener != null) {
                    onModifyListener.MiusClick(i,myListHolder.tv_num);
                }
            }
        });
        myListHolder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onModifyListener != null) {
                    onModifyListener.AddClick(i,myListHolder.tv_num);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBeanData.size();
    }

    public class MyListHolder extends RecyclerView.ViewHolder {

        private final TextView tv_content;
        private final ImageView img_minus;
        private final TextView tv_num;
        private final ImageView img_add;

        public MyListHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            img_minus = itemView.findViewById(R.id.img_minus);
            tv_num = itemView.findViewById(R.id.tv_num);
            img_add = itemView.findViewById(R.id.img_add);
        }
    }

    public interface OnModifyListener {
        void MiusClick(int position,View showCountView);

        void AddClick(int position,View showCountView);
    }

    public void setOnModifyListener(OnModifyListener onModifyListener) {
        this.onModifyListener = onModifyListener;
    }
}
