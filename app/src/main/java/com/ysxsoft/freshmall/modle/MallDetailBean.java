package com.ysxsoft.freshmall.modle;

import java.io.Serializable;
import java.util.List;

public class MallDetailBean implements Serializable{

    /**
     * code : 0
     * data : {"ddxl":0,"dname":"豪享来吃吃2","dpjwd":"114.376038,34.81461","dpjwd_address":"开封市顺河回族区河南大学-南门","fwl":"0","imgfm":"http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png","issc":0,"kdtime":"2019-04-03","leixing":"是打发士大夫","lxphone":"15655485547","pics":"http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg","pingfen":4,"pjlist":[{"dpid":7,"id":1,"phone":"139****3685","pitime":"2019-04-03 16:14:14","pjcontent":"adfadsfsdf","pjpic":["http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","http://oto.sanzhima.cn/uploads/images/20190222/038f8dbf9f271465f778efa5eea74756.jpg","http://oto.sanzhima.cn/uploads/images/20190222/92390857e55f4ab1c82de672c7fc8bb7.jpg"],"pjxj":"4","uidsid":3,"username":"测试萨达"}],"showjl":"85.1km","splist":[{"addtime":"2019-03-29 15:51:08","fmtp":"http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png","gwc":0,"id":2,"istop":"0","kucun":"100","miaoshu":"德国进口猫粮，猫的最爱食品","px":12,"spjiage":369,"spname":"猫粮240kg","ssdpid":7,"xsliang":"20"}],"yytime":"8:00-20:00"}
     * msg : 成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable {
        /**
         * ddxl : 0
         * dname : 豪享来吃吃2
         * dpjwd : 114.376038,34.81461
         * dpjwd_address : 开封市顺河回族区河南大学-南门
         * fwl : 0
         * imgfm : http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png
         * issc : 0
         * kdtime : 2019-04-03
         * leixing : 是打发士大夫
         * lxphone : 15655485547
         * pics : http://oto.sanzhima.cn/uploads/images/20190328/60af739746a57cb877f2f8188fd78f4f.jpg
         * pingfen : 4
         * pjlist : [{"dpid":7,"id":1,"phone":"139****3685","pitime":"2019-04-03 16:14:14","pjcontent":"adfadsfsdf","pjpic":["http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","http://oto.sanzhima.cn/uploads/images/20190222/038f8dbf9f271465f778efa5eea74756.jpg","http://oto.sanzhima.cn/uploads/images/20190222/92390857e55f4ab1c82de672c7fc8bb7.jpg"],"pjxj":"4","uidsid":3,"username":"测试萨达"}]
         * showjl : 85.1km
         * splist : [{"addtime":"2019-03-29 15:51:08","fmtp":"http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png","gwc":0,"id":2,"istop":"0","kucun":"100","miaoshu":"德国进口猫粮，猫的最爱食品","px":12,"spjiage":369,"spname":"猫粮240kg","ssdpid":7,"xsliang":"20"}]
         * yytime : 8:00-20:00
         */

        private String ddxl;
        private String dname;
        private String dpjwd;
        private String dpjwd_address;
        private String fwl;
        private String imgfm;
        private String issc;
        private String kdtime;
        private String leixing;
        private String lxphone;
        private String pics;
        private String pingfen;
        private String showjl;
        private String yytime;
        private List<PjlistBean> pjlist;
        private List<SplistBean> splist;

        public String getDdxl() {
            return ddxl;
        }

