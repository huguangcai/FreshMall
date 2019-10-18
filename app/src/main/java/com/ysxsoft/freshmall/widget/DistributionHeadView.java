package com.ysxsoft.freshmall.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.ShareDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.ShareDataBean;
import com.ysxsoft.freshmall.modle.YaoQingInfoBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DistributionHeadView extends AbsLinearLayout {

    private TextView tv_money,tv_person,tv_my_invite_code;
    private ImageView img_qrCode;
    private Button btn_submit;
    private Context context;
    private ProgressDialog dialog;
    private final String uid;
    private ShareDataBean.DataBean data;
    public DistributionHeadView(Context context) {
        super(context);
        this.context = context;
        rqequestShareData();
        dialog = new ProgressDialog(context);
        uid = SpUtils.getSp(context, "uid");
        requestData();
    }

    private void rqequestShareData() {
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

    private void requestData() {
        NetWork.getService(ImpService.class)
                .getYaoQingInfo(uid,"1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YaoQingInfoBean>() {
                    private YaoQingInfoBean yaoQingInfoBean;

                    @Override
                    public void onCompleted() {
                       if (yaoQingInfoBean.getCode()==0){
                           YaoQingInfoBean.DataBean data = yaoQingInfoBean.getData();
                           tv_person.setText(data.getCount());
                           tv_money.setText(data.getMoney());
                           ImageLoadUtil.GlideGoodsImageLoad(context,data.getEwmurl(),img_qrCode);
                           tv_my_invite_code.setText(data.getYqm());
                       }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(YaoQingInfoBean yaoQingInfoBean) {

                        this.yaoQingInfoBean = yaoQingInfoBean;
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.distribution_headview_layout;
    }

    @Override
    protected void initView() {
        tv_money = getViewById(R.id.tv_money);
        tv_person = getViewById(R.id.tv_person);
        img_qrCode = getViewById(R.id.img_qrCode);
        tv_my_invite_code = getViewById(R.id.tv_my_invite_code);
        btn_submit = getViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShareDialog dialog = new ShareDialog(context);
                TextView tv_wechat = dialog.findViewById(R.id.tv_wechat);
                tv_wechat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        shareUrl( SHARE_MEDIA.WEIXIN);
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
            }
        });
    }

    private void shareUrl(SHARE_MEDIA media) {
        UMWeb umWeb = new UMWeb(data.getFxurl());
        umWeb.setTitle(data.getFxtitle());
        umWeb.setThumb(new UMImage(context, data.getFxpic()));
        umWeb.setDescription(data.getFxcontent());
        new ShareAction((Activity) context).withMedia(umWeb)
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
            Toast.makeText(context,"分享成功",Toast.LENGTH_SHORT).show();
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(context,"分享失败" + t.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(context,"取消分享",Toast.LENGTH_SHORT).show();
        }
    };
}
