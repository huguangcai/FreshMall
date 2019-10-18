package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.widget.cityselect.bean.City;
import com.ysxsoft.freshmall.widget.cityselect.util.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create By 胡
 * on 2019/8/9 0009
 */
public class CityListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<City> mCities;
    private HashMap<String, Integer> letterIndexes;
    private OnCityClickListener onCityClickListener;

    public CityListAdapter(Context mContext, ArrayList<City> cities) {
        this.mContext = mContext;
        mCities = cities;
        inflater = LayoutInflater.from(mContext);
        letterIndexes = new HashMap<>();
        for (int index = 0; index < mCities.size(); index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "A";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
            }
        }

    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? 0: integer;
    }

    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        if (view == null) {
            holder = new CityViewHolder();
            view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
            holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
            view.setTag(holder);
        } else {
            holder = (CityViewHolder) view.getTag();
        }

        final String city = mCities.get(position).getName();
        holder.name.setText(city);
        String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).getPinyin());
        String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position-1).getPinyin()) : "";
        if (!TextUtils.equals(currentLetter, previousLetter)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(currentLetter);
        } else {
            holder.letter.setVisibility(View.GONE);
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCityClickListener != null) {
                    onCityClickListener.onCityClick(city);
                }
            }
        });

        return view;
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name);
    }
}
