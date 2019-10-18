package com.ysxsoft.freshmall.modle;

import java.util.List;

public class SearchBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":6,"dname":"豪享来吃吃吃","imgfm":"http://oto.com/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg","dpjwd":"113.611325,34.801765","fwl":"0","xiaoliang":1,"junjia":395,"jvli":252.2,"showjl":"252.2米"},{"id":7,"dname":"豪享来吃吃2","imgfm":"http://oto.com/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png","dpjwd":"114.376038,34.81461","fwl":"0","xiaoliang":0,"junjia":0,"jvli":85372.6,"showjl":"85.3726千米"}]
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
         * id : 6
         * dname : 豪享来吃吃吃
         * imgfm : http://oto.com/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg
         * dpjwd : 113.611325,34.801765
         * fwl : 0
         * xiaoliang : 1
         * junjia : 395
         * jvli : 252.2
         * showjl : 252.2米
         */

        private String id;
        private String dname;
        private String imgfm;
        private String dpjwd;
        private String fwl;
        private String xiaoliang;
        private String junjia;
        private String jvli;
        private String showjl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getImgfm() {
            return imgfm;
        }

        public void setImgfm(String imgfm) {
            this.imgfm = imgfm;
        }

        public String getDpjwd() {
            return dpjwd;
        }

        public void setDpjwd(String dpjwd) {
            this.dpjwd = dpjwd;
        }

        public String getFwl() {
            return fwl;
        }

        public void setFwl(String fwl) {
            this.fwl = fwl;
        }

        public String getXiaoliang() {
            return xiaoliang;
        }

        public void setXiaoliang(String xiaoliang) {
            this.xiaoliang = xiaoliang;
        }

        public String getJunjia() {
            return junjia;
        }

        public void setJunjia(String junjia) {
            this.junjia = junjia;
        }

        public String getJvli() {
            return jvli;
        }

        public void setJvli(String jvli) {
            this.jvli = jvli;
        }

        public String getShowjl() {
            return showjl;
        }

        public void setShowjl(String showjl) {
            this.showjl = showjl;
        }
    }
}
