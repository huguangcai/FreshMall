package com.ysxsoft.freshmall.view;

import java.util.List;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.GridImageAdapter;
import com.ysxsoft.freshmall.com.FullyGridLayoutManager;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_evaluate;
    private RatingBar rb_star;
    private Button btn_submit;
    private RecyclerView recyclerView;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofAll();
    private int maxSelectNum = 3;
    private GridImageAdapter adapter;
    private String mall, goods_id, order_id;
    private float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mall = intent.getStringExtra("mall");
        goods_id = intent.getStringExtra("goods_id");
        order_id = intent.getStringExtra("order_id");
        setBackVisibily();
        setTitle("评价");
        initView();
        initListener();
    }

    private void initView() {

        ed_evaluate = getViewById(R.id.ed_evaluate);
        recyclerView = getViewById(R.id.recyclerView);
        rb_star = getViewById(R.id.rb_star);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        btn_submit.setOnClickListener(this);
        rb_star.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            PictureSelector.create(EvaluateActivity.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .selectionMedia(selectList)// 是否传入已选图片
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    @Override
    public int getLayout() {
        return R.layout.evaluate_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_evaluate.getText().toString().trim())) {
                    showToastMessage("评论内容不能为空");
                    return;
                }
                if (selectList.size() <= 0) {
                    showToastMessage("图片不能为空");
                    return;
                }
                if ("mall".equals(mall)) {//商城
                    MallSubmit();
                } else {//O2O
                    O2OSubmitData(order_id);
                }
                break;
        }
    }

    private void O2OSubmitData(String order_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", order_id);
        map.put("pjxj", String.valueOf(rate));
        map.put("text", ed_evaluate.getText().toString().trim());
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            files.add(selectList.get(i).getPath());
        }
        NetWork.getService(ImpService.class)
                .O2OEvaluate(builder(map, files).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;
                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });
    }

    private void MallSubmit() {
        HashMap<String, String> map = new HashMap<>();
        map.put("oid", order_id);
        map.put("uid", SpUtils.getSp(mContext, "uid"));
        map.put("sid", goods_id);
        map.put("xingji", String.valueOf(rate));
        map.put("content", ed_evaluate.getText().toString().trim());
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            files.add(selectList.get(i).getPath());
        }
        NetWork.getService(ImpService.class)
                .OrderEvaluate(builder(map, files).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        ClearCache();
                        if (commonBean.getCode() == 0) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        this.commonBean = commonBean;
                    }
                });
    }

    /**
     * 上传后条用
     */
    private void ClearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            Toast.makeText(mContext, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    public MultipartBody.Builder builder(Map<String, String> map, ArrayList<String> imageFile) {
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
                builder.addFormDataPart("pics[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

}
