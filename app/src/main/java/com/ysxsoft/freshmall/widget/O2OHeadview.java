package com.ysxsoft.freshmall.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.HomeGoodsBannerAdapter;
import com.ysxsoft.freshmall.adapter.O2OHeadviewAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.AllClassifyActivity;
import com.ysxsoft.freshmall.view.ClassifyGoodsActivity;
import com.ysxsoft.freshmall.view.O2OMoreClassifyActivity;
import com.ysxsoft.freshmall.view.O2OMoreClassifyListActivity;
import com.ysxsoft.freshmall.view.O2OSecKillActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OHeadview extends AbsLinearLayout {

    private MyRecyclerView recyclerView;
    private String uid;
    private List<GetTypeListBean.DataBean> data;

    public O2OHeadview(Context context) {
        super(context);
        uid = SpUtils.getSp(context, "uid");
        ClassifyData();
    }

    private void ClassifyData() {
        if (TextUtils.isEmpty(uid)||uid==null){
            uid="";
        }
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, "", "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            data = getTypeListBean.getData();
                            if (data.size()<=0||data==null){
                                return;
                            }
                            O2OHeadviewAdapter  adapter = new O2OHeadviewAdapter(getContext(), data);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    if (position == 9) {
                                        Intent intent = new Intent(getContext(), O2OMoreClassifyActivity.class);
                                        getContext().startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getContext(), O2OMoreClassifyListActivity.class);
                                        intent.putExtra("id", data.get(position).getId());
                                        intent.putExtra("name", data.get(position).getTypename());
                                        getContext().startActivity(intent);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetTypeListBean getTypeListBean) {

                        this.getTypeListBean = getTypeListBean;
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.o2o_headview_layout;
    }

    @Override
    protected void initView() {
        recyclerView = getViewById(R.id.recyclerView);
        ImageView img_seckill = getViewById(R.id.img_seckill);
        TextView tv_nearby_more = getViewById(R.id.tv_nearby_more);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(manager);

        img_seckill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), O2OSecKillActivity.class));
            }
        });
        tv_nearby_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), O2OMoreClassifyListActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
