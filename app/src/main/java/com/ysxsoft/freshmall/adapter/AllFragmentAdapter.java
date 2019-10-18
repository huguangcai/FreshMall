package com.ysxsoft.freshmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.ListBaseAdapter;
import com.ysxsoft.freshmall.com.SuperViewHolder;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.view.EvaluateActivity;
import com.ysxsoft.freshmall.view.LogisticsDetailActivity;
import com.ysxsoft.freshmall.view.RefundDetialActivity;
import com.ysxsoft.freshmall.view.WaitEvaluateActivity;
import com.ysxsoft.freshmall.view.WaitFaHouActivity;
import com.ysxsoft.freshmall.view.WaitPayActivity;
import com.ysxsoft.freshmall.view.WaitShouHuoActivity;
import com.ysxsoft.freshmall.widget.MyRecyclerView;

import java.util.ArrayList;

public class AllFragmentAdapter extends ListBaseAdapter<OrderListBean.DataBean> {
    private OnClickListener onClickListener;
    private Context context;

    public AllFragmentAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.all_fragment_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final OrderListBean.DataBean dataBean = mDataList.get(position);
        MyRecyclerView recyclerView = holder.getView(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        AllFragmentItemAdapter adapter = new AllFragmentItemAdapter(mContext, dataBean.getGoods());
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(true);
        adapter.setOnItemClickListener(new AllFragmentItemAdapter.OnItemClickListener() {
            @Override
            public void ItemClick(int position) {
                switch (dataBean.getOrder_status()) {
                    case 1://1:待付款
                        Intent intent1 = new Intent(context, WaitPayActivity.class);
                        intent1.putExtra("orderId", dataBean.getId());
                        context.startActivity(intent1);
                        break;
                    case 2://待发货
                        Intent intent = new Intent(context, WaitFaHouActivity.class);
                        intent.putExtra("orderId", dataBean.getId());
                        context.startActivity(intent);
                        break;
                    case 3://待收货
                        Intent intent3 = new Intent(context, WaitShouHuoActivity.class);
                        intent3.putExtra("orderId", dataBean.getId());
                        context.startActivity(intent3);

                        break;
                    case 4://待评价
                        Intent intent4 = new Intent(context, WaitEvaluateActivity.class);
                        intent4.putExtra("orderId", dataBean.getId());
                        context.startActivity(intent4);
                        break;
                    case 5://已完成

                        break;
                    case 6://售后
                        Intent intent6 = new Intent(context, RefundDetialActivity.class);
                        intent6.putExtra("orderId", dataBean.getId());
                        intent6.putExtra("type", "2");
                        context.startActivity(intent6);
                        break;
                    case 7://待退款寄回
                        Intent intent7 = new Intent(context, RefundDetialActivity.class);
                        intent7.putExtra("orderId", dataBean.getId());
                        intent7.putExtra("type", "6");
                        context.startActivity(intent7);
                        break;
                    case 8://退款寄回待确认
                        Intent intent8 = new Intent(context, RefundDetialActivity.class);
                        intent8.putExtra("orderId", dataBean.getId());
                        intent8.putExtra("type", "5");
                        context. startActivity(intent8);
                        break;
                    case 9://退款已完成
                        Intent intent9 = new Intent(context, RefundDetialActivity.class);
                        intent9.putExtra("orderId", dataBean.getId());
                        intent9.putExtra("type", "3");
                        context. startActivity(intent9);
                        break;

                }
            }
        });
        adapter.setOnClickListener(new AllFragmentItemAdapter.OnClickListener() {
            @Override
            public void cancleClick(int position) {
                context.startActivity(new Intent(context, LogisticsDetailActivity.class));
            }

            @Override
            public void checkClick(int position) {
                String goods_id = dataBean.getGoods().get(position).getSid();
                String order_id = dataBean.getGoods().get(position).getOrder_id();
                Intent intent = new Intent(mContext, EvaluateActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("mall", "mall");
                mContext.startActivity(intent);
            }
        });
        final TextView tv_order_number = holder.getView(R.id.tv_order_number);
        final TextView tv_time_orderNum = holder.getView(R.id.tv_time_orderNum);
        final TextView tv_order_type = holder.getView(R.id.tv_order_type);
        final TextView tv_cancle_order = holder.getView(R.id.tv_cancle_order);
        final TextView tv_pay = holder.getView(R.id.tv_pay);
        final LinearLayout ll_Is_show = holder.getView(R.id.ll_Is_show);

        switch (dataBean.getOrder_status()) {
            case 1://1:待付款
                tv_order_number.setText("");
                tv_time_orderNum.setText(dataBean.getPay_time());
                tv_order_type.setText("待付款");
                tv_cancle_order.setText("取消订单");
                tv_pay.setText("立即支付");
                tv_cancle_order.setVisibility(View.VISIBLE);
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 2://待发货
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待发货");
                tv_cancle_order.setText("退款");
                tv_cancle_order.setVisibility(View.VISIBLE);
                tv_pay.setText("提醒发货");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 3://待收货
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待收货");
                tv_cancle_order.setText("查看物流");
                tv_cancle_order.setVisibility(View.VISIBLE);
                tv_pay.setText("确认收货");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 4://待评价
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待评价");
                tv_cancle_order.setText("查看物流");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("立即评价");
                ll_Is_show.setVisibility(View.GONE);
                break;
            case 5://已完成
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("已完成");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 6://售后
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("退款中(待确认)");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 7:
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("待退款寄回");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                ll_Is_show.setVisibility(View.GONE);
                break;
            case 8:
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("退款寄回待确认");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
            case 9:
                tv_order_number.setText("订单编号:");
                tv_time_orderNum.setText(dataBean.getOrder_num());
                tv_order_type.setText("退款已完成");
                tv_cancle_order.setVisibility(View.INVISIBLE);
                tv_pay.setText("查看详情");
                ll_Is_show.setVisibility(View.VISIBLE);
                break;
        }
        adapter.setOnShowListener(new AllFragmentItemAdapter.OnShowListener() {
            @Override
            public void isShow(View view) {
                switch (dataBean.getOrder_status()) {
                    case 1://1:待付款
                        view.setVisibility(View.GONE);
                        tv_cancle_order.setVisibility(View.VISIBLE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;
                    case 2://待发货
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;
                    case 3://待收货
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;
                    case 4://待评价
                        view.setVisibility(View.VISIBLE);
                        ll_Is_show.setVisibility(View.GONE);
                        break;
                    case 5://已完成
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;
                    case 6://售后
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;

                    case 7://待退款寄回
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;

                    case 8://退款寄回待确认
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;
                    case 9://退款已完成
                        view.setVisibility(View.GONE);
                        ll_Is_show.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
        tv_cancle_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.cancleClick(position);
                }
            }
        });
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.checkClick(position);
                }
            }
        });

    }

    public interface OnClickListener {
        void cancleClick(int position);

        void checkClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


}
