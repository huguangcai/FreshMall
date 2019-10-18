package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.InfoListBean;

public class InfoFragmentAdapter extends ListBaseAdapter<InfoListBean.DataBean> {
    public InfoFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.info_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        InfoListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_item_title = holder.getView(R.id.tv_item_title);
        TextView tv_red_point = holder.getView(R.id.tv_red_point);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_content.setText(dataBean.getContent());
        tv_time.setText(dataBean.getAdd_time());
        tv_item_title.setText(dataBean.getTitle());
        if ("1".equals(dataBean.getRead())){
            tv_red_point.setVisibility(View.GONE);
        }else {
            tv_red_point.setVisibility(View.VISIBLE);
        }
    }
}
