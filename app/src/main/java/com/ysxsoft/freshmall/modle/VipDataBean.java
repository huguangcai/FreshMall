package com.ysxsoft.freshmall.modle;

public class VipDataBean {


    /**
     * code : 0
     * msg : 反馈成功
     * data : {"id":1,"zsvip":2,"appdownurl":"37","huiyuan1":15,"huiyuan6":90,"huiyuan12":180,"times":"1995-10-27 10:25:55","isvip":1,"viptotime":"1995-10-27 10:25:55"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * zsvip : 2
         * appdownurl : 37
         * huiyuan1 : 15
         * huiyuan6 : 90
         * huiyuan12 : 180
         * times : 1995-10-27 10:25:55
         * isvip : 1
         * viptotime : 1995-10-27 10:25:55
         */

        private String id;
        private String zsvip;
        private String appdownurl;
        private String huiyuan1;
        private String huiyuan6;
        private String huiyuan12;
        private String times;
        private String isvip;
        private String viptotime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getZsvip() {
            return zsvip;
        }

        public void setZsvip(String zsvip) {
            this.zsvip = zsvip;
        }

        public String getAppdownurl() {
            return appdownurl;
        }

        public void setAppdownurl(String appdownurl) {
            this.appdownurl = appdownurl;
        }

        public String getHuiyuan1() {
            return huiyuan1;
        }

        public void setHuiyuan1(String huiyuan1) {
            this.huiyuan1 = huiyuan1;
        }

        public String getHuiyuan6() {
            return huiyuan6;
        }

        public void setHuiyuan6(String huiyuan6) {
            this.huiyuan6 = huiyuan6;
        }

        public String getHuiyuan12() {
            return huiyuan12;
        }

        public void setHuiyuan12(String huiyuan12) {
            this.huiyuan12 = huiyuan12;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        public String getViptotime() {
            return viptotime;
        }

        public void setViptotime(String viptotime) {
            this.viptotime = viptotime;
        }
    }
}
