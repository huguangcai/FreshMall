package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.UserRulesBean;

public class UserAgreementAdapter extends ListBaseAdapter<UserRulesBean.DataBean> {
    public UserAgreementAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_agreement_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        UserRulesBean.DataBean dataBean = mDataList.get(position);
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(dataBean.getTitle());
    }
}
