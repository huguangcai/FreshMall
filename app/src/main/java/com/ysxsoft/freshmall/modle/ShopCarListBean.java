package com.ysxsoft.freshmall.modle;

import java.util.List;

public class ShopCarListBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":3,"uid":1,"sid":3,"time":"2019-03-16 13:45:14","num":6,"more":"红色","name":"发送到","price":20,"pic":"http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"},{"id":4,"uid":1,"sid":3,"time":"2019-03-16 13:45:23","num":3,"more":"白色","name":"发送到","price":20,"pic":"http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * id : 3
         * uid : 1
         * sid : 3
         * time : 2019-03-16 13:45:14
         * num : 6
         * more : 红色
         * name : 发送到
         * price : 20
         * pic : http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg
         */

        private int id;
        private String uid;
        private String sid;
        private String time;
        private String num;
        private String more;
        private String name;
        private String price;
        private String pic;

        private boolean isChoosed;

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
