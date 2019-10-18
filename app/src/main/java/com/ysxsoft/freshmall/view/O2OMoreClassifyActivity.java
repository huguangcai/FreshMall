package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.AllClassifyContentAdapter;
import com.ysxsoft.freshmall.adapter.AllClassifyTitleAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OMoreClassifyActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_right1;
    private RelativeLayout rl_search;
    private ImageView title_search_left;
    private EditText ed_title_search;
    private RecyclerView rv_title, rv_content;
    private String uid,latitude,longitude,o2o;
    private List<GetTypeListBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        o2o = intent.getStringExtra("o2o");
        setBackVisibily();
        setTitle("全部分类");
        if ("o2o".equals(o2o)&&!TextUtils.isEmpty(o2o)&&o2o!=null){
            o2oData();
        }else {
            initData("");
        }
        initView();
        initListener();
    }

    private void o2oData() {
        NetWork.getService(ImpService.class)
                .O2OClassifyData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            data = getTypeListBean.getData();
                            final AllClassifyTitleAdapter  titleAdapter = new AllClassifyTitleAdapter(mContext, data);
                            rv_title.setAdapter(titleAdapter);
                            String lid = data.get(0).getId();
                            Requesto2oData(lid);
                            titleAdapter.setOnItemClickListener(new AllClassifyTitleAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    titleAdapter.setSelect(i);
                                    Requesto2oData(data.get(i).getId());
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

    private void Requesto2oData(String lid) {
        NetWork.getService(ImpService.class)
                .O2OErClassifyData(lid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode()==0) {
                            final List<GetTypeListBean.DataBean> data = getTypeListBean.getData();
                            AllClassifyContentAdapter contentAdapter = new AllClassifyContentAdapter(mContext, data);
                            rv_content.setAdapter(contentAdapter);
                            contentAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Intent intent = new Intent(mContext, O2OMoreClassifyListActivity.class);
                                    intent.putExtra("latitude", String.valueOf(latitude));
                                    intent.putExtra("longitude", String.valueOf(longitude));
                                    intent.putExtra("sid2", data.get(position).getId());
                                    startActivity(intent);
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

    private void initData(String id) {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, id, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            data = getTypeListBean.getData();
                            final AllClassifyTitleAdapter  titleAdapter = new AllClassifyTitleAdapter(mContext, data);
                            rv_title.setAdapter(titleAdapter);
                            String lid = data.get(0).getId();
                            RequestData(lid);
                            titleAdapter.setOnItemClickListener(new AllClassifyTitleAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    titleAdapter.setSelect(i);
                                    RequestData(data.get(i).getId());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetTypeListBean getTypeListBean) {

                        this.getTypeListBean = getTypeListBean;
                    }
                });
    }

    private void RequestData(String lid) {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, lid, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;
                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode()==0){
                            final List<GetTypeListBean.DataBean> data = getTypeListBean.getData();
                            AllClassifyContentAdapter contentAdapter=new AllClassifyContentAdapter(mContext,data);
                            rv_content.setAdapter(contentAdapter);
                            contentAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Intent intent=new Intent(mContext,O2OMoreClassifyListActivity.class);
                                    intent.putExtra("latitude", String.valueOf(latitude));
                                    intent.putExtra("longitude", String.valueOf(longitude));
                                    intent.putExtra("sid2", data.get(position).getId());
                                    startActivity(intent);
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

    private void initView() {
        tv_title_right1 = getViewById(R.id.tv_title_right1);
        tv_title_right1.setText("搜索");
        rl_search = getViewById(R.id.rl_search);
        title_search_left = getViewById(R.id.title_search_left);
        ed_title_search = getViewById(R.id.ed_title_search);
        rv_title = getViewById(R.id.rv_title);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv_title.setLayoutManager(manager);
        rv_title.addItemDecoration(new DividerItemDecoration(mContext, OrientationHelper.VERTICAL));

        rv_content = getViewById(R.id.rv_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rv_content.setLayoutManager(gridLayoutManager);
    }

    private void initListener() {
        tv_title_right1.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.o2o_more_classify_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_title_right1:
                if (TextUtils.isEmpty(ed_title_search.getText().toString())){
                    showToastMessage("店铺名不能为空");
                    return;
                }
                Intent intent = new Intent(mContext, O2OMoreClassifyListActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("content",ed_title_search.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }
}
