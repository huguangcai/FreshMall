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
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.widget.CircleImageView;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

public class O2OEvaluateFragmentAdapter extends ListBaseAdapter<MallDetailBean.DataBean.PjlistBean> {
    public O2OEvaluateFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.o2o_evaluate_newitem_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MallDetailBean.DataBean.PjlistBean pjlistBean = mDataList.get(position);
        CircleImageView img_head = holder.getView(R.id.img_head);
        TextView tv_before = holder.getView(R.id.tv_before);
        RatingBar rb_star = holder.getView(R.id.rb_star);
        TextView tv_evaluate_content = holder.getView(R.id.tv_evaluate_content);
        TextView tv_time = holder.getView(R.id.tv_time);
        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        GridLayoutManager manager=new GridLayoutManager(mContext,3);
        recyclerView.setLayoutManager(manager);
        DetailEvaluateImgAdapter adapter=new DetailEvaluateImgAdapter(mContext,pjlistBean.getPjpic());
        recyclerView.setAdapter(adapter);
        ImageLoadUtil.GlideHeadImageLoad(mContext,pjlistBean.getPic(),img_head);
        tv_before.setText(pjlistBean.getPhone());
        rb_star.setRating(Float.valueOf(pjlistBean.getPjxj()));
        tv_evaluate_content.setText(pjlistBean.getPjcontent());
        tv_time.setText(pjlistBean.getPitime());

    }
}
