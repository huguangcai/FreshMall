package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ClassGoodsItemAdapter;
import com.ysxsoft.freshmall.adapter.ClassifyGoodsAdapter;
import com.ysxsoft.freshmall.adapter.HomeGoodsBannerAdapter;
import com.ysxsoft.freshmall.fragment.AllOrderFragment;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.TypeShopListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.MyViewPager;
import com.ysxsoft.freshmall.widget.slidingtablayout.OnTabSelectListener;
import com.ysxsoft.freshmall.widget.slidingtablayout.SlidingTabLayout;
import com.ysxsoft.freshmall.widget.slidingtablayout.ViewFindUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassifyGoodsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ImageView img_title_right;
    private View decorView;
    private String uid;
    private ViewPager vpall_content;
    private SlidingTabLayout stlall_tab;
    private List<GetTypeListBean.DataBean> alldata;
    private RecyclerView recyclerView;
    private String goods_id;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private ClassGoodsItemAdapter mDataAdapter;
    private TypeShopListBean typeShopListBean;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        uid = SpUtils.getSp(mContext, "uid");
        decorView = getWindow().getDecorView();
        setBackVisibily();
        setTitle("分类商品");
        initView();
        RequestData();
        initListener();
    }

    private void RequestData() {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, "", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            alldata = getTypeListBean.getData();
                            GetTypeListBean.DataBean dataBean = new GetTypeListBean.DataBean();
                            for (int i = 0; i < alldata.size(); i++) {
                                mFragments.add(new AllOrderFragment());
                            }
                            MyPagerAdapter alladapter = new MyPagerAdapter(getSupportFragmentManager(), alldata);
                            vpall_content.setAdapter(alladapter);
                            stlall_tab.setViewPager(vpall_content);
                            if (!TextUtils.isEmpty(position) && position != null) {
                                stlall_tab.setCurrentTab(Integer.valueOf(position));
                                RequestSecondData(alldata.get(Integer.valueOf(position)).getId());
                            } else {
                                stlall_tab.setCurrentTab(0);
                                RequestSecondData(alldata.get(0).getId());
                            }
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
        img_title_right = getViewById(R.id.img_title_right);
        stlall_tab = ViewFindUtils.find(decorView, R.id.stlall_tab);
        vpall_content = getViewById(R.id.vpall_content);
        stlall_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                goods_id = "";
                RequestSecondData(alldata.get(position).getId());
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        recyclerView = getViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new ClassGoodsItemAdapter(mContext);
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new com.github.jdsjlzx.interfaces.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TypeShopListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("goodsId", String.valueOf(dataBean.getId()));
                startActivity(intent);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (typeShopListBean != null) {
                    if (page < typeShopListBean.getPages()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
        onRefresh();
    }

    private void RequestSecondData(String id) {
        NetWork.getService(ImpService.class)
                .GetTypeListData(uid, id, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetTypeListBean>() {
                    private GetTypeListBean getTypeListBean;

                    @Override
                    public void onCompleted() {
                        if (getTypeListBean.getCode() == 0) {
                            final List<GetTypeListBean.DataBean> data = getTypeListBean.getData();
                            GetTypeListBean.DataBean dataBean = new GetTypeListBean.DataBean();
                            final ClassifyGoodsAdapter adapter = new ClassifyGoodsAdapter(mContext, data);
                            recyclerView.setAdapter(adapter);
                            if (data.size() <= 0) {
                                goods_id = "";
                                mSwipeRefreshLayout.setRefreshing(false);
                                mDataAdapter.clear();
                                mDataAdapter.notifyDataSetChanged();
//                                notifyDataSetChanged();
                                return;
                            }
                            goods_id = data.get(0).getId();
                            initRecycleView();
                            adapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    adapter.setSelect(position);
                                    goods_id = data.get(position).getId();
                                    onRefresh();
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

    private void initListener() {
        img_title_right.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.classify_goods_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_title_right:
                startActivity(SearchDataActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(mContext)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {

        private WeakReference<ClassifyGoodsActivity> ref;

        PreviewHandler(ClassifyGoodsActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final ClassifyGoodsActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData(goods_id);
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(typeShopListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(typeShopListBean.getData().size());
                                activity.notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getData(String goods_id) {
        NetWork.getService(ImpService.class)
                .getTypeShopList(uid, goods_id, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TypeShopListBean>() {
                    private TypeShopListBean typeShopListBean;

                    @Override
                    public void onCompleted() {
                        if (typeShopListBean.getCode() == 0) {
                            showData(typeShopListBean);
                            List<TypeShopListBean.DataBean> data = typeShopListBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(TypeShopListBean typeShopListBean) {

                        this.typeShopListBean = typeShopListBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(TypeShopListBean typeShopListBean) {

        this.typeShopListBean = typeShopListBean;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<GetTypeListBean.DataBean> data;

        public MyPagerAdapter(FragmentManager fm, List<GetTypeListBean.DataBean> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position).getTypename();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
