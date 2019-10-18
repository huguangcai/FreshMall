package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class O2OWaitPayAdapter extends RecyclerView.Adapter<O2OWaitPayAdapter.O2OOrderHolder> {

    private Context mContext;
    private List<O2OOederDetailBean.DataBean.SplistBean> splist;

    public O2OWaitPayAdapter(Context mContext, List<O2OOederDetailBean.DataBean.SplistBean> splist) {
        this.mContext = mContext;

        this.splist = splist;
    }

    @NonNull
    @Override
    public O2OOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.o2o_order_check_item_layout, viewGroup,false);
        return new O2OOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull O2OOrderHolder o2OOrderHolder, int i) {
        O2OOederDetailBean.DataBean.SplistBean splistBean = splist.get(i);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,splistBean.getSpfmtp(),o2OOrderHolder.img_tupian);
        o2OOrderHolder.tv_goods_name.setText(splistBean.getSpname());
        o2OOrderHolder.tv_number.setText("x"+splistBean.getSpshuliang());
        o2OOrderHolder.tv_price.setText("¥ "+splistBean.getSpjiage());
    }

    @Override
    public int getItemCount() {
        return splist.size();
    }

    public class O2OOrderHolder extends RecyclerView.ViewHolder {

        private final ImageView img_tupian;
        private final TextView tv_goods_name;
        private final TextView tv_number;
        private final TextView tv_price;

        public O2OOrderHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
