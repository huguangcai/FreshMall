package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.AboutMyBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AboutMyActivity extends BaseActivity {

    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestData();
        setBackVisibily();
        setTitle("关于我们");
        web_content = getViewById(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        web_content.setWebViewClient(new MyWebViewClient());
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .AboutMy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AboutMyBean>() {
                    private AboutMyBean aboutMyBean;

                    @Override
                    public void onCompleted() {
                        if (aboutMyBean.getCode()==0){
                            AboutMyBean.DataBean data = aboutMyBean.getData();
                            web_content.loadDataWithBaseURL(null,data.getGywmtext(),"text/html","utf-8",null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AboutMyBean aboutMyBean) {

                        this.aboutMyBean = aboutMyBean;
                    }
                });

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
        return R.layout.about_my_layout;
    }
}
