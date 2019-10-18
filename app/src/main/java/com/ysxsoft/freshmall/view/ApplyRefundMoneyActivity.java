package com.ysxsoft.freshmall.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.ApplyRefundMoneyAdapter;
import com.ysxsoft.freshmall.adapter.GridImageAdapter;
import com.ysxsoft.freshmall.com.FullyGridLayoutManager;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

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

public class ApplyRefundMoneyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_tupian;
    private TextView tv_order_sum_money, tv_refund_money, tv_order_number, tv_order_time, tv_pay_type, tv_pay_time, tv_submit;
    private RadioGroup rg_refund;
    private RadioButton rb_refund, rb_refund_goods;
    private EditText ed_refund_reson, ed_refund_illustrate;
    private MyRecyclerView recyclerView;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofAll();
    private int maxSelectNum = 3;
    private GridImageAdapter adapter;
    private MyRecyclerView recyclerdata;
    private int exit_type = 1;

    private String orderId;
    private OrderDetailBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        requestData();
        setBackVisibily();
        setTitle("申请退款");
        initView();
        initData();
        initListener();
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .orderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderDetailBean>() {
                    private OrderDetailBean orderDetailBean;

                    @Override
                    public void onCompleted() {
                        if (orderDetailBean.getCode() == 0) {
                            data = orderDetailBean.getData();
                            ApplyRefundMoneyAdapter adapter = new ApplyRefundMoneyAdapter(mContext, data.getGoods());
                            recyclerdata.setAdapter(adapter);

                            tv_order_number.setText(data.getOrder_num());
                            tv_order_time.setText(data.getPay_time());
                            switch (data.getPay_type()) {
                                case "1":
                                    tv_pay_type.setText("支付宝支付");
                                    break;
                                case "2":
                                    tv_pay_type.setText("微信支付");
                                    break;
                                case "3":
                                    tv_pay_type.setText("余额支付");
                                    break;
                            }
                            tv_order_sum_money.setText(data.getOrder_money());
                            tv_refund_money.setText(data.getOrder_money());
                            tv_pay_time.setText(data.getPay_time());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(OrderDetailBean orderDetailBean) {

                        this.orderDetailBean = orderDetailBean;
                    }
                });
    }

    private void initView() {
        recyclerdata = getViewById(R.id.recyclerdata);
        tv_order_sum_money = getViewById(R.id.tv_order_sum_money);
        rg_refund = getViewById(R.id.rg_refund);
        rb_refund = getViewById(R.id.rb_refund);
        rb_refund_goods = getViewById(R.id.rb_refund_goods);
        ed_refund_reson = getViewById(R.id.ed_refund_reson);
        tv_refund_money = getViewById(R.id.tv_refund_money);
        ed_refund_illustrate = getViewById(R.id.ed_refund_illustrate);
        recyclerView = getViewById(R.id.recyclerView);
        tv_order_number = getViewById(R.id.tv_order_number);
        tv_order_time = getViewById(R.id.tv_order_time);
        tv_pay_type = getViewById(R.id.tv_pay_type);
        tv_pay_time = getViewById(R.id.tv_pay_time);
        tv_submit = getViewById(R.id.tv_submit);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerdata.setLayoutManager(manager);
    }

    private void initData() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        rg_refund.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_refund:
                        exit_type = 1;
                        break;
                    case R.id.rb_refund_goods:
                        exit_type = 2;
                        break;
                }
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            PictureSelector.create(ApplyRefundMoneyActivity.this)
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

    private void initListener() {
        tv_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.apply_refund_money_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(ed_refund_reson.getText().toString().trim())) {
                    showToastMessage("退款原因不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_refund_illustrate.getText().toString().trim())) {
                    showToastMessage("退款说明不能为空");
                    return;
                }
                if (selectList != null && selectList.size() <= 0) {
                    showToastMessage("上传凭证不能为空");
                    return;
                }

                SunbmitData();
//                showToastMessage("申请退款");
//
                break;
        }
    }

    private void SunbmitData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("oid", orderId);
        map.put("uid", SpUtils.getSp(mContext, "uid"));
        map.put("exit_type", String.valueOf(exit_type));
        map.put("exit_title", ed_refund_reson.getText().toString().trim());
        map.put("exit_content", ed_refund_illustrate.getText().toString().trim());
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            files.add(selectList.get(i).getPath());
        }

        NetWork.getService(ImpService.class)
                .RefundMoney(builder(map, files).build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            Intent intent=new Intent(mContext,RefundDetialActivity.class);
                            intent.putExtra("orderId",orderId);
                            intent.putExtra("type","1");//申请退款
                            intent.putExtra("exit_type",String.valueOf(exit_type));
                            startActivity(intent);
                            ClearCache();
                            finish();//结束页面
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
                builder.addFormDataPart("exit_pics[" + i + "]", i + ".jpg", RequestBody.create(MediaType.parse("image/*"), new File(imageFile.get(i))));
            }
        } else {
            throw new IllegalArgumentException("The param is null");
        }
        return builder;
    }
}
