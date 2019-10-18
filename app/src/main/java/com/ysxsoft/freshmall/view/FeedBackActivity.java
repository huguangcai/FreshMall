package com.ysxsoft.freshmall.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_subimt;
    private EditText ed_content;
    private TextView tv_sum_num;
    private TextView tv_num;
    private LinearLayout ll_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackVisibily();
        setTitle("意见反馈");
        initView();
        initListener();
    }

    private void initView() {
        ll_sum = getViewById(R.id.ll_sum);
        ed_content = getViewById(R.id.ed_content);
        tv_num = getViewById(R.id.tv_num);
        tv_sum_num = getViewById(R.id.tv_sum_num);
        btn_subimt = getViewById(R.id.btn_subimt);
        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tv_num.setText(String.valueOf(s.toString().length()));
                } else {
                    tv_num.setText("0");
                }
                if (s.toString().length() >= 200) {
                    ll_sum.setVisibility(View.GONE);
                } else {
                    ll_sum.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initListener() {
        btn_subimt.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.feedback_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_subimt:
                if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                    showToastMessage("反馈内容不能为空");
                    return;
                }

                SubmitData();
                break;
        }
    }

    private void SubmitData() {
        NetWork.getService(ImpService.class)
                .Feedback(SpUtils.getSp(mContext, "uid"), ed_content.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode()==0){
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });


    }
}
