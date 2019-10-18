package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.UserRulesDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserAgreementDetailActivity extends BaseActivity {

    private String id;
    private TextView tv_info_title, tv_platform, tv_time;
    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        setBackVisibily();
        setTitle("守则详情");
        requestData();
        initView();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .UserRulesDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserRulesDetailBean>() {
                    private UserRulesDetailBean userRulesDetailBean;

                    @Override
                    public void onCompleted() {
                        if (userRulesDetailBean.getCode() == 0) {
                            UserRulesDetailBean.DataBean data = userRulesDetailBean.getData();
                            tv_info_title.setText(data.getTitle());
                            tv_time.setText(data.getTime());
                            web_content.loadDataWithBaseURL(null, data.getContent(), "text/html", "utf-8", null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage("服务器错误");
                    }

                    @Override
                    public void onNext(UserRulesDetailBean userRulesDetailBean) {

                        this.userRulesDetailBean = userRulesDetailBean;
                    }
                });
    }

    private void initView() {
        tv_info_title = getViewById(R.id.tv_info_title);
        tv_platform = getViewById(R.id.tv_platform);
        tv_time = getViewById(R.id.tv_time);
        web_content = getViewById(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        web_content.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.info_detail_layout;
    }
}
