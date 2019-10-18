package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;

public class LogisticsDetailActivity extends BaseActivity {

    private TextView tv_logistics_company,tv_logistics_phone,tv_logistics_number;
    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("物流详情");
        initView();
    }

    private void initView() {
        tv_logistics_company = getViewById(R.id.tv_logistics_company);
        tv_logistics_phone = getViewById(R.id.tv_logistics_phone);
        tv_logistics_number = getViewById(R.id.tv_logistics_number);
        web_content = getViewById(R.id.web_content);
    }

    @Override
    public int getLayout() {
        return R.layout.logistics_detail_layout;
    }
}
