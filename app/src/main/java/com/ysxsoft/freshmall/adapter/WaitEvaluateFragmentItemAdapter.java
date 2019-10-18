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
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;

import java.util.List;

public class WaitEvaluateFragmentItemAdapter extends RecyclerView.Adapter<WaitEvaluateFragmentItemAdapter.MyViewHolder> {

    private Context mContext;
    public List<OrderListBean.DataBean.GoodsBean> goods;
    private OnClickListener onClickListener;
    private OnItemClickListener onItemClickListener;

    public WaitEvaluateFragmentItemAdapter(Context mContext, List<OrderListBean.DataBean.GoodsBean> goods) {
        this.mContext = mContext;
        this.goods = goods;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wait_evaluate_fragment_item_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        ImageLoadUtil.GlideGoodsImageLoad(mContext,goods.get(i).getPic(),myViewHolder.img_tupian);
        myViewHolder.tv_color.setText(goods.get(i).getGuige());
        myViewHolder.tv_content.setText(goods.get(i).getName());
        myViewHolder.tv_goods_num.setText(goods.get(i).getNum());
        myViewHolder.tv_price.setText(goods.get(i).getMoney());

        if ("0".equals(goods.get(i).getPid())) {
        }else {
            myViewHolder.tv_pay.setBackgroundResource(R.drawable.refund_bg_shape);
            myViewHolder.tv_pay.setText("已评价");
            myViewHolder.tv_pay.setTextColor(mContext.getResources().getColor(R.color.black));
            myViewHolder.tv_pay.setEnabled(false);
        }
        myViewHolder.tv_cancle_order.setVisibility(View.INVISIBLE);

        myViewHolder.tv_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.cancleClick(i);
                }
            }
        });
        myViewHolder.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.checkClick(i);
                }
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_tupian;
        private final TextView tv_content,tv_color,tv_price,tv_goods_num,tv_pay,tv_cancle_order;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tupian = itemView.findViewById(R.id.img_tupian);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_color = itemView.findViewById(R.id.tv_color);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_goods_num = itemView.findViewById(R.id.tv_goods_num);
            tv_cancle_order = itemView.findViewById(R.id.tv_cancle_order);
            tv_pay = itemView.findViewById(R.id.tv_pay);
        }
    }

    public interface OnClickListener {
        void cancleClick(int position);
        void checkClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
