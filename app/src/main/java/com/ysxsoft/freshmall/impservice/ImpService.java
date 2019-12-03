package com.ysxsoft.freshmall.impservice;

import com.ysxsoft.freshmall.modle.AboutMyBean;
import com.ysxsoft.freshmall.modle.AliPayBean;
import com.ysxsoft.freshmall.modle.CheckMoneyBean;
import com.ysxsoft.freshmall.modle.ColleteListBean;
import com.ysxsoft.freshmall.modle.CommonBean;
import com.ysxsoft.freshmall.modle.CustomerPhoneBean;
import com.ysxsoft.freshmall.modle.DeleteAddressBean;
import com.ysxsoft.freshmall.modle.GetAddressListBean;
import com.ysxsoft.freshmall.modle.GetTypeInfoBean;
import com.ysxsoft.freshmall.modle.GetTypeListBean;
import com.ysxsoft.freshmall.modle.GoodDetailBean;
import com.ysxsoft.freshmall.modle.HomeBestBean;
import com.ysxsoft.freshmall.modle.HomeBestMoreBean;
import com.ysxsoft.freshmall.modle.HomeLunBoBean;
import com.ysxsoft.freshmall.modle.HomeNewBean;
import com.ysxsoft.freshmall.modle.HomeSeckillBean;
import com.ysxsoft.freshmall.modle.HomelistBean;
import com.ysxsoft.freshmall.modle.InfoDeailtBean;
import com.ysxsoft.freshmall.modle.InfoListBean;
import com.ysxsoft.freshmall.modle.LoginBean;
import com.ysxsoft.freshmall.modle.MallDetailBean;
import com.ysxsoft.freshmall.modle.MallTypeBean;
import com.ysxsoft.freshmall.modle.NearByMallBean;
import com.ysxsoft.freshmall.modle.NumberBean;
import com.ysxsoft.freshmall.modle.O2OAlipayBean;
import com.ysxsoft.freshmall.modle.O2OOederDetailBean;
import com.ysxsoft.freshmall.modle.O2OOrderBean;
import com.ysxsoft.freshmall.modle.O2OPicBean;
import com.ysxsoft.freshmall.modle.O2OSearchDataBean;
import com.ysxsoft.freshmall.modle.O2OShopeCarListBean;
import com.ysxsoft.freshmall.modle.OneClassifyBean;
import com.ysxsoft.freshmall.modle.OrderDetailBean;
import com.ysxsoft.freshmall.modle.OrderListBean;
import com.ysxsoft.freshmall.modle.RegisterBean;
import com.ysxsoft.freshmall.modle.RepasswordBean;
import com.ysxsoft.freshmall.modle.ScOrderDataBean;
import com.ysxsoft.freshmall.modle.SearcRecordeDataBean;
import com.ysxsoft.freshmall.modle.SearchBean;
import com.ysxsoft.freshmall.modle.SearchResultBean;
import com.ysxsoft.freshmall.modle.SecikllMoreBean;
import com.ysxsoft.freshmall.modle.SendMessageBean;
import com.ysxsoft.freshmall.modle.ShareDataBean;
import com.ysxsoft.freshmall.modle.ShopAlipayBean;
import com.ysxsoft.freshmall.modle.ShopCarListBean;
import com.ysxsoft.freshmall.modle.ShopEvaluateBean;
import com.ysxsoft.freshmall.modle.TypeShopListBean;
import com.ysxsoft.freshmall.modle.UserInfoBean;
import com.ysxsoft.freshmall.modle.UserRulesBean;
import com.ysxsoft.freshmall.modle.UserRulesDetailBean;
import com.ysxsoft.freshmall.modle.VipDataBean;
import com.ysxsoft.freshmall.modle.WxPayBean;
import com.ysxsoft.freshmall.modle.YaoQingInfoBean;
import com.ysxsoft.freshmall.modle.YueInfoBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ImpService {
    /**
     * 发送短信验证码
     */
    @FormUrlEncoded
    @POST("index/sendcode")
    Observable<SendMessageBean> sendMessage(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("index/register")
    Observable<RegisterBean> Register(@Field("phone") String phone,
                                      @Field("code") String code,
                                      @Field("pwd") String pwd,
                                      @Field("type") String type,
                                      @Field("yqm") String yqm,
                                      @Field("redz") String redz);

    @FormUrlEncoded
    @POST("index/login")
    Observable<LoginBean> Login(@Field("phone") String phone,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("index/repassword")
    Observable<RepasswordBean> Repassword(@Field("phone") String phone,
                                          @Field("code") String code,
                                          @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("banner/getBanner")
    Observable<HomeLunBoBean> LunBoData(@Field("uid") String uid);


    @FormUrlEncoded
    @POST("shop/getTypeInfo")
    Observable<GetTypeInfoBean> GetTypeInfoData(@Field("type") String type,
                                                @Field("id") String id);


    @FormUrlEncoded
    @POST("shop/getTypeList")
    Observable<GetTypeListBean> GetTypeListData(@Field("uid") String uid,
                                                @Field("id") String id,
                                                @Field("type") String type);

    @FormUrlEncoded
    @POST("address/getAddressList")
    Observable<GetAddressListBean> GetAddressListData(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("address/delAddress")
    Observable<DeleteAddressBean> DeleteAddressData(@Field("uid") String uid,
                                                    @Field("id") String id);

    @FormUrlEncoded
    @POST("address/setDefaultAddress")
    Observable<DeleteAddressBean> setDefaultAddressData(@Field("uid") String uid,
                                                        @Field("id") String id);

    @FormUrlEncoded
    @POST("address/addAddress")
    Observable<DeleteAddressBean> addAddressData(@Field("uid") String uid,
                                                 @Field("name") String name,
                                                 @Field("phone") String phone,
                                                 @Field("city") String city,
                                                 @Field("address") String address);

    @FormUrlEncoded
    @POST("address/updateAddress")
    Observable<DeleteAddressBean> updateAddressData(@Field("uid") String uid,
                                                    @Field("id") String id,
                                                    @Field("name") String name,
                                                    @Field("phone") String phone,
                                                    @Field("city") String city,
                                                    @Field("address") String address);

    @FormUrlEncoded
    @POST("address/getDefaultAddress")
    Observable<GetAddressListBean> getDefaultAddressData(@Field("uid") String uid);


    @FormUrlEncoded
    @POST("shop/getGoodDetail")
    Observable<GoodDetailBean> getGoodDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("my/getUserInfo")
    Observable<UserInfoBean> getUserInfo(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("my/getYueInfo")
    Observable<YueInfoBean> getYueInfo(@Field("uid") String uid,
                                       @Field("type") String type,
                                       @Field("page") String page);

    @FormUrlEncoded
    @POST("my/getYaoQingInfo")
    Observable<YaoQingInfoBean> getYaoQingInfo(@Field("uid") String uid,
                                               @Field("page") String page);

    @FormUrlEncoded
    @POST("shop/getShopEvaluate")
    Observable<ShopEvaluateBean> getShopEvaluate(@Field("id") String id,
                                                 @Field("page") String page);

    @FormUrlEncoded
    @POST("shopcar/addShopCar")
    Observable<CommonBean> addShopCar(@Field("id") String id,
                                      @Field("uid") String uid,
                                      @Field("num") String num,
                                      @Field("more") String more);

    @FormUrlEncoded
    @POST("shop/getTypeShopList")
    Observable<TypeShopListBean> getTypeShopList(@Field("uid") String uid,
                                                 @Field("id") String id,
                                                 @Field("page") String page);

    @FormUrlEncoded
    @POST("shopcar/getShopCar")
    Observable<ShopCarListBean> getShopCar(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("shopcar/delShopCar")
    Observable<CommonBean> delShopCar(@Field("id") String id,
                                      @Field("uid") String uid);

    @FormUrlEncoded
    @POST("shopcar/editShopCar")
    Observable<CommonBean> editShopCar(@Field("id") String id,
                                       @Field("uid") String uid,
                                       @Field("num") String num);

    @FormUrlEncoded
    @POST("pays/ShopAlipay")
    Observable<ShopAlipayBean> ShopAlipay(@Field("uid") String uid,
                                          @Field("aid") String aid,
                                          @Field("goods") String goods,
                                          @Field("shopcar") String shopcar);

    @FormUrlEncoded
    @POST("order/getOrderList")
    Observable<OrderListBean> getOrderList(@Field("uid") String uid,
                                           @Field("status") String status,
                                           @Field("page") String page);


    @FormUrlEncoded
    @POST("order/delOrder")
    Observable<CommonBean> delOrder(@Field("id") String id,
                                    @Field("uid") String uid);

    @FormUrlEncoded
    @POST("pays/OrderAlipay")
    Observable<ShopAlipayBean> OrderAlipay(@Field("id") String id);

    @POST("order/pingjia")
    Observable<CommonBean> OrderEvaluate(@Body RequestBody body);


    @FormUrlEncoded
    @POST("order/qrshOrder")
    Observable<CommonBean> CheckShouHuo(@Field("id") String id,
                                        @Field("uid") String uid);

    @FormUrlEncoded
    @POST("order/upload_time")
    Observable<CommonBean> TipFaHuo(@Field("id") String id,
                                    @Field("uid") String uid);

    @FormUrlEncoded
    @POST("my/addFeedback")
    Observable<CommonBean> Feedback(@Field("uid") String uid,
                                    @Field("content") String content);


    @POST("order/refund")
    Observable<CommonBean> RefundMoney(@Body RequestBody body);

    @FormUrlEncoded
    @POST("order/add_wuliu")
    Observable<CommonBean> AddWuLiu(@Field("oid") String oid,
                                    @Field("uid") String uid,
                                    @Field("logistics") String logistics,
                                    @Field("logistics_num") String logistics_num);

    @FormUrlEncoded
    @POST("O2oindex/sousuo")
    Observable<SearchBean> SearchData(@Field("keywords") String keywords,
                                      @Field("type") String type,
                                      @Field("px") String px,
                                      @Field("jingdu") String jingdu,
                                      @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("O2oindex/typelist")
    Observable<OneClassifyBean> OneClassifyData();


    @FormUrlEncoded
    @POST("order/orderDetail")
    Observable<OrderDetailBean> orderDetail(@Field("id") String id);

    @FormUrlEncoded
    @POST("O2oindex/fujindlist")
    Observable<NearByMallBean> nearByMall(@Field("jingdu") String jingdu,
                                          @Field("weidu") String weidu);


    @FormUrlEncoded
    @POST("O2oindex/sousuo")
    Observable<O2OSearchDataBean> O2OSearchData(@Field("keywords") String keywords,
                                                @Field("type") String type,
                                                @Field("px") String px,
                                                @Field("jingdu") String jingdu,
                                                @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("O2oindex/dianshow")
    Observable<MallDetailBean> MallDetailData(@Field("uid") String uid,
                                              @Field("did") String did,
                                              @Field("jingdu") String jingdu,
                                              @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("O2oindex/scandqx")
    Observable<CommonBean> CancleCollectData(@Field("uid") String uid,
                                             @Field("did") String did);

    @FormUrlEncoded
    @POST("O2oindex/yisuosuo")
    Observable<O2OSearchDataBean> FirstO2OSearchData(@Field("typeid") String typeid,
                                                     @Field("type") String type,
                                                     @Field("px") String px,
                                                     @Field("jingdu") String jingdu,
                                                     @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("O2oindex/ersuosuo")
    Observable<O2OSearchDataBean> SecondO2OSearchData(@Field("typeid") String typeid,
                                                      @Field("type") String type,
                                                      @Field("px") String px,
                                                      @Field("jingdu") String jingdu,
                                                      @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("o2odingdan/ddlist")
    Observable<O2OOrderBean> O2OOrderData(@Field("type") String type,
                                          @Field("uid") String uid,
                                          @Field("page") String page);

    @FormUrlEncoded
    @POST("o2oindex/gwcupcz")
    Observable<CommonBean> O2OShopeCarData(@Field("type") String type,
                                           @Field("id") String id,
                                           @Field("shuliang") String shuliang,
                                           @Field("uid") String uid,
                                           @Field("did") String did);


    @FormUrlEncoded
    @POST("O2oindex/gwclist")
    Observable<O2OShopeCarListBean> O2OShopeCarList(@Field("uid") String uid,
                                                    @Field("did") String did);

    @FormUrlEncoded
    @POST("my/vipjiage")
    Observable<VipDataBean> VipData(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("my/chongzhi")
    Observable<CommonBean> RechagerCarData(@Field("uid") String uid,
                                           @Field("khao") String khao,
                                           @Field("mima") String mima);

    @FormUrlEncoded
    @POST("o2odingdan/scdd")
    Observable<ScOrderDataBean> ScOrderData(@Field("dpid") String dpid,
                                            @Field("uid") String uid);


    @FormUrlEncoded
    @POST("o2odingdan/nodd")
    Observable<CommonBean> O2OCancleOrder(@Field("id") String id);

    @POST("o2odingdan/ljpj")
    Observable<CommonBean> O2OEvaluate(@Body RequestBody body);

    @FormUrlEncoded
    @POST("o2odingdan/ddshow")
    Observable<O2OOederDetailBean> O2OOederDetail(@Field("id") String id,
                                                  @Field("jingdu") String jingdu,
                                                  @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("o2odingdan/sqtk")
    Observable<CommonBean> ApplyRefundMoney(@Field("id") String id,
                                            @Field("tkyy") String tkyy,
                                            @Field("tksm") String tksm);

    @FormUrlEncoded
    @POST("message/getMsgList")
    Observable<InfoListBean> InfoList(@Field("uid") String uid,
                                      @Field("page") String page);

    @FormUrlEncoded
    @POST("message/getMsgDetail")
    Observable<InfoDeailtBean> InfoDeailt(@Field("uid") String uid,
                                          @Field("id") String id);

    @FormUrlEncoded
    @POST("Scindex/sousuo")
    Observable<SearchResultBean> SearchResultData(@Field("page") String page,
                                                  @Field("type") String type,
                                                  @Field("keyword") String keyword,
                                                  @Field("uid") String uid);

    @FormUrlEncoded
    @POST("Scindex/sousuojilu")
    Observable<SearcRecordeDataBean> SearcRecordeData(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("message/delmsg")
    Observable<CommonBean> DeleteInfoData(@Field("id") String id);

    @POST("Scindex/ddlist")
    Observable<HomelistBean> Homelist();

    @POST("Scindex/dayms")
    Observable<HomeSeckillBean> HomeSeckillData();


    @POST("Scindex/xptjlist")
    Observable<HomeNewBean> HomeNewData();


    @POST("scindex/pzyxlist")
    Observable<HomeBestBean> HomeBestData();

    @FormUrlEncoded
    @POST("Scindex/gdtjlist")
    Observable<HomeBestMoreBean> HomeBestMoreData(@Field("page") String page,
                                                  @Field("uid") String uid);

    @FormUrlEncoded
    @POST("scindex/mslist")
    Observable<SecikllMoreBean> SecikllMoreData(@Field("page") String page);

    @POST("my/userszlist")
    Observable<UserRulesBean> UserRules();

    @FormUrlEncoded
    @POST("my/userszshow")
    Observable<UserRulesDetailBean> UserRulesDetail(@Field("id") String id);


    @POST("my/gywm")
    Observable<AboutMyBean> AboutMy();


    @POST("my/fenxiang")
    Observable<ShareDataBean> ShareData();

    @POST("O2oindex/o2opic")
    Observable<O2OPicBean> O2OPic();

    @POST("index/pfzztj")
    Observable<CommonBean> BusinessData(@Body RequestBody body);


    @POST("my/dptypelist")
    Observable<MallTypeBean> MallTypeData();

    @FormUrlEncoded
    @POST("my/sclist")
    Observable<ColleteListBean> ColleteListData(@Field("uid") String uid,
                                                @Field("jingdu") String jingdu,
                                                @Field("weidu") String weidu);

    @FormUrlEncoded
    @POST("my/upuser")
    Observable<CommonBean> PersonNameData(@Field("uid") String uid,
                                          @Field("type") String type,
                                          @Field("username") String username);

    @POST("my/upuser")
    Observable<CommonBean> PersonPicData(@Body RequestBody body);

    @FormUrlEncoded
    @POST("Scindex/delsslist")
    Observable<CommonBean> DeleteRecordData(@Field("uid") String uid);

    @POST("my/zxkf")
    Observable<CustomerPhoneBean> CustomerPhoneData();


    @FormUrlEncoded
    @POST("pays/ShopYuepay")
    Observable<CommonBean> BalanceData(@Field("uid") String uid,
                                       @Field("aid") String aid,
                                       @Field("goods") String goods,
                                       @Field("shopcar") String shopcar);

    @FormUrlEncoded
    @POST("pays/OrderYuepay")
    Observable<CommonBean> OrderBalanceData(@Field("id") String id);

    @FormUrlEncoded
    @POST("shop/getTypeShopList")
    Observable<SearchResultBean> SearchGetTypeListData(@Field("uid") String uid,
                                                       @Field("id") String id,
                                                       @Field("page") String page);

    @POST("O2oindex/typelist")
    Observable<GetTypeListBean> O2OClassifyData();

    @FormUrlEncoded
    @POST("O2oindex/erjilist")
    Observable<GetTypeListBean> O2OErClassifyData(@Field("id") String id);

    @FormUrlEncoded
    @POST("scindex/ddcount")
    Observable<NumberBean> getNumber(@Field("uid") String uid);


    @FormUrlEncoded
    @POST("pays/ShopWxpay")
    Observable<WxPayBean> WxChatPay(@Field("uid") String uid,
                                    @Field("aid") String aid,
                                    @Field("goods") String goods,
                                    @Field("shopcar") String shopcar);

    @FormUrlEncoded
    @POST("o2odingdan/Alipay")
    Observable<O2OAlipayBean> O2OAlipay(@Field("id") String id);

    @FormUrlEncoded
    @POST("o2odingdan/Wxpay")
    Observable<WxPayBean> O2OWxChatPay(@Field("id") String id);

    @FormUrlEncoded
    @POST("pays/Orderwxpay")
    Observable<WxPayBean> Orderwxpay(@Field("id") String id);

    @FormUrlEncoded
    @POST("pays/yueczzfb")
    Observable<AliPayBean> AliPayRecharge(@Field("uid") String uid,
                                          @Field("jiner") String jiner);

    @FormUrlEncoded
    @POST("pays/zfbuservip")
    Observable<AliPayBean> AliPayVip(@Field("uid") String uid,
                                     @Field("type") String type);

    @FormUrlEncoded
    @POST("pays/wxpayyecz")
    Observable<WxPayBean> WxChatRecharge(@Field("uid") String uid,
                                         @Field("jiner") String jiner);


    @FormUrlEncoded
    @POST("pays/userwxvipzf")
    Observable<WxPayBean> WxChatVip(@Field("uid") String uid,
                                    @Field("type") String type);


    @FormUrlEncoded
    @POST("order/youfei")
    Observable<CheckMoneyBean> CheckMoney(@Field("jiage") String jiage);


}
