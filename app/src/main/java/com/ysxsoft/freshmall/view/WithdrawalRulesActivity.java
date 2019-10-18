package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.utils.BaseActivity;

public class WithdrawalRulesActivity extends BaseActivity {

    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("提现规则");
        web_content = getViewById(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        web_content.setWebViewClient(new MyWebViewClient());
        //        web_content.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
    }

    @Override
    public int getLayout() {
        return R.layout.withdrawal_rules_layout;
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
