package com.ysxsoft.freshmall.modle;

import java.util.List;

/**
 * Create By 胡
 * on 2020/1/10 0010
 */
public class WuLiuResponse {

    /**
     * com : huitongkuaidi
     * condition : F00
     * data : [{"context":"已签收，签收人是您好快递已送达如有疑问请致电13903839946/037156278969。轻骑只为佳人笑，给个五星好不好。【请在评价快递员处帮忙点亮五颗小星星】百世快递祝您在2019年百\u201c世\u201d顺心、万事如意","ftime":"2020-01-04 16:15:16","time":"2020-01-04 16:15:16"},{"context":"郑州市【郑州中原区二部】，【刘建伟】正在派件","ftime":"2020-01-04 14:44:54","time":"2020-01-04 14:44:54"},{"context":"到郑州市【郑州中原区二部】","ftime":"2020-01-04 14:43:54","time":"2020-01-04 14:43:54"},{"context":"郑州市【郑州转运中心】，正发往【郑州中原区二部】","ftime":"2020-01-04 03:34:56","time":"2020-01-04 03:34:56"},{"context":"到郑州市【郑州转运中心】","ftime":"2020-01-04 03:22:40","time":"2020-01-04 03:22:40"},{"context":"重庆市【重庆转运中心】，正发往【郑州转运中心】","ftime":"2020-01-02 22:53:38","time":"2020-01-02 22:53:38"},{"context":"到重庆市【重庆转运中心】","ftime":"2020-01-02 22:51:57","time":"2020-01-02 22:51:57"},{"context":"到重庆市【空港一部】","ftime":"2020-01-01 18:31:40","time":"2020-01-01 18:31:40"},{"context":"【空港一部】揽收成功","ftime":"2020-01-01 18:28:54","time":"2020-01-01 18:28:54"},{"context":"等待揽收中","ftime":"2020-01-01 11:24:03","time":"2020-01-01 11:24:03"},{"context":"商品已经下单","ftime":"2019-12-31 20:35:52","time":"2019-12-31 20:35:52"}]
     * ischeck : 1
     * message : ok
     * nu : 70284941245379
     * state : 3
     * status : 200
     */

    private String com;
    private String condition;
    private String ischeck;
    private String message;
    private String nu;
    private String state;
    private String status;
    private List<DataBean> data;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * context : 已签收，签收人是您好快递已送达如有疑问请致电13903839946/037156278969。轻骑只为佳人笑，给个五星好不好。【请在评价快递员处帮忙点亮五颗小星星】百世快递祝您在2019年百“世”顺心、万事如意
         * ftime : 2020-01-04 16:15:16
         * time : 2020-01-04 16:15:16
         */

        private String context;
        private String ftime;
        private String time;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
