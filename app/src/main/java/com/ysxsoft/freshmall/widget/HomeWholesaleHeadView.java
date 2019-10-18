package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.widget.banner.Banner;

public class HomeWholesaleHeadView extends AbsLinearLayout {

    private Banner vp_banner;
    private MyRecyclerView recyclerView;
    private TextView tv_goods_more;

    public HomeWholesaleHeadView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_wholesale_fragment_headview_layout;
    }

    @Override
    protected void initView() {
        vp_banner = getViewById(R.id.vp_banner);
        recyclerView = getViewById(R.id.recyclerView);
        tv_goods_more = getViewById(R.id.tv_goods_more);
    }
}
