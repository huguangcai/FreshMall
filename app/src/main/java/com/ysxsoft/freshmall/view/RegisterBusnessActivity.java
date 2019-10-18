package com.ysxsoft.freshmall.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.JsonBean;
import com.ysxsoft.freshmall.modle.MallTypeBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.FileUtils;
import com.ysxsoft.freshmall.utils.GetJsonDataUtil;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import org.json.JSONArray;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterBusnessActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_mall_name, ed_real_name, ed_id_card_num, ed_phone_num, ed_detail_area;
    private LinearLayout ll_mall_type;
    private TextView tv_mall_type, tv_city, tv_before, tv_after, tv_zhizhao;
    private ImageView img_addres, img_id_card, img_id_card_after, img_zhizhao;
    private RelativeLayout rl_before, rl_after, rl_zhizhao;
    private Button btn_submit;
    private int click = 0;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String path1;
    private String path2;
    private String path3;
    private ArrayList<String> files = new ArrayList<>();
    private List<String> data;
    private String uid;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        customDialog = new CustomDialog(mContext,"正在提交数据....");
        requestData();
        setBackVisibily();
        setTitle("批发商资质");
        initView();
        initData();
        initListener();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .MallTypeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallTypeBean>() {
                    private MallTypeBean mallTypeBean;

                    @Override
                    public void onCompleted() {
                        if (mallTypeBean.getCode() == 0) {
                            data = mallTypeBean.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MallTypeBean mallTypeBean) {

                        this.mallTypeBean = mallTypeBean;
                    }
                });
    }

    private void initData() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        initJsonData();
                    }
                }
        ).start();
    }

    private void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    /**
     * 解析数据
     */
    private ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void initView() {
        ed_mall_name = getViewById(R.id.ed_mall_name);
        ll_mall_type = getViewById(R.id.ll_mall_type);
        tv_mall_type = getViewById(R.id.tv_mall_type);
        ed_real_name = getViewById(R.id.ed_real_name);
        ed_id_card_num = getViewById(R.id.ed_id_card_num);
        ed_phone_num = getViewById(R.id.ed_phone_num);
        img_addres = getViewById(R.id.img_addres);
        tv_city = getViewById(R.id.tv_city);
        ed_detail_area = getViewById(R.id.ed_detail_area);
        rl_before = getViewById(R.id.rl_before);
        img_id_card = getViewById(R.id.img_id_card);
        tv_before = getViewById(R.id.tv_before);
        rl_after = getViewById(R.id.rl_after);
        img_id_card_after = getViewById(R.id.img_id_card_after);
        tv_after = getViewById(R.id.tv_after);
        rl_zhizhao = getViewById(R.id.rl_zhizhao);
        img_zhizhao = getViewById(R.id.img_zhizhao);
        tv_zhizhao = getViewById(R.id.tv_zhizhao);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        ll_mall_type.setOnClickListener(this);
        img_addres.setOnClickListener(this);
        rl_before.setOnClickListener(this);
        rl_after.setOnClickListener(this);
        rl_zhizhao.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.register_business_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mall_type:
                showMallType();
                break;
            case R.id.img_addres:
                AppUtil.colsePhoneKeyboard(this);
                showCity();
                break;
            case R.id.rl_before:
                click = 1;
                openGallery();
                break;
            case R.id.rl_after:
                click = 2;
                openGallery();
                break;
            case R.id.rl_zhizhao:
                click = 3;
                openGallery();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_mall_name.getText().toString().trim())) {
                    showToastMessage("店铺名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv_mall_type.getText().toString().trim())) {
                    showToastMessage("店铺类别不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_real_name.getText().toString().trim())) {
                    showToastMessage("真实姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_id_card_num.getText().toString().trim())) {
                    showToastMessage("身份证号不能为空");
                    return;
                }
                if (ed_id_card_num.getText().toString().trim().length() != 18) {
                    showToastMessage("身份证号输入有误");
                    return;
                }
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("电话号码不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("电话号码输入有误");
                    return;
                }
                if (TextUtils.isEmpty(tv_city.getText().toString().trim())) {
                    showToastMessage("省市区不能为空");
                    return;
                }

                if (TextUtils.isEmpty(ed_detail_area.getText().toString().trim())) {
                    showToastMessage("详细地址不能为空");
                    return;
                }
                files.add(path3);
                files.add(path1);
                files.add(path2);
                if (files.size() <= 0 || files == null) {
                    showToastMessage("证件不足");
                    return;
                }
                customDialog.show();
                SubmitData();
                break;
        }
    }

    private void showMallType() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = data.get(options1);
                tv_mall_type.setText(tx);
            }
        })
                .setTitleText("选择店铺类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(data);//一级选择器
        pvOptions.show();
    }

    private void SubmitData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("dpnam", ed_mall_name.getText().toString().trim());
        map.put("dptype", tv_mall_type.getText().toString());
        map.put("rename", ed_real_name.getText().toString().trim());
        map.put("sfzhm", ed_id_card_num.getText().toString().trim());
        map.put("lxphone", ed_phone_num.getText().toString().trim());
        map.put("ssq", options1Items.get(optionsprovice) + "," + options2Items.get(optionscity) + "," + options3Items.get(optionsarea));
        map.put("xxdz", ed_detail_area.getText().toString().trim());

        NetWork.getService(ImpService.class)
                .BusinessData(builder(map, files).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            customDialog.dismiss();
                            ClearCache();
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

    private int optionsprovice;
    private int optionscity;
    private int optionsarea;

    private void showCity() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                optionsarea = options3;
                optionscity = options2;
                optionsprovice = options1;
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3);
                tv_city.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
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
                .enableCrop(false)// 是否裁剪
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
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
                    switch (click) {
                        case 1:
                            path1 = localMedia.get(0).getPath();
                            ImageLoadUtil.GlideHeadImageLoad(mContext, localMedia.get(0).getPath(), img_id_card);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            img_id_card.setLayoutParams(params);
                            tv_before.setVisibility(View.GONE);
                            break;
                        case 2:
                            path2 = localMedia.get(0).getPath();
                            ImageLoadUtil.GlideHeadImageLoad(mContext, localMedia.get(0).getPath(), img_id_card_after);
                            RelativeLayout.LayoutParams paramsafter = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            img_id_card_after.setLayoutParams(paramsafter);
                            tv_after.setVisibility(View.GONE);
                            break;
                        case 3:
                            path3 = localMedia.get(0).getPath();
                            ImageLoadUtil.GlideHeadImageLoad(mContext, localMedia.get(0).getPath(), img_zhizhao);
                            RelativeLayout.LayoutParams paramszhizhao = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            img_zhizhao.setLayoutParams(paramszhizhao);
                            tv_zhizhao.setVisibility(View.GONE);
                            break;
                    }

            }
        }
    }

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

    /**
     * 图文 上传
     *
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
                switch (i) {
                    case 0:
                        builder.addFormDataPart("yyzz", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
                        break;
                    case 1:
                        builder.addFormDataPart("sfzzm", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
                        break;
                    case 2:
                        builder.addFormDataPart("sfzbm", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
                        break;
                }
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }

}
