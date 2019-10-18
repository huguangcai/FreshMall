package com.ysxsoft.freshmall.modle;

import java.util.List;

public class YaoQingInfoBean {


    /**
     * code : 0
     * msg : 获取成功
     * pages : 1
     * data : {"count":1,"money":2.25,"yqm":"6546546","ewmurl":"http://192.168.1.163:8888/uploads/images/20190315/4aa9004862054a4eae27f2697591035d.png","list":[{"id":1,"time":"2019-03-12 14:12:58","money":"2.25","phone":"13937253685"}]}
     */

    private int code;
    private String msg;
    private int pages;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * count : 1
         * money : 2.25
         * yqm : 6546546
         * ewmurl : http://192.168.1.163:8888/uploads/images/20190315/4aa9004862054a4eae27f2697591035d.png
         * list : [{"id":1,"time":"2019-03-12 14:12:58","money":"2.25","phone":"13937253685"}]
         */

        private String count;
        private String money;
        private String yqm;
        private String ewmurl;
        private List<ListBean> list;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getYqm() {
            return yqm;
        }

        public void setYqm(String yqm) {
            this.yqm = yqm;
        }

        public String getEwmurl() {
            return ewmurl;
        }

        public void setEwmurl(String ewmurl) {
            this.ewmurl = ewmurl;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * time : 2019-03-12 14:12:58
             * money : 2.25
             * phone : 13937253685
             */

            private int id;
            private String time;
            private String money;
            private String phone;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}
