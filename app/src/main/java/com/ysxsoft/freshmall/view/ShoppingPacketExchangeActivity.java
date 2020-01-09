package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.RBaseAdapter;
import com.ysxsoft.freshmall.com.RViewHolder;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.GoodsListBean;
import com.ysxsoft.freshmall.modle.GoodsListResponse;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class ShoppingPacketExchangeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView tvOk;
    private int num;
    private List<GoodsListResponse.DataBeanX.DataBean> data;
    private GoodsListBean goodsListBean;
    private ArrayList<GoodsListBean.DataBean> goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getIntent().getIntExtra("num", -1);
        goodsListBean = new GoodsListBean();
        goods = new ArrayList<>();
        setBackVisibily();
        setTitle("购物券兑换");
        initView();
        requestData();
    }

    private void requestData() {
        OkHttpUtils.post()
                .url(ImpService.GOODS_LIST)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        GoodsListResponse resp = JsonUtils.parseByGson(response, GoodsListResponse.class);
                        if (resp != null) {
                            if (resp.getCode() == 200) {
                                data = resp.getData().getData();
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                                RBaseAdapter<GoodsListResponse.DataBeanX.DataBean> adapter = new RBaseAdapter<GoodsListResponse.DataBeanX.DataBean>(mContext, R.layout.item_shopping_packet_exchange, data) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, final GoodsListResponse.DataBeanX.DataBean item, int position) {
                                        final CheckBox cb_box = holder.getView(R.id.cb_box);
                                        ImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
                                        ImageLoadUtil.GlideGoodsImageLoad(mContext, item.getSppic(), img_goods_tupian);
                                        holder.setText(R.id.tvDesc, item.getSptitle());
                                        holder.setText(R.id.tvColorSize, item.getSpgg());
                                        holder.setText(R.id.tvNum, "1个");
                                        cb_box.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (cb_box.isChecked()) {
                                                    item.setClick(true);
                                                } else {
                                                    item.setClick(false);
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    protected int getViewType(GoodsListResponse.DataBeanX.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                });
    }

    private void initView() {

        recyclerView = getViewById(R.id.recyclerView);
        tvOk = getViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    GoodsListResponse.DataBeanX.DataBean dataBean = data.get(i);
                    if (dataBean.isClick()) {
                        GoodsListBean.DataBean bean = new GoodsListBean.DataBean();
                        bean.setPic(dataBean.getSppic());
                        bean.setDesc(dataBean.getSptitle());
                        bean.setGuige(dataBean.getSpgg());
                        bean.setId(String.valueOf(dataBean.getId()));
                        goods.add(bean);
                    }
                }

                if (goods.size()<=0){
                    showToastMessage("商品不能为空");
                    return;
                }

                if (goods.size()>num){
                    showToastMessage("最多可选"+num+"件商品");
                    return;
                }
                goodsListBean.setData(goods);
                Intent intent = new Intent(mContext, ExchangeCheckOrderActivity.class);
                intent.putExtra("datas", goodsListBean);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public int getLayout() {
        return R.layout.activity_shopping_packet_exchange;
    }
}
