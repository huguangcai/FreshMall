package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.O2OSearchDataBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class O2OMoreClassifyListAdapter extends ListBaseAdapter<O2OSearchDataBean.DataBean> {

    public O2OMoreClassifyListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.o2o_fragment_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        O2OSearchDataBean.DataBean dataBean = mDataList.get(position);
        RoundedImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_distance = holder.getView(R.id.tv_distance);
        TextView tv_xiaoliang = holder.getView(R.id.tv_xiaoliang);
        TextView tv_price = holder.getView(R.id.tv_price);

        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getImgfm(),img_tupian);
        tv_mall_name.setText(dataBean.getDname());
        tv_distance.setText(dataBean.getShowjl());
        tv_xiaoliang.setText("销量："+dataBean.getXiaoliang());
        tv_price.setText(dataBean.getJunjia());
    }
}
