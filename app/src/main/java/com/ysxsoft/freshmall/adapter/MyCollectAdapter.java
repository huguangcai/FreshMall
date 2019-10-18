package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.ColleteListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class MyCollectAdapter extends ListBaseAdapter<ColleteListBean.DataBean> {

    public MyCollectAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_collect_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ColleteListBean.DataBean dataBean = mDataList.get(position);
        ImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_distance = holder.getView(R.id.tv_distance);
        TextView tv_sales_volume = holder.getView(R.id.tv_sales_volume);
        TextView tv_price = holder.getView(R.id.tv_price);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getImgfm(),img_tupian);
        tv_name.setText(dataBean.getDname());
        tv_distance.setText(dataBean.getShowjl());
        tv_sales_volume.setText(dataBean.getDdxl());
        tv_price.setText(dataBean.getDdjj());
    }
}
