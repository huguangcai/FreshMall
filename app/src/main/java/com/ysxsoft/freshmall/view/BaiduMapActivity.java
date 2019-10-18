package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaiduMapActivity extends BaseActivity {

    private MapView mMapView;
    private String latitude, longitude, mallId;
    private BaiduMap mBaiduMap;
    private String[] split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        mallId = intent.getStringExtra("mallId");
        requestData();
        setBackVisibily();
        setTitle("地图");
        initView();
    }

    private void initView() {
        mMapView = getViewById(R.id.bdmap);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mMapView.showScaleControl(false);//是否显示比例尺
        mMapView.showZoomControls(false);//缩放按钮
        mBaiduMap.setMyLocationEnabled(true);
    }
    private void requestData() {
        NetWork.getService(ImpService.class)
                .MallDetailData(SpUtils.getSp(mContext, "uid"), mallId, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallDetailBean>() {
                    private MallDetailBean mallDetailBean;

                    @Override
                    public void onCompleted() {
                        if (mallDetailBean.getCode() == 0) {
                            MallDetailBean.DataBean data = mallDetailBean.getData();
                            split = data.getDpjwd().split(",");
//                            LatLng point = new LatLng(Float.valueOf(split[0]), Float.valueOf(split[1]));
                            //定义Maker坐标点
                            String s = split[0];//113.611325
                            String s1 = split[1];//34.801765

                            LatLng point = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
                            //构建Marker图标
                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.img_dingwei);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
                            mBaiduMap.addOverlay(option);

                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(point)
                                    .zoom(15)
                                    .build();
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//改变地图状态
                            mBaiduMap.setMapStatus(mMapStatusUpdate);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MallDetailBean mallDetailBean) {

                        this.mallDetailBean = mallDetailBean;
                    }
                });

    }
    @Override
    public int getLayout() {
        return R.layout.baidu_map_layout;
    }
}
