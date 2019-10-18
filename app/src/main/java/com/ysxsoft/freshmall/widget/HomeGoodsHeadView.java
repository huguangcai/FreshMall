package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.HomeBestAdapter;
import com.ysxsoft.freshmall.adapter.HomeNewAdapter;
import com.ysxsoft.freshmall.adapter.HomeSeckillAdapter;
import com.ysxsoft.freshmall.com.GallerySnapHelper;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.HomeBestBean;
import com.ysxsoft.freshmall.modle.HomeNewBean;
import com.ysxsoft.freshmall.modle.HomeSeckillBean;
import com.ysxsoft.freshmall.modle.HomelistBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.view.ClassifyGoodsActivity;
import com.ysxsoft.freshmall.view.GoodsDetailActivity;
import com.ysxsoft.freshmall.view.MyVipActivity;
import com.ysxsoft.freshmall.view.SecKillActivity;
import com.ysxsoft.freshmall.widget.banner.Banner;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeGoodsHeadView extends AbsLinearLayout {


    private ImageView img_vip_favourable;
    private TextView tv_start_time, tv_hour, tv_minute, tv_second, tv_seckill_more, tv_xinpin_more,
            tv_youxuan_more, tv_title, tv_name, tv_vip_price, tv_old_price, tv_title1, tv_name2,
            tv_vip_price2, tv_old_price2, tv_title3, tv_name3, tv_vip_price3, tv_old_price3;
    private MyRecyclerView rv_seckill, rv_xinpin, rv_youxuan;
    private Context context;
    private LinearLayout ll_home_1, ll_home_2, ll_home_3;
    private ImageView img_tupian1, img_tupian2, img_tupian3;

    public HomeGoodsHeadView(Context context) {
        super(context);
        this.context = context;
        requestData();
        requestSeckillData();
        requestNewData();
        requestBestData();
    }

    private void requestBestData() {
        NetWork.getService(ImpService.class)
                .HomeBestData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeBestBean>() {
                    private HomeBestBean homeBestBean;

                    @Override
                    public void onCompleted() {
                        if (homeBestBean.getCode() == 0) {
                            List<HomeBestBean.DataBean> data = homeBestBean.getData();
//                            GallerySnapHelper snapHelper = new GallerySnapHelper();
//                            rv_youxuan.setOnFlingListener(null);
//                            snapHelper.attachToRecyclerView(rv_youxuan);
                            final HomeBestAdapter adapter=new HomeBestAdapter(context,data);
                            rv_youxuan.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeBestAdapter.OnItemClickListener() {
                                @Override
                                public void itemClick(int position) {
                                    String id = adapter.data.get(position).getId();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
                                    intent.putExtra("goodsId",id);
                                    context.startActivity(intent);
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeBestBean homeBestBean) {

                        this.homeBestBean = homeBestBean;
                    }
                });
    }

    private void requestNewData() {
        NetWork.getService(ImpService.class)
                .HomeNewData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeNewBean>() {
                    private HomeNewBean homeNewBean;

                    @Override
                    public void onCompleted() {
                        if (homeNewBean.getCode() == 0) {
                            List<HomeNewBean.DataBean> data = homeNewBean.getData();
                            final HomeNewAdapter adapter = new HomeNewAdapter(context, data);
                            rv_xinpin.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeNewAdapter.OnItemClickListener() {
                                @Override
                                public void itemClick(int position) {
                                    String id = adapter.data.get(position).getId();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
                                    intent.putExtra("goodsId",id);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeNewBean homeNewBean) {

                        this.homeNewBean = homeNewBean;
                    }
                });
    }

    private void requestSeckillData() {
        NetWork.getService(ImpService.class)
                .HomeSeckillData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeSeckillBean>() {
                    private HomeSeckillBean homeSeckillBean;

                    @Override
                    public void onCompleted() {
                        if (homeSeckillBean.getCode() == 0) {
                            HomeSeckillBean.DataBean data = homeSeckillBean.getData();
                            List<HomeSeckillBean.DataBean.SplistBean> splist = data.getSplist();
                            switch (data.getStates()) {
                                case "1":
                                    tv_start_time.setText(data.getMsstart());
                                    break;
                                case "2":
                                    tv_start_time.setText(data.getMsstart());
                                    break;
                                case "3":
                                    tv_start_time.setText(data.getMsstart());
                                    break;
                            }
                            int time = Integer.valueOf(data.getSytime());
                            int i = time * 1000;
                            CountDownTimer timer=new CountDownTimer(Long.valueOf(String.valueOf(i)),1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
//                                    String s = AppUtil.FormarTime(AppUtil.AppTime.All, millisUntilFinished);
                                    tv_hour.setText(AppUtil.secToTime(Integer.valueOf(String.valueOf(millisUntilFinished)) / 1000));
                                }

                                @Override
                                public void onFinish() {
                                    tv_hour.setText("00:00:00");
                                }
                            };
                            timer.start();
                            tv_minute.setVisibility(GONE);
                            tv_second.setVisibility(GONE);

                            GallerySnapHelper snapHelper = new GallerySnapHelper();
                            rv_seckill.setOnFlingListener(null);
                            snapHelper.attachToRecyclerView(rv_seckill);
                            final HomeSeckillAdapter adapter = new HomeSeckillAdapter(context, splist);
                            rv_seckill.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeSeckillAdapter.OnItemClickListener() {
                                @Override
                                public void itemClick(int position) {
                                    String id = adapter.splist.get(position).getId();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
//                                    intent.putExtra("goodsId",id);
                                    intent.putExtra("goodsId", id);
                                    intent.putExtra("seckill", "seckill");
                                    context.startActivity(intent);
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeSeckillBean homeSeckillBean) {

                        this.homeSeckillBean = homeSeckillBean;
                    }
                });

    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .Homelist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomelistBean>() {
                    private HomelistBean homelistBean;

                    @Override
                    public void onCompleted() {
                        if (homelistBean.getCode() == 0) {
                            if (homelistBean.getData().size()==0){
                                return;
                            }
                            final HomelistBean.DataBean dataBean = homelistBean.getData().get(0);
                            ImageLoadUtil.GlideGoodsImageLoad(context, dataBean.getFmtp(), img_tupian1);
                            tv_title.setText(dataBean.getTitle());
                            tv_name.setText(dataBean.getSpname());
                            tv_vip_price.setText("¥"+dataBean.getVipjiage());
                            tv_old_price.setText("¥"+dataBean.getJiage());
                            tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            ll_home_1.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String spid = dataBean.getSpid();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
                                    intent.putExtra("goodsId",spid);
                                    context.startActivity(intent);
                                }
                            });
                            if (homelistBean.getData().size()==1){
                                return;
                            }

                            final HomelistBean.DataBean dataBean1 = homelistBean.getData().get(1);
                            ImageLoadUtil.GlideGoodsImageLoad(context, dataBean1.getFmtp(), img_tupian2);
                            tv_title1.setText(dataBean1.getTitle());
                            tv_name2.setText(dataBean1.getSpname());
                            tv_vip_price2.setText("¥"+dataBean1.getVipjiage());
                            tv_old_price2.setText("¥"+dataBean1.getJiage());
                            tv_old_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            ll_home_2.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String spid = dataBean1.getSpid();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
                                    intent.putExtra("goodsId",spid);
                                    context.startActivity(intent);
                                }
                            });
                            if (homelistBean.getData().size()==2){
                                return;
                            }

                            final HomelistBean.DataBean dataBean2 = homelistBean.getData().get(2);
                            ImageLoadUtil.GlideGoodsImageLoad(context, dataBean2.getFmtp(), img_tupian3);
                            tv_title3.setText(dataBean2.getTitle());
                            tv_name3.setText(dataBean2.getSpname());
                            tv_vip_price3.setText("¥"+dataBean2.getVipjiage());
                            tv_old_price3.setText("¥"+dataBean2.getJiage());
                            tv_old_price3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                              ll_home_3.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String spid = dataBean2.getSpid();
                                    Intent intent=new Intent(context,GoodsDetailActivity.class);
                                    intent.putExtra("goodsId",spid);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomelistBean homelistBean) {

                        this.homelistBean = homelistBean;
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_goods_fragment_headview_layout;
    }

    @Override
    protected void initView() {
        img_tupian1 = getViewById(R.id.img_tupian1);
        img_tupian2 = getViewById(R.id.img_tupian2);
        img_tupian3 = getViewById(R.id.img_tupian3);
        ll_home_1 = getViewById(R.id.ll_home_1);
        ll_home_2 = getViewById(R.id.ll_home_2);
        ll_home_3 = getViewById(R.id.ll_home_3);
        tv_title = getViewById(R.id.tv_title);
        tv_name = getViewById(R.id.tv_name);
        tv_vip_price = getViewById(R.id.tv_vip_price);
        tv_old_price = getViewById(R.id.tv_old_price);
        tv_title1 = getViewById(R.id.tv_title1);
        tv_name2 = getViewById(R.id.tv_name2);
        tv_vip_price2 = getViewById(R.id.tv_vip_price2);
        tv_old_price2 = getViewById(R.id.tv_old_price2);
        tv_title3 = getViewById(R.id.tv_title3);
        tv_name3 = getViewById(R.id.tv_name3);
        tv_vip_price3 = getViewById(R.id.tv_vip_price3);
        tv_old_price3 = getViewById(R.id.tv_old_price3);

        img_vip_favourable = getViewById(R.id.img_vip_favourable);
        tv_start_time = getViewById(R.id.tv_start_time);
        tv_hour = getViewById(R.id.tv_hour);
        tv_minute = getViewById(R.id.tv_minute);
        tv_second = getViewById(R.id.tv_second);
        tv_seckill_more = getViewById(R.id.tv_seckill_more);
        rv_seckill = getViewById(R.id.rv_seckill);
        tv_xinpin_more = getViewById(R.id.tv_xinpin_more);
        rv_xinpin = getViewById(R.id.rv_xinpin);
        tv_youxuan_more = getViewById(R.id.tv_youxuan_more);
        rv_youxuan = getViewById(R.id.rv_youxuan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_seckill.setLayoutManager(layoutManager);

        LinearLayoutManager Manager = new LinearLayoutManager(context);
        Manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_youxuan.setLayoutManager(Manager);

        GridLayoutManager manager = new GridLayoutManager(context, 3);
        rv_xinpin.setLayoutManager(manager);
        img_vip_favourable.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyVipActivity.class));
            }
        });
        tv_seckill_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SecKillActivity.class));
            }
        });
        tv_xinpin_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ClassifyGoodsActivity.class));
            }
        });
        tv_youxuan_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ClassifyGoodsActivity.class));
            }
        });
    }
}
