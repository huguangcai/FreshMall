package com.ysxsoft.freshmall.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.SearcRecordeDataBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.widget.flowlayout.FlowLayout;
import com.ysxsoft.freshmall.widget.flowlayout.TagAdapter;
import com.ysxsoft.freshmall.widget.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchDataActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_clean;
    private TagFlowLayout mFlowLayout;
    private LinearLayout ll_data;
    private LayoutInflater mInflater;
    private TextView tv_title_right;
    private EditText ed_title_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
        setBackVisibily();
        initView();
        initListener();
    }

    private void requstData() {
        NetWork.getService(ImpService.class)
                .SearcRecordeData(SpUtils.getSp(mContext, "uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearcRecordeDataBean>() {
                    private SearcRecordeDataBean searcRecordeDataBean;

                    @Override
                    public void onCompleted() {
                        if (searcRecordeDataBean.getCode() == 0) {
                            final List<SearcRecordeDataBean.DataBean> data = searcRecordeDataBean.getData();
                            if (data.size()<=0){
                                ll_data.setVisibility(View.GONE);
                            }

                            mFlowLayout.setAdapter(new TagAdapter<SearcRecordeDataBean.DataBean>(data) {
                                @Override
                                public View getView(FlowLayout parent, int position, SearcRecordeDataBean.DataBean dataBean) {
                                    TextView tv = (TextView) mInflater.inflate(R.layout.search_flow_item_layout, mFlowLayout, false);
                                    tv.setText(dataBean.getKeyword());
                                    return tv;
                                }


                            });
                            mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                                @Override
                                public void onSelected(Set<Integer> selectPosSet) {

                                }
                            });
                            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    Intent intent = new Intent(mContext, SearchResultActivity.class);
                                    intent.putExtra("content", data.get(position).getKeyword());
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SearcRecordeDataBean searcRecordeDataBean) {

                        this.searcRecordeDataBean = searcRecordeDataBean;
                    }
                });
    }

    private void initView() {
        ed_title_search = getViewById(R.id.ed_title_search);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("搜索");
        ll_data = getViewById(R.id.ll_data);
        img_clean = getViewById(R.id.img_clean);
        mFlowLayout = getViewById(R.id.tf_layout);

    }

    private void initListener() {
        img_clean.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.search_data_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_clean:
                DeleteData();

                break;
            case R.id.tv_title_right:
                if (TextUtils.isEmpty(ed_title_search.getText().toString().trim())) {
                    showToastMessage("搜索内容不能为空");
                    return;
                }
                AppUtil.colsePhoneKeyboard(this);
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("content", ed_title_search.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }

    private void DeleteData() {
        NetWork.getService(ImpService.class)
                .DeleteRecordData(SpUtils.getSp(mContext,"uid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode()==0){
                            mFlowLayout.removeAllViews();
                            ll_data.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        requstData();
    }
}
