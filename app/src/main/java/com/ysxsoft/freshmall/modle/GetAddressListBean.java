package com.ysxsoft.freshmall.modle;

import java.util.List;

public class GetAddressListBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":1,"shname":"刘浩东","shphone":"13566323654","shszq":"河南省郑州市二七区","shxxdz":"大法师打发存村","uid":1,"addtime":"2019-03-04 14:15:26","ismr":"1"}]
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
         * id : 1
         * shname : 刘浩东
         * shphone : 13566323654
         * shszq : 河南省郑州市二七区
         * shxxdz : 大法师打发存村
         * uid : 1
         * addtime : 2019-03-04 14:15:26
         * ismr : 1
         */

        private int id;
        private String shname;
        private String shphone;
        private String shszq;
        private String shxxdz;
        private int uid;
        private String addtime;
        private int ismr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShname() {
            return shname;
        }

        public void setShname(String shname) {
            this.shname = shname;
        }

        public String getShphone() {
            return shphone;
        }

        public void setShphone(String shphone) {
            this.shphone = shphone;
        }

        public String getShszq() {
            return shszq;
        }

        public void setShszq(String shszq) {
            this.shszq = shszq;
        }

        public String getShxxdz() {
            return shxxdz;
        }

        public void setShxxdz(String shxxdz) {
            this.shxxdz = shxxdz;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getIsmr() {
            return ismr;
        }

        public void setIsmr(int ismr) {
            this.ismr = ismr;
        }
    }
}
