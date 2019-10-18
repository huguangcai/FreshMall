package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;

public class O2OSecKillActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("秒杀");
        initView();
    }
    private void initView() {
        TextView tv_time = getViewById(R.id.tv_time);
    }
    @Override
    public int getLayout() {
        return R.layout.seckill_layout;
    }
}
