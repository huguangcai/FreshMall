package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.com.RBaseAdapter;
import com.ysxsoft.freshmall.com.RViewHolder;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommentResponse;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.WuLiuResponse;
import com.ysxsoft.freshmall.modle.WuliuPhoneResponse;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class LogisticsDetailActivity extends BaseActivity {

    private TextView tv_logistics_company, tv_logistics_phone, tv_logistics_number;
    private RecyclerView recyclerView;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra("orderId");
        setBackVisibily();
        setTitle("物流详情");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        OkHttpUtils.post()
                .url(ImpService.WU_LIU)
                .addParams("oid", orderId)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        WuLiuResponse resp = JsonUtils.parseByGson(response, WuLiuResponse.class);
                        if (resp != null) {
                            if ("ok".equals(resp.getMessage())) {
                                switch (resp.getCom()) {
                                    case "zhaijisong":
                                        tv_logistics_company.setText("宅急送");
                                        break;
                                    case "yuantong":
                                        tv_logistics_company.setText("圆通快递");
                                        break;
                                    case "zhongtong":
                                        tv_logistics_company.setText("中通快递");
                                        break;
                                    case "shentong":
                                        tv_logistics_company.setText("申通快递");
                                        break;
                                    case "yundakuaiyun":
                                        tv_logistics_company.setText("韵达快运");
                                        break;
                                    case "huitongkuaidi":
                                        tv_logistics_company.setText("百世快递");
                                        break;
                                    case "yunda":
                                        tv_logistics_company.setText("韵达快递");
                                        break;
                                    case "youzhengguonei":
                                        tv_logistics_company.setText("邮政快递包裹");
                                        break;
                                    case "shunfeng":
                                        tv_logistics_company.setText("顺丰速运");
                                        break;
                                    case "ems":
                                        tv_logistics_company.setText("EMS");
                                        break;
                                    case "jd":
                                        tv_logistics_company.setText("京东物流");
                                        break;
                                    case "debangkuaidi":
                                        tv_logistics_company.setText("德邦快递");
                                        break;
                                    case "tiantian":
                                        tv_logistics_company.setText("天天快递");
                                        break;
                                    case "youshuwuliu":
                                        tv_logistics_company.setText("优速快递");
                                        break;
                                }
                                requestPhone(resp.getCom());
                                tv_logistics_number.setText(resp.getNu());
                                final List<WuLiuResponse.DataBean> data = resp.getData();

                                RBaseAdapter<WuLiuResponse.DataBean> adapter = new RBaseAdapter<WuLiuResponse.DataBean>(mContext, R.layout.item_logistics_detail_layout, data) {
                                    @Override
                                    protected void fillItem(RViewHolder holder, WuLiuResponse.DataBean item, int position) {
                                        ImageView iv = holder.getView(R.id.iv);
                                        TextView tvContent = holder.getView(R.id.tvContent);
                                        tvContent.setText(item.getContext());
                                        holder.setText(R.id.tvTime, item.getTime());

                                        if (position==0){
                                            iv.setBackgroundResource(R.drawable.img_rg_ok);
                                            tvContent.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                                        }else {
                                            iv.setBackgroundResource(R.drawable.shape_gray_point);
                                            tvContent.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                                        }
                                    }

                                    @Override
                                    protected int getViewType(WuLiuResponse.DataBean item, int position) {
                                        return 0;
                                    }
                                };
                                recyclerView.setAdapter(adapter);


                            }
                        }
                    }
                });

    }

    private void requestPhone(String com) {
        PostFormBuilder url = OkHttpUtils.post()
                .url(ImpService.WU_LIU_PHONE);
        switch (com) {
            case "zhaijisong":
                url.addParams("name","宅急送") ;
                break;
            case "yuantong":
                url.addParams("name","圆通快递") ;
                break;
            case "zhongtong":
                url.addParams("name","中通快递") ;
                break;
            case "shentong":
                url.addParams("name","申通快递") ;
                break;
            case "yundakuaiyun":
                url.addParams("name","韵达快运") ;
                break;
            case "huitongkuaidi":
                url.addParams("name","百世快递") ;
                break;
            case "yunda":
                url.addParams("name","韵达快递") ;
                break;
            case "youzhengguonei":
                url.addParams("name","邮政快递包裹") ;
                break;
            case "shunfeng":
                url.addParams("name","顺丰速运") ;
                break;
            case "ems":
                url.addParams("name","EMS") ;
                break;
            case "jd":
                url.addParams("name","京东物流") ;
                break;
            case "debangkuaidi":
                url.addParams("name","德邦快递") ;
                break;
            case "tiantian":
                url.addParams("name","天天快递") ;
                break;
            case "youshuwuliu":
                url.addParams("name","优速快递") ;
                break;
        }
        url.tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        WuliuPhoneResponse resp = JsonUtils.parseByGson(response, WuliuPhoneResponse.class);
                        if (resp!=null){
                            if (resp.getCode()==200){
                                tv_logistics_phone.setText(resp.getData());
                            }
                        }
                    }
                });

    }

    private void initView() {
        tv_logistics_company = getViewById(R.id.tv_logistics_company);
        tv_logistics_phone = getViewById(R.id.tv_logistics_phone);
        tv_logistics_number = getViewById(R.id.tv_logistics_number);
        recyclerView = getViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public int getLayout() {
        return R.layout.logistics_detail_layout;
    }
}
