package com.ysxsoft.freshmall.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.dialog.PersonDataDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.FileUtils;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PersonDataActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView img_head;
    private LinearLayout ll_nike_name, ll_phone_num, ll_address;
    private TextView tv_nike_name, tv_phone_num;
    private CustomDialog customDialog;
    private RxPermissions rxPermissions;
    private String headurl, nikename, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        headurl = intent.getStringExtra("headurl");
        nikename = intent.getStringExtra("nikename");
        phone = intent.getStringExtra("phone");
        customDialog = new CustomDialog(mContext, "正在上传,请稍后...");
        rxPermissions = new RxPermissions(this);
        setBackVisibily();
        setTitle("个人资料");
        initView();
        initListener();
    }

    private void initView() {
        img_head = getViewById(R.id.img_head);
        ll_nike_name = getViewById(R.id.ll_nike_name);
        tv_nike_name = getViewById(R.id.tv_nike_name);
        ll_phone_num = getViewById(R.id.ll_phone_num);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        ll_address = getViewById(R.id.ll_address);
        if (!TextUtils.isEmpty(headurl) && headurl != null) {
            ImageLoadUtil.GlideHeadImageLoad(mContext, headurl, img_head);
            tv_nike_name.setText(nikename);
            tv_phone_num.setText(phone);
        }
    }

    private void initListener() {
        img_head.setOnClickListener(this);
        ll_nike_name.setOnClickListener(this);
        ll_phone_num.setOnClickListener(this);
        ll_address.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.person_data_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
                            openGallery();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToastMessage("未授权权限，部分功能不能使用");
                        }
                    }
                });
                break;
            case R.id.ll_nike_name:
                final PersonDataDialog dialog = new PersonDataDialog(mContext);
                final EditText ed_content = dialog.findViewById(R.id.ed_content);
                ed_content.setHint("请输入昵称");
                ed_content.setInputType(InputType.TYPE_CLASS_TEXT);
                TextView tv_ok = dialog.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                            showToastMessage("昵称不能为空");
                            return;
                        }
                        tv_nike_name.setText(ed_content.getText().toString().trim());
                        submitName(dialog, ed_content.getText().toString().trim());
                    }
                });
                dialog.show();

                break;
            case R.id.ll_phone_num:
                final PersonDataDialog phonedialog = new PersonDataDialog(mContext);
                final EditText phone_ed_content = phonedialog.findViewById(R.id.ed_content);
                phone_ed_content.setHint("请输入电话号");
                phone_ed_content.setInputType(InputType.TYPE_CLASS_NUMBER);
                TextView phone_tv_ok = phonedialog.findViewById(R.id.tv_ok);
                phone_tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(phone_ed_content.getText().toString().trim())) {
                            showToastMessage("电话号不能为空");
                            return;
                        }
                        if (!AppUtil.checkPhoneNum(phone_ed_content.getText().toString().trim())) {
                            showToastMessage("电话号输入有误");
                            return;
                        }
                        tv_phone_num.setText(phone_ed_content.getText().toString().trim());
                        phonedialog.dismiss();
                    }
                });
//                phonedialog.show();
                break;
            case R.id.ll_address:
                startActivity(GetGoodsAddressActivity.class);
                break;
        }
    }

    private void submitName(final PersonDataDialog dialog, String trim) {
        NetWork.getService(ImpService.class)
                .PersonNameData(SpUtils.getSp(mContext, "uid"), "2", trim)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            dialog.dismiss();
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

    /**
     * 打开相册
     */
    private void openGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .compressSavePath(FileUtils.getSDCardPath())//压缩图片保存地址
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    // 图片选择结果回调
                    final List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    File file = new File(localMedia.get(0).getPath());
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", SpUtils.getSp(mContext, "uid"));
                    map.put("type", "1");
                    ArrayList<String> files = new ArrayList<>();
                    for (int i = 0; i < localMedia.size(); i++) {
                        files.add(localMedia.get(i).getPath());
                    }
                    NetWork.getService(ImpService.class)
                            .PersonPicData(builder(map, files).build())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<CommonBean>() {
                                private CommonBean commonBean;

                                @Override
                                public void onCompleted() {
                                    showToastMessage(commonBean.getMsg());
                                    if (commonBean.getCode() == 0) {
                                        ImageLoadUtil.GlideHeadImageLoad(mContext, localMedia.get(0).getPath(), img_head);
                                        ClearCache();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    showToastMessage(e.getMessage());
                                }

                                @Override
                                public void onNext(CommonBean commonBean) {

                                    this.commonBean = commonBean;
                                }
                            });


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
                builder.addFormDataPart("pic", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

    private void ClearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            showToastMessage(getString(R.string.picture_jurisdiction));
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
}
