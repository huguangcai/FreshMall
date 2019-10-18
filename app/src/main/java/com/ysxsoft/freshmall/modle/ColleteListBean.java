package com.ysxsoft.freshmall.modle;

import java.util.List;

public class ColleteListBean {


    /**
     * code : 0
     * msg : 成功
     * data : [{"did":6,"dname":"豪享来吃吃吃","imgfm":"http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg","ddxl":7,"ddjj":25.5,"jvli":252.2,"showjl":"252.2m"}]
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
         * did : 6
         * dname : 豪享来吃吃吃
         * imgfm : http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg
         * ddxl : 7
         * ddjj : 25.5
         * jvli : 252.2
         * showjl : 252.2m
         */

        private String did;
        private String dname;
        private String imgfm;
        private String ddxl;
        private String ddjj;
        private String jvli;
        private String showjl;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
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

        public String getDdxl() {
            return ddxl;
        }

        public void setDdxl(String ddxl) {
            this.ddxl = ddxl;
        }

        public String getDdjj() {
            return ddjj;
        }

        public void setDdjj(String ddjj) {
            this.ddjj = ddjj;
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
