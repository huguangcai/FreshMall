package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.HomeBestMoreBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class GoodsFragmentAdapter extends ListBaseAdapter<HomeBestMoreBean.DataBean> {

    public GoodsFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.classify_goods_adapter_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        HomeBestMoreBean.DataBean dataBean = mDataList.get(position);
        ImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_current_price = holder.getView(R.id.tv_current_price);
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getPic(),img_tupian);
        tv_content.setText(dataBean.getSpname());
        tv_current_price.setText("¥"+dataBean.getVipjiage());
        if(dataBean.getJiage()!=null) {
            tv_old_price.setText("¥" + dataBean.getJiage());
            tv_old_price.setVisibility(View.VISIBLE);
        }else {
            tv_old_price.setVisibility(View.GONE);
        }
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }
}
