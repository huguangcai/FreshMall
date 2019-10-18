package com.ysxsoft.freshmall.modle;

import java.util.List;

public class O2OOederDetailBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"did":6,"dname":"豪享来吃吃吃","yytime":"8:00-20:00","ddsates":"2","splist":[{"spname":"是打发的是","spjiage":386,"spfmtp":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","spshuliang":"4"}],"spcount":1,"ddjiner":395,"showjl":"252.2m","dpjwd_address":"郑州市中原区河南省国家大学科技园(东区)16号楼C座","ddh":"20186565554122","xxtime":"2019-04-01 17:32:49","zftime":"2019-04-02 10:17:47","tktime":"2019-04-02 16:46:11","sytime":"2019-04-02 12:03:34","wctktime":"2019-04-02 17:05:05","zftype":"0","zfewm":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"}
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
         * did : 6
         * dname : 豪享来吃吃吃
         * yytime : 8:00-20:00
         * ddsates : 2
         * splist : [{"spname":"是打发的是","spjiage":386,"spfmtp":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","spshuliang":"4"}]
         * spcount : 1
         * ddjiner : 395
         * showjl : 252.2m
         * dpjwd_address : 郑州市中原区河南省国家大学科技园(东区)16号楼C座
         * ddh : 20186565554122
         * xxtime : 2019-04-01 17:32:49
         * zftime : 2019-04-02 10:17:47
         * tktime : 2019-04-02 16:46:11
         * sytime : 2019-04-02 12:03:34
         * wctktime : 2019-04-02 17:05:05
         * zftype : 0
         * zfewm : http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
         */

        private String did;
        private String dname;

        public String getImgfm() {
            return imgfm;
        }

        public void setImgfm(String imgfm) {
            this.imgfm = imgfm;
        }

        private String imgfm;
        private String yytime;
        private String ddsates;
        private String spcount;
        private String ddjiner;
        private String showjl;
        private String dpjwd_address;
        private String ddh;
        private String xxtime;
        private String zftime;
        private String tktime;
        private String tkyuanyin;

        public String getTkyuanyin() {
            return tkyuanyin;
        }

        public void setTkyuanyin(String tkyuanyin) {
            this.tkyuanyin = tkyuanyin;
        }

        public String getTkshuomign() {
            return tkshuomign;
        }

        public void setTkshuomign(String tkshuomign) {
            this.tkshuomign = tkshuomign;
        }

        private String tkshuomign;

        private String sytime;
        private String wctktime;
        private String zftype;

        public String getYzms() {
            return yzms;
        }

        public void setYzms(String yzms) {
            this.yzms = yzms;
        }

        private String yzms;
        private String zfewm;
        private List<SplistBean> splist;

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

        public String getYytime() {
            return yytime;
        }

        public void setYytime(String yytime) {
            this.yytime = yytime;
        }

        public String getDdsates() {
            return ddsates;
        }

        public void setDdsates(String ddsates) {
            this.ddsates = ddsates;
        }

        public String getSpcount() {
            return spcount;
        }

        public void setSpcount(String spcount) {
            this.spcount = spcount;
        }

        public String getDdjiner() {
            return ddjiner;
        }

        public void setDdjiner(String ddjiner) {
            this.ddjiner = ddjiner;
        }

        public String getShowjl() {
            return showjl;
        }

        public void setShowjl(String showjl) {
            this.showjl = showjl;
        }

        public String getDpjwd_address() {
            return dpjwd_address;
        }

        public void setDpjwd_address(String dpjwd_address) {
            this.dpjwd_address = dpjwd_address;
        }

        public String getDdh() {
            return ddh;
        }

        public void setDdh(String ddh) {
            this.ddh = ddh;
        }

        public String getXxtime() {
            return xxtime;
        }

        public void setXxtime(String xxtime) {
            this.xxtime = xxtime;
        }

        public String getZftime() {
            return zftime;
        }

        public void setZftime(String zftime) {
            this.zftime = zftime;
        }

        public String getTktime() {
            return tktime;
        }

        public void setTktime(String tktime) {
            this.tktime = tktime;
        }

        public String getSytime() {
            return sytime;
        }

        public void setSytime(String sytime) {
            this.sytime = sytime;
        }

        public String getWctktime() {
            return wctktime;
        }

        public void setWctktime(String wctktime) {
            this.wctktime = wctktime;
        }

        public String getZftype() {
            return zftype;
        }

        public void setZftype(String zftype) {
            this.zftype = zftype;
        }

        public String getZfewm() {
            return zfewm;
        }

        public void setZfewm(String zfewm) {
            this.zfewm = zfewm;
        }

        public List<SplistBean> getSplist() {
            return splist;
        }

        public void setSplist(List<SplistBean> splist) {
            this.splist = splist;
        }

        public static class SplistBean {
            /**
             * spname : 是打发的是
             * spjiage : 386
             * spfmtp : http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
             * spshuliang : 4
             */

            private String spname;
            private String spjiage;
            private String spfmtp;
            private String spshuliang;

            public String getSpname() {
                return spname;
            }

            public void setSpname(String spname) {
                this.spname = spname;
            }

            public String getSpjiage() {
                return spjiage;
            }

            public void setSpjiage(String spjiage) {
                this.spjiage = spjiage;
            }

            public String getSpfmtp() {
                return spfmtp;
            }

            public void setSpfmtp(String spfmtp) {
                this.spfmtp = spfmtp;
            }

            public String getSpshuliang() {
                return spshuliang;
            }

            public void setSpshuliang(String spshuliang) {
                this.spshuliang = spshuliang;
            }
        }
    }
}
