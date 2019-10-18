package com.ysxsoft.freshmall.modle;

import java.util.List;

public class GoodDetailBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"id":1,"spbh":"2018230215021","spname":"大茄子","kucun":"100","pic":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","piclist":["http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"],"jiage":108,"vipjiage":105,"istop":"0","isms":"1","ispzyx":"0","guigename":"颜色","guige":["白色","黑色","红色"],"text":"<p>测试测试a <\/p>","addtime":"2019-03-06 09:51:08","state":"1","types":"1","typeid":"9","px":"69","msmsg":{"msstart":"已结束","sytime":0,"states":3},"pingjia":[{"id":2,"uid":1,"xingji":"4","content":"最新一条评价","pjpic":["http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"],"pjtime":"2019-03-16 09:50:36","sid":1,"name":"asdfsd","headpic":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"}]}
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
         * id : 1
         * spbh : 2018230215021
         * spname : 大茄子
         * kucun : 100
         * pic : http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
         * piclist : ["http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"]
         * jiage : 108
         * vipjiage : 105
         * istop : 0
         * isms : 1
         * ispzyx : 0
         * guigename : 颜色
         * guige : ["白色","黑色","红色"]
         * text : <p>测试测试a </p>
         * addtime : 2019-03-06 09:51:08
         * state : 1
         * types : 1
         * typeid : 9
         * px : 69
         * msmsg : {"msstart":"已结束","sytime":0,"states":3}
         * pingjia : [{"id":2,"uid":1,"xingji":"4","content":"最新一条评价","pjpic":["http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"],"pjtime":"2019-03-16 09:50:36","sid":1,"name":"asdfsd","headpic":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"}]
         */

        private String id;
        private String spbh;
        private String spname;
        private String kucun;
        private String pic;
        private String jiage;
        private String vipjiage;
        private String istop;
        private String isms;
        private String ispzyx;
        private String guigename;
        private String text;
        private String addtime;
        private String state;
        private String types;
        private String typeid;
        private String px;
        private MsmsgBean msmsg;
        private List<String> piclist;
        private List<String> guige;
        private List<PingjiaBean> pingjia;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSpbh() {
            return spbh;
        }

        public void setSpbh(String spbh) {
            this.spbh = spbh;
        }

        public String getSpname() {
            return spname;
        }

        public void setSpname(String spname) {
            this.spname = spname;
        }

        public String getKucun() {
            return kucun;
        }

        public void setKucun(String kucun) {
            this.kucun = kucun;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getJiage() {
            return jiage;
        }

        public void setJiage(String jiage) {
            this.jiage = jiage;
        }

        public String getVipjiage() {
            return vipjiage;
        }

        public void setVipjiage(String vipjiage) {
            this.vipjiage = vipjiage;
        }

        public String getIstop() {
            return istop;
        }

        public void setIstop(String istop) {
            this.istop = istop;
        }

        public String getIsms() {
            return isms;
        }

        public void setIsms(String isms) {
            this.isms = isms;
        }

        public String getIspzyx() {
            return ispzyx;
        }

        public void setIspzyx(String ispzyx) {
            this.ispzyx = ispzyx;
        }

        public String getGuigename() {
            return guigename;
        }

        public void setGuigename(String guigename) {
            this.guigename = guigename;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
            this.px = px;
        }

        public MsmsgBean getMsmsg() {
            return msmsg;
        }

        public void setMsmsg(MsmsgBean msmsg) {
            this.msmsg = msmsg;
        }

        public List<String> getPiclist() {
            return piclist;
        }

        public void setPiclist(List<String> piclist) {
            this.piclist = piclist;
        }

        public List<String> getGuige() {
            return guige;
        }

        public void setGuige(List<String> guige) {
            this.guige = guige;
        }

        public List<PingjiaBean> getPingjia() {
            return pingjia;
        }

        public void setPingjia(List<PingjiaBean> pingjia) {
            this.pingjia = pingjia;
        }

        public static class MsmsgBean {
            /**
             * msstart : 已结束
             * sytime : 0
             * states : 3
             */

            private String msstart;
            private String sytime;
            private String states;

            public String getMsstart() {
                return msstart;
            }

            public void setMsstart(String msstart) {
                this.msstart = msstart;
            }

            public String getSytime() {
                return sytime;
            }

            public void setSytime(String sytime) {
                this.sytime = sytime;
            }

            public String getStates() {
                return states;
            }

            public void setStates(String states) {
                this.states = states;
            }
        }

        public static class PingjiaBean {
            /**
             * id : 2
             * uid : 1
             * xingji : 4
             * content : 最新一条评价
             * pjpic : ["http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"]
             * pjtime : 2019-03-16 09:50:36
             * sid : 1
             * name : asdfsd
             * headpic : http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
             */

            private String id;
            private String uid;
            private String xingji;
            private String content;
            private String pjtime;
            private String sid;
            private String name;
            private String headpic;
            private List<String> pjpic;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getXingji() {
                return xingji;
            }

            public void setXingji(String xingji) {
                this.xingji = xingji;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPjtime() {
                return pjtime;
            }

            public void setPjtime(String pjtime) {
                this.pjtime = pjtime;
            }

            public String getSid() {
                return sid;
            }

            public void setSid(String sid) {
                this.sid = sid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHeadpic() {
                return headpic;
            }

            public void setHeadpic(String headpic) {
                this.headpic = headpic;
            }

            public List<String> getPjpic() {
                return pjpic;
            }

            public void setPjpic(List<String> pjpic) {
                this.pjpic = pjpic;
            }
        }
    }
}
