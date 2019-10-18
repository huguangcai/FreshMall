package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class O2OGoodsFragmentAdapter extends ListBaseAdapter<MallDetailBean.DataBean.SplistBean> {

    private ModifyCountInterface modifyCountInterface;
    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public O2OGoodsFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.o2o_goods_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        MallDetailBean.DataBean.SplistBean splistBean = mDataList.get(position);
        ImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_yuexiao = holder.getView(R.id.tv_yuexiao);
        TextView tv_price = holder.getView(R.id.tv_price);
        final LinearLayout ll_add_minus = holder.getView(R.id.ll_add_minus);
        final ImageView img_join_shop_car = holder.getView(R.id.img_join_shop_car);
        ImageView img_minus = holder.getView(R.id.img_minus);
        final TextView tv_num = holder.getView(R.id.tv_num);
        ImageView img_add = holder.getView(R.id.img_add);

        ImageLoadUtil.GlideGoodsImageLoad(mContext, splistBean.getFmtp(), img_tupian);
        tv_mall_name.setText(splistBean.getSpname());
        tv_desc.setText(splistBean.getMiaoshu());
        tv_yuexiao.setText("月销：" + splistBean.getXsliang());
        tv_price.setText("¥" + splistBean.getSpjiage());
        tv_num.setText(String.valueOf(splistBean.getGwc()));
        if (Integer.valueOf(splistBean.getGwc())>0){
            img_join_shop_car.setVisibility(View.GONE);
            ll_add_minus.setVisibility(View.VISIBLE);
        }else {
            img_join_shop_car.setVisibility(View.VISIBLE);
            ll_add_minus.setVisibility(View.GONE);
        }

        img_join_shop_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_join_shop_car.setVisibility(View.GONE);
                ll_add_minus.setVisibility(View.VISIBLE);
            }
        });

        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface != null) {
                    modifyCountInterface.doDecrease(position, tv_num);
                }
            }
        });
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface != null) {
                    modifyCountInterface.doIncrease(position, tv_num);
                }
            }
        });

    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doIncrease(int position, View showCountView);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doDecrease(int position, View showCountView);
    }
}
