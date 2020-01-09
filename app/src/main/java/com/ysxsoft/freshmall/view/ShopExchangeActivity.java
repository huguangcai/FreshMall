package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommentResponse;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.JsonUtils;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Create By 胡
 * on 2020/1/7 0007
 */
public class ShopExchangeActivity extends BaseActivity {

    private CustomDialog customDialog;
    private EditText etNum;
    private EditText etPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialog = new CustomDialog(mContext, "请求中...");
        setBackVisibily();
        setTitle("购物券兑换");
        TextView tv_title_right = getViewById(R.id.tv_title_right);
        TextView tv_ok = getViewById(R.id.tv_ok);
        etNum = getViewById(R.id.etNum);
        etPwd = getViewById(R.id.etPwd);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("已兑换");
        tv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ExchangedActivity.class);
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNum.getText().toString().trim())) {
                    showToastMessage("卡号不能为空");
                    return;
                }

                if (TextUtils.isEmpty(etPwd.getText().toString().trim())) {
                    showToastMessage("密码不能为空");
                    return;
                }
                submitData();
            }
        });
    }

    private void submitData() {
//        customDialog.show();
        OkHttpUtils.post()
                .url(ImpService.USE_COUPON)
                .addParams("uid", SpUtils.getSp(mContext,"uid"))
                .addParams("key", etNum.getText().toString().trim())
                .addParams("value", etPwd.getText().toString().trim())
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        customDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        customDialog.dismiss();
                        CommentResponse resp = JsonUtils.parseByGson(response, CommentResponse.class);
                        if (resp != null) {
                            if (resp.getCode()==200) {
                                Intent intent = new Intent(mContext, ShoppingPacketExchangeActivity.class);
                                intent.putExtra("num",resp.getData());
                                startActivity(intent);
                                finish();
                            }else {
                                showToastMessage(resp.getMsg());
                            }
                        }
                    }
                });


    }

    @Override
    public int getLayout() {
        return R.layout.activity_shop_exchange_layout;
    }
}
