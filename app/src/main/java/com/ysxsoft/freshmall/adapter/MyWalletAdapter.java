package com.ysxsoft.freshmall.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.YueInfoBean;

public class MyWalletAdapter extends ListBaseAdapter<YueInfoBean.DataBean> {

    public MyWalletAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_wallet_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        YueInfoBean.DataBean dataBean = mDataList.get(position);
        TextView tv_type = holder.getView(R.id.tv_type);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_money = holder.getView(R.id.tv_money);
        tv_type.setText(dataBean.getBeizhu());
        tv_time.setText(dataBean.getTimes());
        switch (dataBean.getType()) {
            case "0":
                tv_money.setText("+¥"+dataBean.getJiner());
                tv_money.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                break;
            case "1":
                tv_money.setText("-¥"+dataBean.getJiner());
                tv_money.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
                break;
            default:
                break;
        }


    }
}
