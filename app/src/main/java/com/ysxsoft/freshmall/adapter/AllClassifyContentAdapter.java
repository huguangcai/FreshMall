package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class AllClassifyContentAdapter extends RecyclerView.Adapter<AllClassifyContentAdapter.ContentViewHordle> {
    private Context mContext;
    public List<GetTypeListBean.DataBean> data;
    private OnItemClickListener onItemClickListener;

    public AllClassifyContentAdapter(Context mContext, List<GetTypeListBean.DataBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public ContentViewHordle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.classify_content_item_layout, null);
        return new ContentViewHordle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHordle contentViewHordle, final int i) {
        contentViewHordle.tv_content.setText(data.get(i).getTypename());
        ImageLoadUtil.GlideGoodsImageLoad(mContext,data.get(i).getTypepic(),contentViewHordle.img_tupian);
        contentViewHordle.itemView.setOnClickListener(new View.OnClickListener() {
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
        return data.size();
    }

    public class ContentViewHordle extends RecyclerView.ViewHolder{

        private final RoundedImageView img_tupian;
        private final TextView tv_content;

        public ContentViewHordle(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_content = itemView.findViewById(R.id.tv_content);

        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
