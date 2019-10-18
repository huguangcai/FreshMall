package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.view.ClassifyGoodsActivity;

public class HomeGoodsFooter extends AbsLinearLayout {
    private Context context;
    private TextView tv_more_tuijian;

    public HomeGoodsFooter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_goods_footer_layout;
    }

    @Override
    protected void initView() {
        tv_more_tuijian = getViewById(R.id.tv_more_tuijian);
        tv_more_tuijian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ClassifyGoodsActivity.class));
            }
        });
    }
}
