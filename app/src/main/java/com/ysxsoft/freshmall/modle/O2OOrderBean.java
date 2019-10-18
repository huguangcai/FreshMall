package com.ysxsoft.freshmall.modle;

import java.util.List;

public class O2OOrderBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"imgfm":"http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg","dname":"豪享来吃吃吃","ddsates":"2","shuliang":4,"jiner":395,"xxtime":"2019-04-01 17:32:49"}]
     * pages : 1
     */

    private int code;
    private String msg;
    private int pages;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * imgfm : http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg
         * dname : 豪享来吃吃吃
         * ddsates : 2
         * shuliang : 4
         * jiner : 395
         * xxtime : 2019-04-01 17:32:49
         */

        private String id;
        private String imgfm;
        private String dname;
        private String ddsates;
        private String shuliang;
        private String jiner;
        private String xxtime;

        public String getImgfm() {
            return imgfm;
        }

        public void setImgfm(String imgfm) {
            this.imgfm = imgfm;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getDdsates() {
            return ddsates;
        }

        public void setDdsates(String ddsates) {
            this.ddsates = ddsates;
        }

        public String getShuliang() {
            return shuliang;
        }

        public void setShuliang(String shuliang) {
            this.shuliang = shuliang;
        }

        public String getJiner() {
            return jiner;
        }

        public void setJiner(String jiner) {
            this.jiner = jiner;
        }

        public String getXxtime() {
            return xxtime;
        }

        public void setXxtime(String xxtime) {
            this.xxtime = xxtime;
        }
    }
}
