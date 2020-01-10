package com.ysxsoft.freshmall.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
//        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        /**
         * s:  友盟   apkey
         * s1: 友盟 channel
         * 设备类型  UMConfigure.DEVICE_TYPE_PHONE
         * s2 : Push推送业务的secret
         */
//        UMConfigure.init(this, "5c417c9af1f55683a20015ae", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.init(this, "5e17d470cb23d2e4640004fe", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    {
//        PlatformConfig.setWeixin("wxc523eebce9e041f3", "01fbabed46e4635ec9f6de8090d8c7d6");//s:  appId   s1: AppSecret
        PlatformConfig.setWeixin("wx4e6f9ac9432f741d", "01fbabed46e4635ec9f6de8090d8c7d6");//s:  appId   s1: AppSecret
//        PlatformConfig.setQQZone("101541625", "e0dff3680171cef4cd0a5776f169fd29");//qq 的s:APP ID   s1：APP Key
        PlatformConfig.setQQZone("1109140159", "xCYe0zhnDMonNPy4");//qq 的s:APP ID   s1：APP Key
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
