package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.SearchResultBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class SearchResultAdapter extends ListBaseAdapter<SearchResultBean.DataBean> {

    public SearchResultAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.search_result_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        SearchResultBean.DataBean dataBean = mDataList.get(position);
        RoundedImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_old_price = holder.getView(R.id.tv_old_price);
        ImageLoadUtil.GlideHeadImageLoad(mContext,dataBean.getPic(),img_tupian);
        tv_content.setText(dataBean.getSpname());
        tv_price.setText("¥"+dataBean.getVipjiage());
        if (dataBean.getJiage()!=null){
            tv_old_price.setText("¥"+dataBean.getJiage());
            tv_old_price.setVisibility(View.VISIBLE);
        }else {
            tv_old_price.setVisibility(View.GONE);
        }
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
    }
}
