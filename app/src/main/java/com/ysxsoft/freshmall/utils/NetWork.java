package com.ysxsoft.freshmall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetWork {

//    public static String BaseUrl = "https://oto.sanzhima.cn/index.php/api/";
    public static String BaseUrl = "http://o2o.ysxapp.com/index.php/api/";
//    public static String BaseUrl = "http://192.168.1.163:8888/index.php/api/";
//    public static String BaseUrl = "http://192.168.1.164:8888/index.php/api/";


    //获取原生数据的网络请求
    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 获取Service对象
     */
    public static <T extends Object> T getService(Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))//2019-4-49 modify
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);
    }
    /**
     * 图文 上传
     * @param map
     * @param imageFile
     * @return
     */
    public static MultipartBody.Builder builder(Map<String, String> map, ArrayList<String> imageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map != null) {
            Set<String> keys = map.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.addFormDataPart(key, map.get(key));
            }
        }
        if (imageFile != null && imageFile.size() > 0) {
            for (int i = 0; i < imageFile.size(); i++) {
                builder.addFormDataPart("exit_pics[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }
}
