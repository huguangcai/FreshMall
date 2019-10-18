package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LogisticsInputActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_select_logistics;
    private EditText ed_logistics_num;
    private Button btn_submit;
    private ImageView img_select_logistics_type;

    private ArrayList<String> options1Items = new ArrayList<>();
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        options1Items.add("跨越速运");
        options1Items.add("顺丰速运");
        options1Items.add("邮政EMS");
        options1Items.add("圆通快递");
        options1Items.add("申通快递");
        options1Items.add("韵达快递");
        options1Items.add("汇通快递");
        options1Items.add("中通快递");
        options1Items.add("宅急送");
        options1Items.add("天天快递");
        setBackVisibily();
        setTitle("录入物流单");
        initView();
        initListener();
    }

    private void initView() {
        tv_select_logistics = getViewById(R.id.tv_select_logistics);
        img_select_logistics_type = getViewById(R.id.img_select_logistics_type);
        ed_logistics_num = getViewById(R.id.ed_logistics_num);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        img_select_logistics_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.logistics_order_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_select_logistics_type:
                showLogistics();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(tv_select_logistics.getText().toString().trim())) {
                    showToastMessage("物流方式不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_logistics_num.getText().toString().trim())) {
                    showToastMessage("物流单号不能为空");
                    return;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .AddWuLiu(orderId, SpUtils.getSp(mContext, "uid"), tv_select_logistics.getText().toString(), ed_logistics_num.getText().toString().trim())
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
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });

    }

    private void showLogistics() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_select_logistics.setText(tx);
            }
        })
                .setTitleText("")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

    }
}
