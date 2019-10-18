package com.ysxsoft.freshmall.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.SendMessageBean;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： activity基类
 * 日期： 2018/10/23 0023 09:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public abstract class BaseActivity extends AppCompatActivity{

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityPageManager mActivityPageManager = ActivityPageManager.getInstance();
        mActivityPageManager.addActivity(this);
        mContext = this;

    }

    /**
     * 显示并关闭界面
     */
    public void setBackVisibily() {
        View img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack(v);
            }
        });
    }

    /**
     * 关闭界面
     * @param v
     */
    public void clickBack(View v) {
        finish();
    }

    public abstract int getLayout();

    public void setTitle(String title){
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    /**
     * 获取指定Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(int resId) {
        showToastMessage(getResources().getString(resId));
    }

    /**
     * 打印Log，tag默认为类的名字
     */
    protected void log(String text) {
        log(this.getClass().getName(), text);
    }

    /**
     * 打印Log
     */
    protected void log(String tag, String text) {
        Log.i(tag, text);
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    /**
     * 跳转到登录界面
     */
    protected void JumpLogin() {
        ActivityPageManager instance = ActivityPageManager.getInstance();
        instance.finishAllActivity();
//        startActivity(LoginActivity.class);
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private String data;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    protected String sendMessage(String phone) {
        NetWork.getService(ImpService.class)
                .sendMessage(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendMessageBean>() {
                    private SendMessageBean sendMessageBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(sendMessageBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SendMessageBean sendMessageBean) {
                        this.sendMessageBean = sendMessageBean;
                    }
                });
        return data;
    }
}
