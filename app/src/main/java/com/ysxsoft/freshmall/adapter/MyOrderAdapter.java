package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.O2OOrderBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.widget.CircleImageView;

public class MyOrderAdapter extends ListBaseAdapter<O2OOrderBean.DataBean> {
    private OnPayItemClickListener onPayItemClickListener;

    public MyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.o2o_order_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        O2OOrderBean.DataBean dataBean = mDataList.get(position);
        CircleImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_order_type = holder.getView(R.id.tv_order_type);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_order_time = holder.getView(R.id.tv_order_time);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_cancle_order = holder.getView(R.id.tv_cancle_order);
        TextView tv_pay = holder.getView(R.id.tv_pay);

        ImageLoadUtil.GlideGoodsImageLoad(mContext, dataBean.getImgfm(), img_tupian);
        tv_mall_name.setText(dataBean.getDname());
        tv_num.setText(dataBean.getShuliang());
        tv_money.setText(dataBean.getJiner());
        tv_time.setText(dataBean.getXxtime());

        switch (dataBean.getDdsates()) {
            case "0":
                tv_order_type.setText("待付款");
                tv_order_time.setVisibility(View.INVISIBLE);
                tv_time.setVisibility(View.INVISIBLE);
                tv_cancle_order.setVisibility(View.VISIBLE);
                tv_cancle_order.setText("取消订单");
                tv_pay.setText("立即支付");
                break;
            case "1":
                tv_order_type.setText("待使用");
                tv_order_time.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_cancle_order.setVisibility(View.VISIBLE);
                tv_cancle_order.setText("申请退款");
                tv_pay.setText("扫码验证");
                break;
            case "2":
                tv_order_type.setText("待评价");
                tv_order_time.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("立即评价");
                break;
            case "3":
                tv_order_type.setText("退款中");
                tv_order_time.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                break;
            case "4":
                tv_order_type.setText("退款完成");
                tv_order_time.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                break;
            case "5":
                tv_order_type.setText("已完成");
                tv_order_time.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                break;
        }

        tv_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPayItemClickListener!=null){
                    onPayItemClickListener.OnCancleClick(position);
                }
            }
        });
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPayItemClickListener!=null){
                    onPayItemClickListener.OnPayClick(position);
                }
            }
        });
    }

    public interface OnPayItemClickListener{
        void OnCancleClick(int position);
        void OnPayClick(int position);
    }
    public void setOnPayItemClickListener(OnPayItemClickListener onPayItemClickListener){
        this.onPayItemClickListener = onPayItemClickListener;
    }
}
