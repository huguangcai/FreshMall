package com.ysxsoft.freshmall.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.freshmall.R;
import com.ysxsoft.freshmall.adapter.MyOrderAdapter;
import com.ysxsoft.freshmall.dialog.O2OPayDialog;
import com.ysxsoft.freshmall.dialog.ShowQrCodeDialog;
import com.ysxsoft.freshmall.impservice.ImpService;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.O2OAlipayBean;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.modle.O2OOrderBean;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.utils.AppUtil;
import com.ysxsoft.freshmall.utils.BaseActivity;
import com.ysxsoft.freshmall.utils.CustomDialog;
import com.ysxsoft.freshmall.utils.ImageLoadUtil;
import com.ysxsoft.freshmall.utils.NetWork;
import com.ysxsoft.freshmall.utils.SpUtils;
import com.ysxsoft.freshmall.utils.alipay.PayResult;
import com.ysxsoft.freshmall.widget.pageslidingtableview.PageSlidingTableView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private PageSlidingTableView slidingTableView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private MyOrderAdapter mDataAdapter;
    private int page = 1;
    private int type = 0;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private O2OOrderBean o2OOrderBean;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private double latitude;
    private double longitude;
    private double totalPrice = 0.00;// 购买的商品总价
    private int payType = 1;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx4e6f9ac9432f741d");
        api.registerApp("wx4e6f9ac9432f741d");
        initBdMap();
        setBackVisibily();
        setTitle("我的订单");
        initView();
    }
    private void initBdMap() {
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setScanSpan(3000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
        }
    }

    private void initView() {
        slidingTableView = getViewById(R.id.pstv_indicator);
        slidingTableView.setTabTitles(new String[]{"全部", "待付款", "待使用", "待评价"});
        slidingTableView.setOnTabClickListener(new PageSlidingTableView.onTabClickListener() {
            @Override
            public void onTabClick(String title, int position) {
                switch (position) {
                    case 0:
//                        o2o_order_item_layout
                        type = 0;
                        page = 1;
                        onRefresh();
                        break;
                    case 1:
                        type = 1;
                        page = 1;
                        onRefresh();
                        break;
                    case 2:
                        type = 2;
                        page = 1;
                        onRefresh();
                        break;
                    case 3:
                        type = 3;
                        page = 1;
                        onRefresh();
                        break;
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new MyOrderAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                O2OOrderBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String id = dataBean.getId();
                switch (dataBean.getDdsates()) {
                    case "0":
                        Intent payintent=new Intent(mContext,O2OWaitPayActivity.class);
                        payintent.putExtra("order_id",id);
                        payintent.putExtra("latitude",String.valueOf(latitude));
                        payintent.putExtra("longitude",String.valueOf(longitude));
                        startActivity(payintent);
                        break;
                    case "1":
                        Intent userintent=new Intent(mContext,O2OWaitUseOrderActivity.class);
                        userintent.putExtra("order_id",id);
                        userintent.putExtra("latitude",String.valueOf(latitude));
                        userintent.putExtra("longitude",String.valueOf(longitude));
                        startActivity(userintent);
                        break;
                    case "2":
                        Intent intent=new Intent(mContext,EvaluateActivity.class);
                        intent.putExtra("order_id",id);
                        startActivity(intent);
                        break;
                    case "3":
                        Intent intent3=new Intent(mContext,RefundingMoneyActivity.class);
                        intent3.putExtra("order_id",id);
                        intent3.putExtra("latitude",String.valueOf(latitude));
                        intent3.putExtra("longitude",String.valueOf(longitude));
                        startActivity(intent3);
                        break;
                    case "4":
                        Intent intent4=new Intent(mContext,RefundingMoneyActivity.class);
                        intent4.putExtra("order_id",id);
                        intent4.putExtra("latitude",String.valueOf(latitude));
                        intent4.putExtra("longitude",String.valueOf(longitude));
                        intent4.putExtra("ok","ok");
                        startActivity(intent4);
                        break;
                    case "5"://已完成
                        Intent compeletintent=new Intent(mContext,O2OCompleteActivity.class);
                        compeletintent.putExtra("order_id",id);
                        compeletintent.putExtra("latitude",String.valueOf(latitude));
                        compeletintent.putExtra("longitude",String.valueOf(longitude));
                        startActivity(compeletintent);
                        break;
                }
            }
        });
        mDataAdapter.setOnPayItemClickListener(new MyOrderAdapter.OnPayItemClickListener() {
            @Override
            public void OnCancleClick(int position) {
                O2OOrderBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String id = dataBean.getId();
                switch (dataBean.getDdsates()) {
                    case "0":
                        CancleOrder(id);
                        break;
                    case "1":
                        Intent intent=new Intent(mContext,O2OApplyRefundMoneyActivity.class);
                        intent.putExtra("order_id",id);
                        intent.putExtra("latitude",String.valueOf(latitude));
                        intent.putExtra("longitude",String.valueOf(longitude));
                        startActivity(intent);
                        break;
                    case "2"://待评价
                        break;
                    case "3"://退款中
                        break;
                    case "4"://退款已完成
                        break;
                    case "5"://已完成
                        break;
                }
            }

            @Override
            public void OnPayClick(int position) {
                List<O2OOrderBean.DataBean> dataList = mDataAdapter.getDataList();
                O2OOrderBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                final String id = dataBean.getId();
                switch (dataBean.getDdsates()) {
                    case "0"://立即支付
                        totalPrice = 0.00;
                        if ("0".equals(dataBean.getDdsates())) {
                            for (int i = 0; i < dataList.size(); i++) {
                                O2OOrderBean.DataBean dataBean1 = dataList.get(i);
                                totalPrice += Double.valueOf(dataBean1.getJiner()) * Double.valueOf(dataBean1.getShuliang());
                            }
                        }
                        totalPrice = Double.valueOf(dataBean.getJiner()) * Double.valueOf(dataBean.getShuliang());
                        final O2OPayDialog dialog = new O2OPayDialog(mContext);
                        LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
                        final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
                        img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                        LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
                        final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
                        TextView tv_money = dialog.findViewById(R.id.tv_money);
                        tv_money.setText(totalPrice + "");
                        TextView tv_check_pay = dialog.findViewById(R.id.tv_check_pay);
                        ll_alipay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                img_alipay.setBackgroundResource(R.mipmap.img_ok_dui);
                                img_wechatpay.setBackgroundResource(R.mipmap.img_normal_dui);
                                payType = 1;

                            }
                        });
                        ll_wechatpay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                img_alipay.setBackgroundResource(R.mipmap.img_normal_dui);
                                img_wechatpay.setBackgroundResource(R.mipmap.img_ok_dui);
                                payType = 2;

                            }
                        });
                        tv_check_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                switch (payType) {
                                    case 1:
                                        AliPayData(id);
                                        break;
                                    case 2:
                                        WxChatPay(id);
                                        break;
                                }
                            }
                        });
                        dialog.show();
                        break;
                    case "1"://扫码支付
                        ScanData(id);
                        break;
                    case "2"://立即评价
                        Intent intent=new Intent(mContext,EvaluateActivity.class);
                        intent.putExtra("order_id",id);
                        startActivity(intent);
                        break;
                    case "3"://退款中
                        Intent intent3=new Intent(mContext,RefundingMoneyActivity.class);
                        intent3.putExtra("order_id",id);
                        intent3.putExtra("latitude",String.valueOf(latitude));
                        intent3.putExtra("longitude",String.valueOf(longitude));
                        startActivity(intent3);
                        break;
                    case "4"://退款已完成
                        Intent intent4=new Intent(mContext,RefundingMoneyActivity.class);
                        intent4.putExtra("order_id",id);
                        intent4.putExtra("latitude",String.valueOf(latitude));
                        intent4.putExtra("longitude",String.valueOf(longitude));
                        intent4.putExtra("ok","ok");
                        startActivity(intent4);
                        break;
                    case "5"://已完成
                        Intent compeletintent=new Intent(mContext,O2OCompleteActivity.class);
                        compeletintent.putExtra("order_id",id);
                        compeletintent.putExtra("latitude",String.valueOf(latitude));
                        compeletintent.putExtra("longitude",String.valueOf(longitude));
                        startActivity(compeletintent);
                        break;
                }
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (o2OOrderBean != null) {
                    if (page < o2OOrderBean.getPages()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");

    }

    private void WxChatPay(String id) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .O2OWxChatPay(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WxPayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WxPayBean wxPayBean) {
                        if ("0".equals(wxPayBean.getCode())) {
                            PayReq req = new PayReq();
                            req.appId = wxPayBean.getData().getAppid();
                            req.partnerId = wxPayBean.getData().getPartnerid();
                            req.prepayId = wxPayBean.getData().getPrepayid();
                            req.nonceStr = wxPayBean.getData().getNoncestr();
                            req.timeStamp = String.valueOf(wxPayBean.getData().getTimestamp());
                            req.packageValue = wxPayBean.getData().getPackageX();
                            req.sign = wxPayBean.getData().getSign();
                            req.extData = "app data"; // optional
//                            showToastMessage("正常调起支付");
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                            wxPay.dismiss();
                        }
                    }
                });
    }

    private void AliPayData(String orderInfo) {
        NetWork.getService(ImpService.class)
                .O2OAlipay(orderInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OAlipayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(O2OAlipayBean o2OAlipayBean) {
                        if (o2OAlipayBean.getCode()==0){
                            AliPay(o2OAlipayBean.getData());
                        }
                    }
                });


    }

    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(MyOrderActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                log(result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                Handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToastMessage("支付成功");
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
//                    tv_check_pay.setEnabled(true);
                    break;
                }
            }
        }
    };
    private void ScanData(String order_id) {
        NetWork.getService(ImpService.class)
                .O2OOederDetail(order_id, String.valueOf(latitude), String.valueOf(longitude))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OOederDetailBean>() {
                    private O2OOederDetailBean o2OOederDetailBean;

                    @Override
                    public void onCompleted() {
                        if (o2OOederDetailBean.getCode() == 0) {
                            O2OOederDetailBean.DataBean data = o2OOederDetailBean.getData();
                            ShowQrCodeDialog dialog=new ShowQrCodeDialog(mContext);
                            ImageView img_qrCode = dialog.findViewById(R.id.img_qrCode);
                            ImageLoadUtil.GlideGoodsImageLoad(mContext,data.getZfewm(),img_qrCode);
                            TextView tv_phone_num = dialog.findViewById(R.id.tv_phone_num);
                            tv_phone_num.setText(data.getYzms());
                            dialog.show();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(O2OOederDetailBean o2OOederDetailBean) {

                        this.o2OOederDetailBean = o2OOederDetailBean;
                    }
                });
    }

    private void CancleOrder(String id) {
        NetWork.getService(ImpService.class)
                .O2OCancleOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonBean>() {
                    private CommonBean commonBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(commonBean.getMsg());
                        if (commonBean.getCode() == 0) {
                            onRefresh();
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
    public int getLayout() {
        return R.layout.o2o_order_layout;
    }

    @Override
    public void onRefresh() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(mContext)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {
        private WeakReference<MyOrderActivity> ref;

        PreviewHandler(MyOrderActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyOrderActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(o2OOrderBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(o2OOrderBean.getData().size());
                                activity.notifyDataSetChanged();
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
                .O2OOrderData(String.valueOf(type), SpUtils.getSp(mContext, "uid"), String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<O2OOrderBean>() {
                    private O2OOrderBean o2OOrderBean;

                    @Override
                    public void onCompleted() {
                        if (o2OOrderBean.getCode() == 0) {
                            showData(o2OOrderBean);
                            List<O2OOrderBean.DataBean> data = o2OOrderBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mRecyclerView.setRefreshing(false);//同时调用LuRecyclerView的setRefreshing方法
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(O2OOrderBean o2OOrderBean) {

                        this.o2OOrderBean = o2OOrderBean;
                    }
                });
    }

    private void showData(O2OOrderBean o2OOrderBean) {
        this.o2OOrderBean = o2OOrderBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
