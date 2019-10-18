package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.view.EvaluateActivity;
import com.ysxsoft.freshmall.view.LogisticsDetailActivity;
import com.ysxsoft.freshmall.view.WaitEvaluateActivity;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.ArrayList;

public class WaitEvaluateAdapter extends ListBaseAdapter<OrderListBean.DataBean> {
    private Context context;

    public WaitEvaluateAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.wait_evaluate_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final OrderListBean.DataBean dataBean = mDataList.get(position);
        MyRecyclerView recyclerView = holder.getView(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        final WaitEvaluateFragmentItemAdapter adapter = new WaitEvaluateFragmentItemAdapter(mContext, dataBean.getGoods());
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(true);

        adapter.setOnItemClickListener(new WaitEvaluateFragmentItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent4=new Intent(context,WaitEvaluateActivity.class);
                intent4.putExtra("orderId",dataBean.getId());
                context.startActivity(intent4);
            }
        });

        adapter.setOnClickListener(new WaitEvaluateFragmentItemAdapter.OnClickListener() {
            @Override
            public void cancleClick(int i) {
                Intent intent3=new Intent(context,LogisticsDetailActivity.class);
                intent3.putExtra("orderId",adapter.goods.get(i).getId());
                context.startActivity(intent3);
            }

            @Override
            public void checkClick(int i) {
                OrderListBean.DataBean.GoodsBean goodsBean = adapter.goods.get(i);
                String goods_id = goodsBean.getSid();
                String order_id = goodsBean.getOrder_id();
                Intent intent=new Intent(mContext, EvaluateActivity.class);
                intent.putExtra("goods_id",goods_id);
                intent.putExtra("order_id",order_id);
                intent.putExtra("mall","mall");
                mContext.startActivity(intent);
            }
        });
        TextView tv_order_number = holder.getView(R.id.tv_order_number);
        TextView tv_time_orderNum = holder.getView(R.id.tv_time_orderNum);
        TextView tv_order_type = holder.getView(R.id.tv_order_type);
        switch (dataBean.getOrder_status()) {
            case 1://1:待付款
                tv_order_number.setText("剩余付款时间：");
                tv_time_orderNum.setText(dataBean.getPay_time());
                tv_order_type.setText("待付款");
                break;
            case 2://待发货
                tv_order_number.setText("订单编号：");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待发货");
                break;
            case 3://待收货
                tv_order_number.setText("订单编号：");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待收货");
                break;
            case 4://待评价
                tv_order_number.setText("订单编号：");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待评价");
                break;
            case 5://售后
                tv_order_number.setText("订单编号：");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("退款中");
                break;
        }
    }
}
