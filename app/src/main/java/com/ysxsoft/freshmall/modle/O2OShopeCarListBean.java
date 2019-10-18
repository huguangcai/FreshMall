package com.ysxsoft.freshmall.modle;

import java.util.List;

public class O2OShopeCarListBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":1,"scuserid":3,"scspid":1,"sliang":"3","ssdpid":6,"fmtp":"http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png","spjiage":369,"spname":"猫粮240kg"}]
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
         * scuserid : 3
         * scspid : 1
         * sliang : 3
         * ssdpid : 6
         * fmtp : http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png
         * spjiage : 369
         * spname : 猫粮240kg
         */

        private String id;
        private String scuserid;
        private String scspid;
        private String sliang;
        private String ssdpid;
        private String fmtp;
        private String spjiage;
        private String spname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScuserid() {
            return scuserid;
        }

        public void setScuserid(String scuserid) {
            this.scuserid = scuserid;
        }

        public String getScspid() {
            return scspid;
        }

        public void setScspid(String scspid) {
            this.scspid = scspid;
        }

        public String getSliang() {
            return sliang;
        }

        public void setSliang(String sliang) {
            this.sliang = sliang;
        }

        public String getSsdpid() {
            return ssdpid;
        }

        public void setSsdpid(String ssdpid) {
            this.ssdpid = ssdpid;
        }

        public String getFmtp() {
            return fmtp;
        }

        public void setFmtp(String fmtp) {
            this.fmtp = fmtp;
        }

        public String getSpjiage() {
            return spjiage;
        }

        public void setSpjiage(String spjiage) {
            this.spjiage = spjiage;
        }

        public String getSpname() {
            return spname;
        }

        public void setSpname(String spname) {
            this.spname = spname;
        }
    }
}
