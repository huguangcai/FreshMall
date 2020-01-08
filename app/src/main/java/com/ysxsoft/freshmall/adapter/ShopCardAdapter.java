package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.ShopCarListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

public class ShopCardAdapter extends ListBaseAdapter<ShopCarListBean.DataBean> {

    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }


    public ShopCardAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.shop_card_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final ShopCarListBean.DataBean dataBean = mDataList.get(position);
        final CheckBox cb_box = holder.getView(R.id.cb_box);
        RoundedImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
        ImageView img_down_arrow = holder.getView(R.id.img_down_arrow);
        img_down_arrow.setVisibility(View.GONE);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_money = holder.getView(R.id.tv_money);
        final TextView tv_color = holder.getView(R.id.tv_color);
        TextView tv_minus = holder.getView(R.id.tv_minus);
        final TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_add = holder.getView(R.id.tv_add);
        if (dataBean.isChoosed()) {
            cb_box.setChecked(true);
        } else {
            cb_box.setChecked(false);
        }
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getPic(),img_goods_tupian);
        tv_desc.setText(dataBean.getName());
        tv_money.setText(dataBean.getPrice());
        tv_color.setText(dataBean.getMore());
        tv_num.setText(dataBean.getNum());
        cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean.setChoosed(((CheckBox) v).isChecked());
                if (checkInterface != null) {
                    checkInterface.checkGroup(position, ((CheckBox) v).isChecked());
                }
            }
        });
        tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface != null) {
                    modifyCountInterface.doDecrease(position, tv_num, cb_box.isChecked());
                }
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface != null) {
                    modifyCountInterface.doIncrease(position, tv_num, cb_box.isChecked());
                }
            }
        });
        tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface!=null){
                    modifyCountInterface.doEdittext(position,tv_num,cb_box.isChecked());
                }
            }
        });
        img_down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifyCountInterface!=null){
                    modifyCountInterface.doGuige(position,tv_color,tv_num,cb_box.isChecked());
                }
            }
        });
    }


    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
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
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 输入数字
         * @param position
         * @param text
         * @param isChecked
         */
        void doEdittext(int position, View text, boolean isChecked);

        /**
         * 规格改变
         * @param position
         * @param text
         * @param isChecked
         */
        void doGuige(int position, View text, View textnum,boolean isChecked);
    }
}
