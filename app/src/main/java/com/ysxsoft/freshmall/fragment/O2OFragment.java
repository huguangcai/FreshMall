package com.ysxsoft.freshmall.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.luck.picture.lib.permissions.RxPermissions;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.O2OFragmentAdapter;
import com.ysxsoft.freshmall.adapter.O2OHeadviewAdapter;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.NearByMallBean;
import com.ysxsoft.freshmall.modle.O2OPicBean;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.LocationActivity;
import com.ysxsoft.freshmall.view.LoginActivity;
import com.ysxsoft.freshmall.view.MyOrderActivity;
import com.ysxsoft.freshmall.view.O2OMallDetailActivity;
import com.ysxsoft.freshmall.view.O2OMoreClassifyActivity;
import com.ysxsoft.freshmall.view.O2OMoreClassifyListActivity;
import com.ysxsoft.freshmall.view.SearchDataActivity;
import com.ysxsoft.freshmall.widget.O2OHeadview;

import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class O2OFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_location;
    private TextView tv_title_right, tv_location;
    private EditText ed_title_search;
    private RelativeLayout rl_search;
    private FloatingActionButton fab;
    private RxPermissions rxPermissions;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private RecyclerView recyclerView;
    private double latitude;
    private double longitude;
    private List<GetTypeListBean.DataBean> data;
    private String picurl;

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
                            initBdMap();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToastMessage("未授权权限，部分功能不能使用");
                        }
                    }
                });
        int stateBar = getStateBar();
        LinearLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        ll_location = getViewById(R.id.ll_location);
        tv_location = getViewById(R.id.tv_location);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("搜索");
        ed_title_search = getViewById(R.id.ed_title_search);
        ed_title_search.setHint("请输入店铺名称");
        rl_search = getViewById(R.id.rl_search);
        fab = getViewById(R.id.fab);
        recyclerView = getViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        requestPic();
        ClassifyData();

    }

    private void requestPic() {
        NetWork.getService(ImpService.class)
                .O2OPic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OPicBean>() {
                    private O2OPicBean o2OPicBean;

                    @Override
                    public void onCompleted() {
                        if (o2OPicBean.getCode() == 0) {
                            picurl = o2OPicBean.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(O2OPicBean o2OPicBean) {

                        this.o2OPicBean = o2OPicBean;
                    }
                });
    }

    private void ClassifyData() {
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

    private void requestData(double latitude, double longitude) {
        NetWork.getService(ImpService.class)
                .nearByMall(String.valueOf(longitude), String.valueOf(latitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NearByMallBean>() {
                    private NearByMallBean nearByMallBean;
                    @Override
                    public void onCompleted() {
                        if (nearByMallBean.getCode() == 0) {
                            final O2OFragmentAdapter adapter = new O2OFragmentAdapter(getActivity(), nearByMallBean.getData(), data, picurl);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnGrideItemClickListener(new O2OFragmentAdapter.OnGrideItemClickListener() {
                                @Override
                                public void grideItemClick(int position) {
                                    if (TextUtils.isEmpty(SpUtils.getSp(getActivity(), "uid")) || SpUtils.getSp(getActivity(), "uid") == null) {
                                        startActivity(LoginActivity.class);
                                    } else {
                                        if (position == 7) {
                                            Intent intent = new Intent(getActivity(), O2OMoreClassifyActivity.class);
                                            intent.putExtra("latitude", String.valueOf(O2OFragment.this.latitude));
                                            intent.putExtra("longitude", String.valueOf(O2OFragment.this.longitude));
                                            intent.putExtra("o2o", "o2o");
                                            getActivity().startActivity(intent);
                                        } else {
                                            //更多
                                            GetTypeListBean.DataBean dataBean = adapter.dataBeans.get(position);
                                            Intent intent = new Intent(getActivity(), O2OMoreClassifyListActivity.class);
                                            intent.putExtra("latitude", String.valueOf(O2OFragment.this.latitude));
                                            intent.putExtra("longitude", String.valueOf(O2OFragment.this.longitude));
                                            intent.putExtra("sid1", dataBean.getId());
                                            getActivity().startActivity(intent);
                                        }
                                    }
                                }
                            });

                            adapter.setOnNormalClickListener(new O2OFragmentAdapter.OnNormalClickListener() {
                                @Override
                                public void itemClick(int position) {
                                    if (TextUtils.isEmpty(SpUtils.getSp(getActivity(), "uid")) || SpUtils.getSp(getActivity(), "uid") == null) {
                                        startActivity(LoginActivity.class);
                                    } else {
                                        NearByMallBean.DataBean dataBean = adapter.data.get(position);
                                        Intent intent = new Intent(getActivity(), O2OMallDetailActivity.class);
                                        intent.putExtra("latitude", String.valueOf(O2OFragment.this.latitude));
                                        intent.putExtra("longitude", String.valueOf(O2OFragment.this.longitude));
                                        intent.putExtra("mallId", dataBean.getId());
                                        getActivity().startActivity(intent);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage("O2O 获取数据"+e.getMessage());
                    }

                    @Override
                    public void onNext(NearByMallBean nearByMallBean) {

                        this.nearByMallBean = nearByMallBean;
                    }
                });
    }

    private void initBdMap() {
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        //注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);


        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(3000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            String city = location.getCity();    //获取城市
            tv_location.setText(city);
            requestData(latitude,longitude);
        }
    }

    @Override
    protected void initListenser() {
        ll_location.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.o2o_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_location:
                Intent intent1 = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(intent1, 110);
                break;
            case R.id.tv_title_right:
                if (TextUtils.isEmpty(ed_title_search.getText().toString())) {
                    showToastMessage("店铺名不能为空");
                    return;
                }
                Intent intent = new Intent(getActivity(), O2OMoreClassifyListActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("content", ed_title_search.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.fab:
                if (TextUtils.isEmpty(SpUtils.getSp(getActivity(), "uid")) || SpUtils.getSp(getActivity(), "uid") == null) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyOrderActivity.class);
                }
                break;
        }
    }
    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    return;
                } else {
                    latitude = geoCodeResult.getLocation().latitude;
                    longitude = geoCodeResult.getLocation().longitude;
                }
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            latitude = reverseGeoCodeResult.getLocation().latitude;
            longitude = reverseGeoCodeResult.getLocation().longitude;
        }
    };
    private GeoCoder geoCoder;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 110 && resultCode == 101) {
            tv_location.setText(data.getStringExtra("name"));
            geoCoder = GeoCoder.newInstance();
            geoCoder.setOnGetGeoCodeResultListener(listener);
            geoCoder.geocode(new GeoCodeOption()
                    .city(data.getStringExtra("name"))
                    .address(data.getStringExtra("name")+"市政府"));
        }
    }
}
