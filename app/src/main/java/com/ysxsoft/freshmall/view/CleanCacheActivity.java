package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.DataCleanManager;

public class CleanCacheActivity extends BaseActivity {

    private String totalCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("清除缓存");
        try {
            totalCacheSize = DataCleanManager.getTotalCacheSize(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView tv_clean = getViewById(R.id.tv_clean);
        tv_clean.setText(totalCacheSize);
        DataCleanManager.clearAllCache(mContext);
    }

    @Override
    public int getLayout() {
        return R.layout.clean_cache_layout;
    }
}
