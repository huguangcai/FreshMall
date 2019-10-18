package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptC;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.umeng.socialize.utils.UmengText;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.DeleteAddressBean;
import com.ysxsoft.freshmall.modle.JsonBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.GetJsonDataUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import org.json.JSONArray;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_right, tv_city;
    private EditText ed_get_goods_person, ed_phone, ed_area;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String uid;
    private String aid, city, address, linkname, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = SpUtils.getSp(mContext, "uid");
        setBackVisibily();
        setTitle("地址管理");
        Intent intent = getIntent();
        aid = intent.getStringExtra("aid");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        linkname = intent.getStringExtra("linkname");
        phone = intent.getStringExtra("phone");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("保存");
        ed_get_goods_person = getViewById(R.id.ed_get_goods_person);
        ed_phone = getViewById(R.id.ed_phone);
        tv_city = getViewById(R.id.tv_city);
        ed_area = getViewById(R.id.ed_area);
        if (!TextUtils.isEmpty(aid) && aid != null) {
            ed_get_goods_person.setText(linkname);
            ed_phone.setText(phone);
            tv_city.setText(city);
            ed_area.setText(address);
        }
    }

    private void initListener() {
        tv_title_right.setOnClickListener(this);
        tv_city.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right://保存
                if (checkData()) return;
                if (!TextUtils.isEmpty(aid) && aid != null) {
                    updataAddress();
                }else {
                    submitData();
                }
                break;
            case R.id.tv_city:
                AppUtil.colsePhoneKeyboard(this);
                showCity();
                break;
        }
    }

    private void updataAddress() {
        NetWork.getService(ImpService.class)
                .updateAddressData(uid, aid,
                        ed_get_goods_person.getText().toString().trim(),
                        ed_phone.getText().toString().trim(),
                        tv_city.getText().toString().trim(),
                        ed_area.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteAddressBean>() {
                    private DeleteAddressBean deleteAddressBean;

                    @Override
                    public void onCompleted() {
                        if (deleteAddressBean.getCode() == 0) {
                            showToastMessage(deleteAddressBean.getMsg());
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteAddressBean deleteAddressBean) {

                        this.deleteAddressBean = deleteAddressBean;
                    }
                });

    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .addAddressData(uid,
                        ed_get_goods_person.getText().toString().trim(),
                        ed_phone.getText().toString().trim(),
                        tv_city.getText().toString().trim(),
                        ed_area.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteAddressBean>() {
                    private DeleteAddressBean deleteAddressBean;

                    @Override
                    public void onCompleted() {
                        if (deleteAddressBean.getCode() == 0) {
                            showToastMessage(deleteAddressBean.getMsg());
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteAddressBean deleteAddressBean) {

                        this.deleteAddressBean = deleteAddressBean;
                    }
                });

    }

    private void showCity() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
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
     * 核对信息
     *
     * @return
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_get_goods_person.getText().toString().trim())) {
            showToastMessage("收货人姓名不能为空");
            return true;
        }
        if (TextUtils.isEmpty(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号不正确");
            return true;
        }
        if (TextUtils.isEmpty(tv_city.getText().toString().trim())) {
            showToastMessage("省市区不能为空");
            return true;
        }
        if (TextUtils.isEmpty(ed_area.getText().toString().trim())) {
            showToastMessage("详细地址不能为空");
            return true;
        }
        return false;
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

    @Override
    public int getLayout() {
        return R.layout.address_manager_layout;
    }

    /**
     * 解析数据
     *
     * @param result
     * @return
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


}
