package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.InfoDeailtBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InfoDetailActivity extends BaseActivity {

    private TextView tv_info_title, tv_platform, tv_time;
    private WebView web_content;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        setBackVisibily();
        setTitle("消息详情");
        requestData();
        initView();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .InfoDeailt(SpUtils.getSp(mContext, "uid"), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InfoDeailtBean>() {
                    private InfoDeailtBean infoDeailtBean;

                    @Override
                    public void onCompleted() {
                        if (infoDeailtBean.getCode() == 0) {
                            tv_info_title.setText(infoDeailtBean.getData().getTitle());
//                            tv_platform.setText(infoDeailtBean.getData().g);
                            tv_time.setText(infoDeailtBean.getData().getAdd_time());
                            web_content.loadDataWithBaseURL(null,infoDeailtBean.getData().getContent(),"text/html","utf-8",null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InfoDeailtBean infoDeailtBean) {

                        this.infoDeailtBean = infoDeailtBean;
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
