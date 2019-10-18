package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.SecikllMoreBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class SecKillAdapter extends ListBaseAdapter<SecikllMoreBean.DataBean.SplistBean> {
    public SecKillAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.seckill_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        SecikllMoreBean.DataBean.SplistBean splistBean = mDataList.get(position);
        ImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_current_price = holder.getView(R.id.tv_current_price);
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        TextView tv_surplus_num = holder.getView(R.id.tv_surplus_num);
        TextView tv_seckill = holder.getView(R.id.tv_seckill);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,splistBean.getPic(),img_tupian);
        tv_content.setText(splistBean.getSpname());
        tv_current_price.setText(splistBean.getVipjiage());
        tv_old_price.setText("¥"+splistBean.getJiage());
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        tv_surplus_num.setText("库存"+splistBean.getKucun());

    }
}
