package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.HomeGoodsBannerAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.HomeLunBoBean;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.AllClassifyActivity;
import com.ysxsoft.freshmall.view.ClassifyGoodsActivity;
import com.ysxsoft.freshmall.widget.banner.Banner;
import com.ysxsoft.freshmall.widget.banner.GlideImageLoader;
import com.ysxsoft.freshmall.widget.banner.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeGoodsBanner extends AbsLinearLayout implements OnBannerListener {

    private Banner vp_banner;
    private MyRecyclerView recyclerView;
    private ArrayList<String> urls = new ArrayList<>();
    private final String uid;
    private HomeGoodsBannerAdapter adapter;
    private List<GetTypeListBean.DataBean> data;

    public HomeGoodsBanner(Context context) {
        super(context);
        uid = SpUtils.getSp(context, "uid");
        LunBoData();
        ClassifyData();
    }
    private void LunBoData() {
        NetWork.getService(ImpService.class)
                .LunBoData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeLunBoBean>() {
                    private HomeLunBoBean homeLunBoBean;

                    @Override
                    public void onCompleted() {
                        if (homeLunBoBean.getCode()==0){
                            List<HomeLunBoBean.DataBean> data = homeLunBoBean.getData();
                            for (int i = 0; i < data.size(); i++) {
                                urls.add(data.get(i).getBanner());
                            }
                            vp_banner.setImages(urls)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(HomeGoodsBanner.this)
                                    .start();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeLunBoBean homeLunBoBean) {

                        this.homeLunBoBean = homeLunBoBean;
                    }
                });
    }

    /**
     * 十宫格分类
     */
    private void ClassifyData() {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid,"","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode()==0){
                            data = getTypeListBean.getData();
                            adapter = new HomeGoodsBannerAdapter(getContext(), data);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    if (position==9){
                                        Intent intent=new Intent(getContext(),AllClassifyActivity.class);
                                        getContext().startActivity(intent);
                                    }else {
                                        Intent intent=new Intent(getContext(), ClassifyGoodsActivity.class);
                                        intent.putExtra("position",String.valueOf(position));
                                        getContext().startActivity(intent);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(GetTypeListBean getTypeListBean) {

                        this.getTypeListBean = getTypeListBean;
                    }
                });
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.home_goods_banner_layout;
    }

    @Override
    protected void initView() {
        vp_banner = getViewById(R.id.vp_banner);
        recyclerView = getViewById(R.id.recyclerView);
        GridLayoutManager manager=new GridLayoutManager(getContext(),5);
        recyclerView.setLayoutManager(manager);

    }

    @Override
    public void OnBannerClick(int position) {
//        Toast.makeText(getContext(),"点击了=="+position,Toast.LENGTH_SHORT).show();
    }


}
