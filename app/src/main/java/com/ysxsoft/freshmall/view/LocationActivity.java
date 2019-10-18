package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.CityListAdapter;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.widget.cityselect.SideLetterBar;
import com.ysxsoft.freshmall.widget.cityselect.bean.AreasBean;
import com.ysxsoft.freshmall.widget.cityselect.bean.City;
import com.ysxsoft.freshmall.widget.cityselect.bean.CityPickerBean;
import com.ysxsoft.freshmall.widget.cityselect.util.GsonUtil;
import com.ysxsoft.freshmall.widget.cityselect.util.PinyinUtils;
import com.ysxsoft.freshmall.widget.cityselect.util.ReadAssetsFileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class LocationActivity extends BaseActivity {

    private ListView recyclerView;
    private SideLetterBar mLetterBar;
    private CityListAdapter adapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("城市选择");
        initView();
        getCityData();
    }

    private void getCityData() {
        String json = ReadAssetsFileUtil.getJson(this, "city.json");
        CityPickerBean bean = GsonUtil.getBean(json, CityPickerBean.class);
        HashSet<City> citys = new HashSet<>();
        for (AreasBean areasBean : bean.data.areas) {
            String name = areasBean.name.replace("", "");
            citys.add(new City(areasBean.id, name, PinyinUtils.getPinYin(name), areasBean.is_hot == 1));
            for (AreasBean.ChildrenBeanX childrenBeanX : areasBean.children) {
                citys.add(new City(childrenBeanX.id, childrenBeanX.name, PinyinUtils.getPinYin(childrenBeanX.name), childrenBeanX.is_hot == 1));
            }
        }
        //set转换list
        ArrayList<City> cities = new ArrayList<>(citys);
        //按照字母排序
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City city, City t1) {
                return city.getPinyin().compareTo(t1.getPinyin());
            }
        });

        adapter = new CityListAdapter(mContext, cities);
        recyclerView.setAdapter(adapter);
        adapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {//选择城市
                Intent intent = new Intent();
                intent.putExtra("name",name);
                setResult(101,intent);
                finish();
            }
        });
    }

    private void initView() {
        recyclerView = getViewById(R.id.recyclerView);
        mLetterBar = getViewById(R.id.side_letter_bar);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = adapter.getLetterPosition(letter);
                recyclerView.setSelection(position);
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.location_layout;
    }

}
