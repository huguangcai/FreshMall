package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.text.DecimalFormat;
import java.util.List;

public class AllFragmentItemAdapter extends RecyclerView.Adapter<AllFragmentItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderListBean.DataBean.GoodsBean> goods;
    private OnShowListener onShowListener;
    private OnClickListener onClickListener;
    private OnItemClickListener onItemClickListener;

    public AllFragmentItemAdapter(Context mContext, List<OrderListBean.DataBean.GoodsBean> goods) {
        this.mContext = mContext;
        this.goods = goods;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_fragment_item_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext, goods.get(i).getPic(), myViewHolder.img_tupian);
        myViewHolder.tv_color.setText(goods.get(i).getGuige());
        myViewHolder.tv_content.setText(goods.get(i).getName());
        myViewHolder.tv_goods_num.setText(goods.get(i).getNum());
//        myViewHolder.tv_price.setText(goods.get(i).getMoney());
        //TODO:Edit 2019/4/25
        String num=goods.get(i).getNum();
        String money=goods.get(i).getMoney();
        if(num!=null&&!"".equals(num)){
            int n= Integer.parseInt(num);//数量
            double m=Double.valueOf(money);//单价
            double total=n*m;
            DecimalFormat format=new DecimalFormat("0.00");
            myViewHolder.tv_price.setText(format.format(total));//商品显示数量*单价
        }

        if ("0".equals(goods.get(i).getPid())) {

        }else {
            myViewHolder.tv_pay.setBackgroundResource(R.drawable.refund_bg_shape);
            myViewHolder.tv_pay.setText("已评价");
            myViewHolder.tv_pay.setTextColor(mContext.getResources().getColor(R.color.black));
            myViewHolder.tv_pay.setEnabled(false);
        }
        myViewHolder.tv_cancle_order.setVisibility(View.INVISIBLE);
        if (onShowListener != null) {
            onShowListener.isShow(myViewHolder.ll_is_show);
        }

        myViewHolder.tv_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.cancleClick(i);
                }
            }
        });
        myViewHolder.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener != null) {
                    onClickListener.checkClick(i);
                }
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.ItemClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_tupian;
        private final TextView tv_content, tv_color, tv_price, tv_goods_num, tv_cancle_order, tv_pay;
        private final LinearLayout ll_is_show;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_goods_num = itemView.findViewById(R.id.tv_goods_num);
            ll_is_show = itemView.findViewById(R.id.ll_Is_show);
            tv_cancle_order = itemView.findViewById(R.id.tv_cancle_order);
            tv_pay = itemView.findViewById(R.id.tv_pay);
        }
    }

    public interface OnShowListener {
        void isShow(View view);
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public interface OnClickListener {
        void cancleClick(int position);

        void checkClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnItemClickListener {
        void ItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
