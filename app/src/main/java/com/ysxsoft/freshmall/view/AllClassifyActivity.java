package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AllClassifyActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rv_title, rv_content;
    private TextView tv_title_right;
    private RelativeLayout rl_search;
    private ImageView title_search_left;
    private EditText ed_title_search;
    private String uid;
    private List<GetTypeListBean.DataBean> data;
    private AllClassifyTitleAdapter titleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        setBackVisibily();
        setTitle("全部商品");
        initData("");
        initView();
        initListener();
    }

    private void initData(final String id) {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, id, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            data = getTypeListBean.getData();
                            titleAdapter = new AllClassifyTitleAdapter(mContext, data);
                            rv_title.setAdapter(titleAdapter);
                            String lid = data.get(0).getId();
                            RequestData(lid,data.get(0).getTypename());
                            titleAdapter.setOnItemClickListener(new AllClassifyTitleAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    titleAdapter.setSelect(i);
                                    RequestData(data.get(i).getId(),data.get(i).getTypename());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetTypeListBean getTypeListBean) {

                        this.getTypeListBean = getTypeListBean;
                    }
                });
    }

    private void RequestData(String lid, final String name) {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, lid, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode()==0){
                            final List<GetTypeListBean.DataBean> data = getTypeListBean.getData();
                            final AllClassifyContentAdapter contentAdapter=new AllClassifyContentAdapter(mContext,data);
                            rv_content.setAdapter(contentAdapter);
                            contentAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Intent intent=new Intent(mContext,SearchResultActivity.class);
                                    String typename = data.get(position).getTypename();
//                                    intent.putExtra("content", name);
                                    intent.putExtra("lid1", contentAdapter.data.get(position).getId());
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

    private void initListener() {
        tv_title_right.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        title_search_left.setOnClickListener(this);
        ed_title_search.setOnClickListener(this);
    }

    private void initView() {
        tv_title_right = getViewById(R.id.tv_title_right1);
        tv_title_right.setText("搜索");
        rl_search = getViewById(R.id.rl_search);
        title_search_left = getViewById(R.id.title_search_left);
        ed_title_search = getViewById(R.id.ed_title_search);
        ed_title_search.setFocusable(false);

        rv_title = getViewById(R.id.rv_title);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv_title.setLayoutManager(manager);
        rv_title.addItemDecoration(new DividerItemDecoration(mContext, OrientationHelper.VERTICAL));


        rv_content = getViewById(R.id.rv_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rv_content.setLayoutManager(gridLayoutManager);

    }

    @Override
    public int getLayout() {
        return R.layout.all_classify_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right1:
            case R.id.rl_search:
            case R.id.title_search_left:
            case R.id.ed_title_search:
                startActivity(SearchDataActivity.class);
                break;
        }
    }
}
