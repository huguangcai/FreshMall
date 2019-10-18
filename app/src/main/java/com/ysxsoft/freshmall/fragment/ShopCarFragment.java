package com.ysxsoft.freshmall.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.DetailItemAdapter;
import com.ysxsoft.freshmall.adapter.ShopCardAdapter;
import com.ysxsoft.freshmall.dialog.GoodsDetailDialog;
import com.ysxsoft.freshmall.dialog.PersonDataDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.impservice.OnItemClickListener;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.GoodDetailBean;
import com.ysxsoft.freshmall.modle.PackageDetailBean;
import com.ysxsoft.freshmall.modle.ShopCarListBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseFragment;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.view.OrderCheckActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopCarFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ShopCardAdapter.ModifyCountInterface, ShopCardAdapter.CheckInterface {

    private TextView tv_title_right;
    private CheckBox cb_box;
    private LinearLayout ll_goods_sum,ll_show;
    private TextView tv_goods_sum_money;
    private TextView tv_delete;
    private boolean isManager = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler();
    private ShopCardAdapter mDataAdapter;
    private String uid;
    private ShopCarListBean shopCarListBean;
    private double totalPrice = 0.00;// 购买的商品总价

    private StringBuffer sectionDelete = new StringBuffer();
    private String shopCardId;
    private ArrayList<PackageDetailBean.DetailData> detailBeans = new ArrayList<>();


    @Override
    protected void initData() {
        int stateBar = getStateBar();
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("购物车");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("管理");
        cb_box = getViewById(R.id.cb_box);
        ll_goods_sum = getViewById(R.id.ll_goods_sum);
        ll_show = getViewById(R.id.ll_show);
        tv_goods_sum_money = getViewById(R.id.tv_goods_sum_money);
        tv_delete = getViewById(R.id.tv_delete);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) getViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new ShopCardAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(getActivity(), mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    @Override
    protected void initListenser() {
        tv_title_right.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        cb_box.setOnClickListener(this);
        mDataAdapter.setCheckInterface(this);
        mDataAdapter.setModifyCountInterface(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.shop_car_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_box:
                if (mDataAdapter.getDataList().size() != 0) {
                    if (cb_box.isChecked()) {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(true);
                        }
                        notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(false);
                        }
                        notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.tv_title_right:
                if (isManager) {
                    isManager = false;
                    tv_title_right.setText("管理");
                    ll_goods_sum.setVisibility(View.VISIBLE);
                    tv_delete.setText("结算");
                } else {
                    isManager = true;
                    tv_title_right.setText("完成");
                    ll_goods_sum.setVisibility(View.INVISIBLE);
                    tv_delete.setText("删除");
                }
                break;
            case R.id.tv_delete:
                if (TextUtils.equals("0.0", tv_goods_sum_money.getText().toString())) {
                    Toast.makeText(getActivity(), "所选商品不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sectionDelete.length() != 0) {
                    sectionDelete.setLength(0);
                }
                PackageDetailBean detailBean = new PackageDetailBean();
                for (ShopCarListBean.DataBean dataBean : mDataAdapter.getDataList()) {
                    boolean choosed = dataBean.isChoosed();
                    if (choosed) {
                        sectionDelete.append(String.valueOf(dataBean.getId())).append(",");
                        PackageDetailBean.DetailData detailData = new PackageDetailBean.DetailData();
                        detailData.setShopCarId(String.valueOf(dataBean.getId()));
                        detailData.setGoodsId(String.valueOf(dataBean.getSid()));
                        detailData.setPrice(dataBean.getPrice());
                        detailData.setNumber(dataBean.getNum());
                        detailData.setGuige(dataBean.getMore());
                        detailData.setContent(dataBean.getName());
                        detailData.setUrl(dataBean.getPic());
                        detailBeans.add(detailData);
                    }
                }
                detailBean.setSum(tv_goods_sum_money.getText().toString());
                detailBean.setDataList(detailBeans);
                shopCardId = sectionDelete.deleteCharAt(sectionDelete.length() - 1).toString();
                if (isManager) {
                    sectionDelete();
                } else {
                    Intent intentId = new Intent(getActivity(), OrderCheckActivity.class);
                    intentId.putExtra("goods", detailBean);
                    getActivity().startActivity(intentId);
                }
                break;
        }
    }

    /**
     * 删除
     */
    private void sectionDelete() {
        NetWork.getService(ImpService.class)
                .delShopCar(shopCardId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            sectionDelete.setLength(0);
                            shopCardId=null;
                            detailBeans.clear();
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });
    }

    @Override
    public void onRefresh() {
        if (cb_box.isChecked()) {
            cb_box.setChecked(false);
        }
        tv_goods_sum_money.setText("0.0");
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(getActivity())) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        mDataAdapter.getDataList().get(position).setChoosed(isChecked);
        if (isAllCheck()) {
            cb_box.setChecked(true);
        } else {
            cb_box.setChecked(false);
        }
        notifyDataSetChanged();
        statistics();
    }

    private void statistics() {
        totalPrice = 0.00;
        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
            ShopCarListBean.DataBean dataBean = mDataAdapter.getDataList().get(i);
            if (dataBean.isChoosed()) {
                totalPrice += Double.valueOf(dataBean.getPrice()) * Double.valueOf(dataBean.getNum());
            }
        }
        DecimalFormat decimalFormat =new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String format = decimalFormat.format(totalPrice);
        tv_goods_sum_money.setText(format);
    }

    private boolean isAddBack = false;

    @Override
    public void doIncrease(int position, final View showCountView, boolean isChecked) {
        final ShopCarListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        int currentCount = Integer.valueOf(dataBean.getNum());
        currentCount++;
        dataBean.setNum(currentCount + "");
        ((TextView) showCountView).setText(currentCount + "");
        notifyDataSetChanged();
        statistics();

        if (isAddBack) {
//            showToastMessage("小鱼三秒");
        } else {
            isAddBack = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAddBack = false;
//                    showToastMessage("大于三秒");
                    editextData(String.valueOf(dataBean.getId()), uid, ((TextView) showCountView).getText().toString());
                }
            }, 3000);
        }

    }

    private boolean isMinusBack = false;

    @Override
    public void doDecrease(int position, final View showCountView, boolean isChecked) {
        final ShopCarListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        int currentCount = Integer.valueOf(dataBean.getNum());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        dataBean.setNum(currentCount + "");
        ((TextView) showCountView).setText(currentCount + "");
        notifyDataSetChanged();
        statistics();

        if (isMinusBack) {
//            showToastMessage("小鱼三秒");
        } else {
            isMinusBack = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isMinusBack = false;
//                    showToastMessage("大于三秒");
                    editextData(String.valueOf(dataBean.getId()), uid, ((TextView) showCountView).getText().toString());
                }
            }, 3000);
        }
    }

    private void editextData(String id, String uid, String num) {
        NetWork.getService(ImpService.class)
                .editShopCar(id, uid, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {

                        this.commonBean = commonBean;
                    }
                });
    }

    @Override
    public void doEdittext(int position, final View text, boolean isChecked) {
        final ShopCarListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        final PersonDataDialog textdialog = new PersonDataDialog(getActivity());
        final EditText ed_num = textdialog.findViewById(R.id.ed_content);
        ed_num.setHint("请输入数量");
        ed_num.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView text_tv_ok = textdialog.findViewById(R.id.tv_ok);
        text_tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(ed_num.getText().toString().trim()) || Integer.valueOf(ed_num.getText().toString().trim()) <= 0) {
                    Toast.makeText(getActivity(), "输入数量不能为零", Toast.LENGTH_SHORT).show();
                } else {
                    dataBean.setNum(ed_num.getText().toString().trim());
                    ((TextView) text).setText(ed_num.getText().toString().trim());
                    notifyDataSetChanged();
                    statistics();
                    editextData(String.valueOf(dataBean.getId()), uid, ((TextView) text).getText().toString());
                }
                textdialog.dismiss();
            }
        });
        textdialog.show();
    }

    @Override
    public void doGuige(int position, final View text, final View tv_num, boolean isChecked) {
    }

    /**
     * q全选
     *
     * @return
     */
    private boolean isAllCheck() {
        for (ShopCarListBean.DataBean group : mDataAdapter.getDataList()) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    private class PreviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.refreshComplete(shopCarListBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(shopCarListBean.getData().size());
                                notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        NetWork.getService(ImpService.class)
                .getShopCar(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopCarListBean>() {
                    private ShopCarListBean shopCarListBean;

                    @Override
                    public void onCompleted() {
                        if (shopCarListBean.getCode() == 0) {
                            showData(shopCarListBean);
                            List<ShopCarListBean.DataBean> data = shopCarListBean.getData();
                            if (data.size()>0){
                                ll_show.setVisibility(View.VISIBLE);
                            }else {
                                ll_show.setVisibility(View.GONE);
                            }

                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mRecyclerView.setRefreshing(false);//同时调用LuRecyclerView的setRefreshing方法
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ShopCarListBean shopCarListBean) {

                        this.shopCarListBean = shopCarListBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(ShopCarListBean shopCarListBean) {

        this.shopCarListBean = shopCarListBean;
    }

    @Override
    public void onResume() {
        super.onResume();
        uid = SpUtils.getSp(getActivity(), "uid");
        onRefresh();
    }

    @Override
    public void onPause() {
        detailBeans.clear();
        super.onPause();
    }
}
