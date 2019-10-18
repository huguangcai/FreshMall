package com.ysxsoft.freshmall.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * O2O商家详情  商家的Fragment
 */
public class O2OMallFragment extends BaseFragment {

    private TextView tv_type, tv_phone_num, tv_open_mall_time, tv_mall_address;
    private MallDetailBean.DataBean data;
    private MapView mMapView;
    private String latitude, longitude, mallId;
    private BaiduMap mBaiduMap;
    private String[] split;

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            latitude = arguments.getString("latitude");
            longitude = arguments.getString("longitude");
            mallId = arguments.getString("mallId");
            requestData();
        }
        tv_type = getViewById(R.id.tv_type);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_open_mall_time = getViewById(R.id.tv_open_mall_time);
        tv_mall_address = getViewById(R.id.tv_mall_address);

        mMapView = getViewById(R.id.bdmap);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mMapView.showScaleControl(false);//是否显示比例尺
        mMapView.showZoomControls(false);//缩放按钮
        mBaiduMap.setMyLocationEnabled(true);

        tv_phone_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.callPhone(getActivity(), tv_phone_num.getText().toString());
            }
        });
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .MallDetailData(SpUtils.getSp(getActivity(), "uid"), mallId, latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallDetailBean>() {
                    private MallDetailBean mallDetailBean;

                    @Override
                    public void onCompleted() {
                        if (mallDetailBean.getCode() == 0) {
                            data = mallDetailBean.getData();
                            tv_type.setText(data.getLeixing());
                            tv_phone_num.setText(data.getLxphone());
                            tv_open_mall_time.setText(data.getKdtime());
                            tv_mall_address.setText(data.getDpjwd_address());
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
    protected int getLayoutResId() {
        return R.layout.o2o_mall_fragment_layout;
    }

}
