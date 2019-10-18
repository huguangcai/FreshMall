package com.ysxsoft.freshmall.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.DetailEvaluateImgAdapter;
import com.ysxsoft.freshmall.adapter.DetailItemAdapter;
import com.ysxsoft.freshmall.dialog.GoodsDetailDialog;
import com.ysxsoft.freshmall.dialog.ShareDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.CustomerPhoneBean;
import com.ysxsoft.freshmall.modle.GoodDetailBean;
import com.ysxsoft.freshmall.modle.PackageDetailBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.CircleImageView;
import com.ysxsoft.freshmall.widget.MyRecyclerView;
import com.ysxsoft.freshmall.widget.banner.Banner;
import com.ysxsoft.freshmall.widget.banner.GlideImageLoader;
import com.ysxsoft.freshmall.widget.banner.OnBannerListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, OnBannerListener {

    private View img_share;
    private Banner vp_banner;
    private TextView tv_time, tv_num, tv_current_price, tv_old_price, tv_content, tv_evaluate,
            tv_look_all, tv_phone_before, tv_after, tv_customer, tv_join_shop_card, tv_buy, tv_goods_specifications, tv_no_evaluate, tv_end_time, tv_number;
    private ImageView img_arrow;
    private CircleImageView img_head;
    private WebView web_content;
    private ProgressDialog dialog;
    private GoodDetailBean.DataBean data;
    private MyRecyclerView rv_tupian;
    private String uid;
    private TextView tv_goods_num, tv_time_desc;
    private String goodsId, seckill;
    private LinearLayout ll_have_evaluate;
    private ArrayList<PackageDetailBean.DetailData> detailBeans = new ArrayList<>();
    private CountDownTimer timer;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("goodsId");
        seckill = intent.getStringExtra("seckill");
        setBackVisibily();
        setHalfTransparent();
        setFitSystemWindow(false);
        setBackVisibily();
        int stateBar = getStateBar();
        LinearLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        initView();
        RequestData();
        initListener();
        requestPhoneData();
    }

    private void requestPhoneData() {
        NetWork.getService(ImpService.class)
                .CustomerPhoneData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerPhoneBean>() {
                    private CustomerPhoneBean customerPhoneBean;

                    @Override
                    public void onCompleted() {
                        if (customerPhoneBean.getCode() == 0) {
                            phoneNumber = customerPhoneBean.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CustomerPhoneBean customerPhoneBean) {

                        this.customerPhoneBean = customerPhoneBean;
                    }
                });
    }

    private void RequestData() {
        NetWork.getService(ImpService.class)
                .getGoodDetail(goodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodDetailBean>() {
                    private GoodDetailBean goodDetailBean;

                    @Override
                    public void onCompleted() {
                        if (goodDetailBean.getCode() == 0) {
                            data = goodDetailBean.getData();
                            vp_banner.setImages(data.getPiclist())
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(GoodsDetailActivity.this)
                                    .start();
                            tv_current_price.setText(String.valueOf(data.getVipjiage()));
                            if (data.getJiage() != null) {
                                tv_old_price.setText("¥" + data.getJiage());
                                tv_old_price.setVisibility(View.VISIBLE);
                            } else {
                                tv_old_price.setVisibility(View.GONE);
                            }
                            tv_num.setText(data.getKucun());
                            tv_content.setText(data.getSpname());
//                            web_content.loadDataWithBaseURL(null, data.getText(), "text/html", "utf-8", null);
                            web_content.loadDataWithBaseURL(null, getNewContent(data.getText()), "text/html", "utf-8", null);
                            if (data.getPingjia().size() != 0) {
                                tv_no_evaluate.setVisibility(View.GONE);
                                ll_have_evaluate.setVisibility(View.VISIBLE);
                                ImageLoadUtil.GlideHeadImageLoad(mContext, data.getPingjia().get(0).getHeadpic(), img_head);
                                tv_phone_before.setText(data.getPingjia().get(0).getName());
                                tv_evaluate.setText(data.getPingjia().get(0).getContent());
                                DetailEvaluateImgAdapter adapter = new DetailEvaluateImgAdapter(mContext, data.getPingjia().get(0).getPjpic());
                                rv_tupian.setAdapter(adapter);
                            } else {
                                tv_no_evaluate.setVisibility(View.VISIBLE);
                                ll_have_evaluate.setVisibility(View.GONE);
                            }
                            if (data.getMsmsg() == null) {
                                return;
                            }
                            timer = new CountDownTimer(Long.valueOf(Integer.valueOf(data.getMsmsg().getSytime()) * 1000), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
//                                    String s = AppUtil.FormarTime(AppUtil.AppTime.H_M_S, millisUntilFinished);
                                    tv_end_time.setText(AppUtil.secToTime(Integer.valueOf(String.valueOf(millisUntilFinished)) / 1000));
                                }

                                @Override
                                public void onFinish() {
                                    tv_end_time.setText("00:00:00");
                                }
                            }.start();

                            switch (data.getMsmsg().getStates()) {
                                case "1":
                                    tv_time_desc.setText("距离本场开始");
                                    tv_end_time.setVisibility(View.VISIBLE);
                                    break;
                                case "2":
                                    tv_time_desc.setText("距离本场结束");
                                    tv_end_time.setVisibility(View.VISIBLE);
                                    break;
                                case "3":
                                    tv_time_desc.setText(data.getMsmsg().getMsstart());
                                    tv_end_time.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GoodDetailBean goodDetailBean) {
                        this.goodDetailBean = goodDetailBean;
                    }
                });
    }

    private void initView() {
        LinearLayout ll_Is_show = getViewById(R.id.ll_Is_show);
        tv_end_time = getViewById(R.id.tv_end_time);
        tv_number = getViewById(R.id.tv_number);
        tv_time_desc = getViewById(R.id.tv_time_desc);
        if ("seckill".equals(seckill)) {
            ll_Is_show.setVisibility(View.VISIBLE);
        } else {
            ll_Is_show.setVisibility(View.GONE);
        }

        img_share = getViewById(R.id.img_share);
        img_share.setVisibility(View.INVISIBLE);
        vp_banner = getViewById(R.id.vp_banner);
        tv_current_price = getViewById(R.id.tv_current_price);
        tv_old_price = getViewById(R.id.tv_old_price);
        tv_content = getViewById(R.id.tv_content);
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tv_time = getViewById(R.id.tv_time);
        tv_num = getViewById(R.id.tv_num);
        tv_goods_specifications = getViewById(R.id.tv_goods_specifications);
        img_arrow = getViewById(R.id.img_arrow);
        tv_evaluate = getViewById(R.id.tv_evaluate);
        tv_look_all = getViewById(R.id.tv_look_all);
        img_head = getViewById(R.id.img_head);
        tv_phone_before = getViewById(R.id.tv_phone_before);
        tv_after = getViewById(R.id.tv_after);
        web_content = getViewById(R.id.web_content);
        tv_customer = getViewById(R.id.tv_customer);
        tv_join_shop_card = getViewById(R.id.tv_join_shop_card);
        tv_buy = getViewById(R.id.tv_buy);
        ll_have_evaluate = getViewById(R.id.ll_have_evaluate);
        tv_no_evaluate = getViewById(R.id.tv_no_evaluate);
        rv_tupian = getViewById(R.id.rv_tupian);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        rv_tupian.setLayoutManager(manager);

        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSupportZoom(true);
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

    private void initListener() {
        img_share.setOnClickListener(this);
        img_arrow.setOnClickListener(this);
        tv_look_all.setOnClickListener(this);
        tv_customer.setOnClickListener(this);
        tv_join_shop_card.setOnClickListener(this);
        tv_goods_specifications.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.goods_detail_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share:
                final ShareDialog sharedialog = new ShareDialog(mContext);
                TextView tv_wechat = sharedialog.findViewById(R.id.tv_wechat);
                tv_wechat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedialog.dismiss();
                        shareUrl("url", SHARE_MEDIA.WEIXIN);
                    }
                });
                TextView tv_QQ = sharedialog.findViewById(R.id.tv_QQ);
                tv_QQ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedialog.dismiss();
                        shareUrl("", SHARE_MEDIA.QQ);
                    }
                });
                sharedialog.show();
                break;
            case R.id.tv_goods_specifications:
            case R.id.img_arrow:
                selectGuige();
                break;
            case R.id.tv_look_all:
                Intent intent = new Intent(mContext, AllEvaluateActivity.class);
                intent.putExtra("goodsId", String.valueOf(data.getId()));
                startActivity(intent);
                break;
            case R.id.tv_customer:
                AppUtil.callPhone(mContext, phoneNumber);
                break;
            case R.id.tv_join_shop_card:
                if ("seckill".equals(seckill)) {
                    if (data.getMsmsg().getStates() != null) {
                        if ("1".equals(data.getMsmsg().getStates())) {
                            showToastMessage("活动未开始");
                            return;
                        }

                        if ("3".equals(data.getMsmsg().getStates())) {
                            showToastMessage("活动已结束");
                            return;
                        }
                    }
                }

                if (TextUtils.isEmpty(tv_goods_specifications.getText().toString())) {
                    showToastMessage("商品规格不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv_goods_num.getText().toString())) {
                    showToastMessage("商品数量不能为空");
                    return;
                }
                addShopCar();
                break;
            case R.id.tv_buy:
                if ("seckill".equals(seckill)) {
                    if (data.getMsmsg().getStates() != null) {
                        if ("1".equals(data.getMsmsg().getStates())) {
                            showToastMessage("活动未开始");
                            return;
                        }

                        if ("3".equals(data.getMsmsg().getStates())) {
                            showToastMessage("活动已结束");
                            return;
                        }
                    }
                }

                if (TextUtils.isEmpty(tv_goods_specifications.getText().toString().trim())) {
                    selectGuige();
                    return;
                }
                if (data.getMsmsg() != null) {
                    switch (data.getMsmsg().getStates()) {
                        case "1":
                            if ("seckill".equals(seckill)) {
                                showToastMessage("秒杀未开始");
                                return;
                            }
                            break;
                        case "3":
                            if ("seckill".equals(seckill)) {
                                showToastMessage("秒杀已结束");
                                return;
                            }
                            break;
                    }
                }

                double sumMoney = Double.valueOf(tv_current_price.getText().toString()) * Double.valueOf(tv_goods_num.getText().toString());
                PackageDetailBean detailBean = new PackageDetailBean();
                detailBean.setSum(String.valueOf(sumMoney));
                PackageDetailBean.DetailData detailData = new PackageDetailBean.DetailData();
                detailData.setGoodsId(String.valueOf(data.getId()));
                detailData.setUrl(data.getPic());
                detailData.setContent(data.getSpname());
                detailData.setGuige(tv_goods_specifications.getText().toString().trim());
                detailData.setNumber(tv_goods_num.getText().toString());
                detailData.setPrice(tv_current_price.getText().toString());
                detailBeans.add(detailData);
                detailBean.setDataList(detailBeans);
                Intent intentId = new Intent(mContext, OrderCheckActivity.class);
                intentId.putExtra("goods", detailBean);
                startActivity(intentId);
                break;
        }
    }

    private void selectGuige() {
        final GoodsDetailDialog detailDialog = new GoodsDetailDialog(mContext);
        ImageView img_tupian = detailDialog.findViewById(R.id.img_tupian);
        TextView tv_cur_price = detailDialog.findViewById(R.id.tv_current_price);
        TextView tv_old_price = detailDialog.findViewById(R.id.tv_old_price);
        final TextView tv_color = detailDialog.findViewById(R.id.tv_color);
        TextView tv_size = detailDialog.findViewById(R.id.tv_size);
        tv_size.setVisibility(View.GONE);
        TextView tv_guige = detailDialog.findViewById(R.id.tv_guige);
        TextView tv_submit = detailDialog.findViewById(R.id.tv_submit);
        RecyclerView rv_guige = detailDialog.findViewById(R.id.rv_guige);
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        final DetailItemAdapter adapter = new DetailItemAdapter(mContext, data.getGuige());
        adapter.setOnclickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                adapter.setSelect(position);
                tv_color.setText(data.getGuige().get(position));
            }
        });
        rv_guige.setLayoutManager(manager);
        rv_guige.setAdapter(adapter);
        tv_goods_num = detailDialog.findViewById(R.id.tv_num);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_goods_specifications.setText(tv_color.getText().toString());
                detailDialog.dismiss();

                double sumMoney = Double.valueOf(tv_current_price.getText().toString()) * Double.valueOf(tv_goods_num.getText().toString());
                PackageDetailBean detailBean = new PackageDetailBean();
                detailBean.setSum(String.valueOf(sumMoney));
                PackageDetailBean.DetailData detailData = new PackageDetailBean.DetailData();
                detailData.setGoodsId(String.valueOf(data.getId()));
                detailData.setUrl(data.getPic());
                detailData.setContent(data.getSpname());
                detailData.setGuige(tv_goods_specifications.getText().toString().trim());
                detailData.setNumber(tv_goods_num.getText().toString());
                detailData.setPrice(tv_current_price.getText().toString());
                detailBeans.add(detailData);
                detailBean.setDataList(detailBeans);
                Intent intentId = new Intent(mContext, OrderCheckActivity.class);
                intentId.putExtra("goods", detailBean);
                startActivity(intentId);
            }
        });
        ImageLoadUtil.GlideGoodsImageLoad(mContext, data.getPic(), img_tupian);
        tv_cur_price.setText(data.getVipjiage());
        tv_old_price.setText("¥" + data.getJiage());
        tv_guige.setText(data.getGuigename());
        tv_color.setText(data.getGuige().get(0));
        detailDialog.show();
    }

    private void addShopCar() {
        NetWork.getService(ImpService.class)
                .addShopCar(String.valueOf(data.getId()), uid, tv_goods_num.getText().toString(), tv_goods_specifications.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });

    }

    private void shareUrl(String url, SHARE_MEDIA media) {
        UMWeb umWeb = new UMWeb(url);
        umWeb.setTitle(mContext.getResources().getString(R.string.app_name));
        umWeb.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));
        umWeb.setDescription("朋友邀请你参加商品拼团");
        new ShareAction(GoodsDetailActivity.this).withMedia(umWeb)
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

    public String getNewContent(String htmltext){
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (int i = 0; i <elements.size(); i++) {
            Element element = elements.get(i);
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }

    @Override
    public void OnBannerClick(int position) {
//        showToastMessage("点击了==" + position);
    }

    @Override
    protected void onPause() {
        detailBeans.clear();
        if (data.getMsmsg() != null) {
            timer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (data.getMsmsg() != null) {
            timer.cancel();
        }
    }
}
