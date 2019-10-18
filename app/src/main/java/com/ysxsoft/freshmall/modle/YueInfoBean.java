package com.ysxsoft.freshmall.modle;

import java.util.List;

public class YueInfoBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":1,"userid":1,"type":"1","jiner":-100,"times":"2019-03-04 14:23:12","beizhu":"手动支出"},{"id":2,"userid":1,"type":"0","jiner":20,"times":"2019-03-04 14:23:12","beizhu":"手动充值"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;
    /**
     * pages : 1
     */

    private int pages;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public static class DataBean {
        /**
         * id : 1
         * userid : 1
         * type : 1
         * jiner : -100
         * times : 2019-03-04 14:23:12
         * beizhu : 手动支出
         */

        private int id;
        private String userid;
        private String type;
        private String jiner;
        private String times;
        private String beizhu;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getJiner() {
            return jiner;
        }

        public void setJiner(String jiner) {
            this.jiner = jiner;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getBeizhu() {
            return beizhu;
        }

        public void setBeizhu(String beizhu) {
            this.beizhu = beizhu;
        }
    }
}