        public void setDdxl(String ddxl) {
            this.ddxl = ddxl;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getDpjwd() {
            return dpjwd;
        }

        public void setDpjwd(String dpjwd) {
            this.dpjwd = dpjwd;
        }

        public String getDpjwd_address() {
            return dpjwd_address;
        }

        public void setDpjwd_address(String dpjwd_address) {
            this.dpjwd_address = dpjwd_address;
        }

        public String getFwl() {
            return fwl;
        }

        public void setFwl(String fwl) {
            this.fwl = fwl;
        }

        public String getImgfm() {
            return imgfm;
        }

        public void setImgfm(String imgfm) {
            this.imgfm = imgfm;
        }

        public String getIssc() {
            return issc;
        }

        public void setIssc(String issc) {
            this.issc = issc;
        }

        public String getKdtime() {
            return kdtime;
        }

        public void setKdtime(String kdtime) {
            this.kdtime = kdtime;
        }

        public String getLeixing() {
            return leixing;
        }

        public void setLeixing(String leixing) {
            this.leixing = leixing;
        }

        public String getLxphone() {
            return lxphone;
        }

        public void setLxphone(String lxphone) {
            this.lxphone = lxphone;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getPingfen() {
            return pingfen;
        }

        public void setPingfen(String pingfen) {
            this.pingfen = pingfen;
        }

        public String getShowjl() {
            return showjl;
        }

        public void setShowjl(String showjl) {
            this.showjl = showjl;
        }

        public String getYytime() {
            return yytime;
        }

        public void setYytime(String yytime) {
            this.yytime = yytime;
        }

        public List<PjlistBean> getPjlist() {
            return pjlist;
        }

        public void setPjlist(List<PjlistBean> pjlist) {
            this.pjlist = pjlist;
        }

        public List<SplistBean> getSplist() {
            return splist;
        }

        public void setSplist(List<SplistBean> splist) {
            this.splist = splist;
        }

        public static class PjlistBean {
            /**
             * dpid : 7
             * id : 1
             * phone : 139****3685
             * pitime : 2019-04-03 16:14:14
             * pjcontent : adfadsfsdf
             * pjpic : ["http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","http://oto.sanzhima.cn/uploads/images/20190222/038f8dbf9f271465f778efa5eea74756.jpg","http://oto.sanzhima.cn/uploads/images/20190222/92390857e55f4ab1c82de672c7fc8bb7.jpg"]
             * pjxj : 4
             * uidsid : 3
             * username : 测试萨达
             */

            private String dpid;
            private String id;
            private String phone;
            private String pitime;
            private String pjcontent;
            private String pjxj;
            private String uidsid;
            private String username;
            private List<String> pjpic;
            private String pic;

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getDpid() {
                return dpid;
            }

            public void setDpid(String dpid) {
                this.dpid = dpid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPitime() {
                return pitime;
            }

            public void setPitime(String pitime) {
                this.pitime = pitime;
            }

            public String getPjcontent() {
                return pjcontent;
            }

            public void setPjcontent(String pjcontent) {
                this.pjcontent = pjcontent;
            }

            public String getPjxj() {
                return pjxj;
            }

            public void setPjxj(String pjxj) {
                this.pjxj = pjxj;
            }

            public String getUidsid() {
                return uidsid;
            }

            public void setUidsid(String uidsid) {
                this.uidsid = uidsid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public List<String> getPjpic() {
                return pjpic;
            }

            public void setPjpic(List<String> pjpic) {
                this.pjpic = pjpic;
            }
        }

        public static class SplistBean {
            /**
             * addtime : 2019-03-29 15:51:08
             * fmtp : http://oto.sanzhima.cn/uploads/images/20190328/4a60dfbb24456bbd1e5c6f46843c7d27.png
             * gwc : 0
             * id : 2
             * istop : 0
             * kucun : 100
             * miaoshu : 德国进口猫粮，猫的最爱食品
             * px : 12
             * spjiage : 369
             * spname : 猫粮240kg
             * ssdpid : 7
             * xsliang : 20
             */

            private String addtime;
            private String fmtp;
            private int gwc;
            private String id;
            private String istop;
            private String kucun;
            private String miaoshu;
            private String px;
            private String spjiage;
            private String spname;
            private String ssdpid;
            private String xsliang;

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }

            private int number;

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getFmtp() {
                return fmtp;
            }

            public void setFmtp(String fmtp) {
                this.fmtp = fmtp;
            }

            public int getGwc() {
                return gwc;
            }

            public void setGwc(int gwc) {
                this.gwc = gwc;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIstop() {
                return istop;
            }

            public void setIstop(String istop) {
                this.istop = istop;
            }

            public String getKucun() {
                return kucun;
            }

            public void setKucun(String kucun) {
                this.kucun = kucun;
            }

            public String getMiaoshu() {
                return miaoshu;
            }

            public void setMiaoshu(String miaoshu) {
                this.miaoshu = miaoshu;
            }

            public String getPx() {
                return px;
            }

            public void setPx(String px) {
                this.px = px;
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

            public String getSsdpid() {
                return ssdpid;
            }

            public void setSsdpid(String ssdpid) {
                this.ssdpid = ssdpid;
            }

            public String getXsliang() {
                return xsliang;
            }

            public void setXsliang(String xsliang) {
                this.xsliang = xsliang;
            }
        }
    }
}
