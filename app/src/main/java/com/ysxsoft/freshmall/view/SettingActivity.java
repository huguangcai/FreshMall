package com.ysxsoft.freshmall.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.LoginOutDilaog;
import com.ysxsoft.freshmall.dialog.ShareDialog;
import com.ysxsoft.freshmall.dialog.VersionUpdataDilaog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.ShareDataBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_clear_cache, ll_share_app, ll_version_update, ll_give_back_message, ll_about_my;
    private Button btn_login_out;
    private ProgressDialog dialog;
    private ShareDataBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rqequestData();
        setBackVisibily();
        setTitle("设置");
        dialog = new ProgressDialog(this);
        initView();
        initListener();
    }

    private void rqequestData() {
        NetWork.getService(ImpService.class)
                .ShareData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShareDataBean>() {
                    private ShareDataBean shareDataBean;

                    @Override
                    public void onCompleted() {
                        if (shareDataBean.getCode()==0){
                            data = shareDataBean.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShareDataBean shareDataBean) {

                        this.shareDataBean = shareDataBean;
                    }
                });

    }

    private void initView() {
        ll_clear_cache = getViewById(R.id.ll_clear_cache);
        ll_share_app = getViewById(R.id.ll_share_app);
        ll_version_update = getViewById(R.id.ll_version_update);
        ll_give_back_message = getViewById(R.id.ll_give_back_message);
        ll_about_my = getViewById(R.id.ll_about_my);
        btn_login_out = getViewById(R.id.btn_login_out);
    }

    private void initListener() {
        ll_clear_cache.setOnClickListener(this);
        ll_share_app.setOnClickListener(this);
        ll_version_update.setOnClickListener(this);
        ll_give_back_message.setOnClickListener(this);
        ll_about_my.setOnClickListener(this);
        btn_login_out.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_clear_cache:
                startActivity(CleanCacheActivity.class);
                break;
            case R.id.ll_share_app:
                final ShareDialog dialog = new ShareDialog(mContext);
                TextView tv_wechat = dialog.findViewById(R.id.tv_wechat);
                tv_wechat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        shareUrl(SHARE_MEDIA.WEIXIN);
                    }
                });
                TextView tv_QQ = dialog.findViewById(R.id.tv_QQ);
                tv_QQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        shareUrl(SHARE_MEDIA.QQ);
                    }
                });
                dialog.show();
                break;
            case R.id.ll_version_update:
                VersionUpdataDilaog updataDilaog=new VersionUpdataDilaog(mContext);
                updataDilaog.show();
                break;
            case R.id.ll_give_back_message:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.ll_about_my:
                startActivity(AboutMyActivity.class);
                break;
            case R.id.btn_login_out:
                LoginOutDilaog dilaog = new LoginOutDilaog(mContext);
                dilaog.show();
                SpUtils.deleteSp(mContext);
                break;
        }
    }

    private void shareUrl(SHARE_MEDIA media) {
        UMWeb umWeb = new UMWeb(data.getFxurl());
        umWeb.setTitle(data.getFxtitle());
        umWeb.setThumb(new UMImage(mContext, data.getFxpic()));
        umWeb.setDescription(data.getFxcontent());
        new ShareAction(SettingActivity.this).withMedia(umWeb)
                .setPlatform(media)
                .setCallback(shareListener).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToastMessage("分享成功");
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("分享失败" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("取消分享");

        }
    };
}
