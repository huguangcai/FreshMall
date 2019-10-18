package com.ysxsoft.freshmall.modle;

import java.util.List;

public class TypeShopListBean {

    /**
     * code : 0
     * msg : 获取成功
     * pages : 1
     * data : [{"id":3,"spbh":"2019031155999","spname":"发送到","kucun":"100","pic":"http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","piclist":["http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"],"jiage":20,"vipjiage":20,"istop":"0","isms":"0","ispzyx":"0","guigename":"啊啊","guige":["多少","阿斯蒂芬"],"text":"<p>大多数<\/p>","addtime":"2019-03-11 18:59:35","state":"1","types":"0","typeid":"4","px":"20"}]
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
        /**
         * id : 3
         * spbh : 2019031155999
         * spname : 发送到
         * kucun : 100
         * pic : http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg
         * piclist : ["http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"]
         * jiage : 20
         * vipjiage : 20
         * istop : 0
         * isms : 0
         * ispzyx : 0
         * guigename : 啊啊
         * guige : ["多少","阿斯蒂芬"]
         * text : <p>大多数</p>
         * addtime : 2019-03-11 18:59:35
         * state : 1
         * types : 0
         * typeid : 4
         * px : 20
         */

        private int id;
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
        private List<String> piclist;
        private List<String> guige;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
    }
}
