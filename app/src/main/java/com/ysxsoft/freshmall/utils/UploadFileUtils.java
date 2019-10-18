package com.ysxsoft.freshmall.utils;

import com.baidu.mapapi.http.HttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class UploadFileUtils {

    /**
     * 图文上传
     *
     * @param photos
     * @param resultListener
     */
    public static void submitFile(Map<String, String> map,ArrayList<String> photos, final ResultListener resultListener) {
//        HttpClient.getInstance()
//                .getApiService()
//                .uploadFiles(builder(map,photos).build())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<UploadImgBean>(null) {
//                    @Override
//                    public void onSuccess(BaseBean baseBean) {
//                        resultListener.resultStr(true, ((UploadImgBean) baseBean.getData()).getPicid());
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        resultListener.resultStr(false, msg);
//                    }
//                });
    }

    /**
     * 上传图片
     *
     * @param photos
     * @param resultListener
     */
    public static void uploadFile(ArrayList<String> photos, final ResultListener resultListener) {
//        HttpClient.getInstance()
//                .getApiService()
//                .uploadFiles(builder(photos).build())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<UploadImgBean>(null) {
//                    @Override
//                    public void onSuccess(BaseBean baseBean) {
//                        resultListener.resultStr(true, ((UploadImgBean) baseBean.getData()).getPicid());
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        resultListener.resultStr(false, msg);
//                    }
//                });
    }

    public interface ResultListener {
        void resultStr(boolean isSuccess, String result);
    }

    /**
     * 请求体builder  上传文件
     *
     * @param imageFile
     * @return
     */
    public static MultipartBody.Builder builder(ArrayList<String> imageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (imageFile != null && imageFile.size() > 0) {
            for (int i = 0; i < imageFile.size(); i++) {
                builder.addFormDataPart("pic[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

    /**
     * 修改用户头像
     *
     * @param resultListener
     */
    public static void uploadHead(String filePash, final ResultListener resultListener) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        String [] imageStr=new String[]{"edit"};
//        File [] imageFile=new File[]{new File(filePash)};
//        hashMap.put("type", "1");
//        HttpClient.getInstance()
//                .getApiService()
//                .modifyUserHead(builder(hashMap,imageStr,imageFile).build())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver(null) {
//                    @Override
//                    public void onSuccess(BaseBean baseBean) {
//                        resultListener.resultStr(true, baseBean.getMsg());
//                    }
//
//                    @Override
//                    public void onFail(int code, String msg) {
//                        resultListener.resultStr(false, msg);
//                    }
//                });
    }


    /**
     * 请求体builder  上传文件
     *
     * @param imageFile
     * @return
     */
    public static MultipartBody.Builder builder(Map<String, String> map,ArrayList<String> imageFile) {
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
                builder.addFormDataPart("pic[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

    /**
     * 请求体builder  上传文件
     *
     * @param map
     * @param imageNames
     * @param imageFile
     * @return
     */
    public static MultipartBody.Builder builder(Map<String, String> map, String[] imageNames, File[] imageFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map != null) {
            Set<String> keys = map.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.addFormDataPart(key, map.get(key));
            }
        }
        if (imageNames != null && imageFile != null && imageNames.length == imageFile.length) {
            int length = imageNames.length;
            for (int i = 0; i < length; i++) {
                builder.addFormDataPart(imageNames[i], imageFile[i].getName(), RequestBody.create(MediaType.parse("image/*"), imageFile[i]));
            }
        } else {
            throw new IllegalArgumentException("The param imageNames is null");
        }
        return builder;
    }

}
