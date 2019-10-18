package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.ShopEvaluateBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.widget.CircleImageView;

public class AllEvaluateAdapter extends ListBaseAdapter<ShopEvaluateBean.DataBean> {

    public AllEvaluateAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.o2o_evaluate_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ShopEvaluateBean.DataBean dataBean = mDataList.get(position);
        CircleImageView img_head = holder.getView(R.id.img_head);
        TextView tv_before = holder.getView(R.id.tv_before);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_color = holder.getView(R.id.tv_color);
        TextView tv_size = holder.getView(R.id.tv_size);
        RatingBar rb_star = holder.getView(R.id.rb_star);
        TextView tv_evaluate_content = holder.getView(R.id.tv_evaluate_content);
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        ImageLoadUtil.GlideHeadImageLoad(mContext,dataBean.getHeadpic(),img_head);
        tv_before.setText(dataBean.getName());
        tv_time.setText(dataBean.getPjtime());
        rb_star.setRating(Float.valueOf(dataBean.getXingji()));
        tv_evaluate_content.setText(dataBean.getContent());
        tv_color.setText(dataBean.getGuige());
        GridLayoutManager manager=new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(manager);
        DetailEvaluateImgAdapter adapter=new DetailEvaluateImgAdapter(mContext,dataBean.getPjpic());
        recyclerView.setAdapter(adapter);
    }
}
