package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.YaoQingInfoBean;

public class MyDistributionAdapter extends ListBaseAdapter<YaoQingInfoBean.DataBean.ListBean> {
    public MyDistributionAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.distribution_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        YaoQingInfoBean.DataBean.ListBean listBean = mDataList.get(position);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_money = holder.getView(R.id.tv_money);
        tv_name.setText(listBean.getPhone());
        tv_time.setText(listBean.getTime());
        tv_money.setText(listBean.getMoney()+"元");

    }
}
