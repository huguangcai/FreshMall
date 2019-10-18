package com.ysxsoft.freshmall.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.view.SearchDataActivity;

public class WholesaleFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_title_right;
    private EditText ed_title_search;
    private RelativeLayout rl_search;
    @Override
    protected void initData() {
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("搜索");
        ed_title_search = getViewById(R.id.ed_title_search);
        ed_title_search.setFocusable(false);
        rl_search = getViewById(R.id.rl_search);
    }

    @Override
    protected void initListenser() {
        tv_title_right.setOnClickListener(this);
        ed_title_search.setOnClickListener(this);
        rl_search.setOnClickListener(this);
//        HomeWholesaleHeadView
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_wholesale_fragment_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ed_title_search:
            case R.id.rl_search:
            case R.id.tv_title_right:
                startActivity(SearchDataActivity.class);
                break;
        }
    }
}
