package com.ysxsoft.freshmall.modle;

import java.util.List;

public class ShopEvaluateBean {


    /**
     * code : 0
     * msg : 获取成功
     * pages : 1
     * data : [{"id":2,"uid":1,"xingji":"4","content":"最新一条评价","pjpic":["http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"],"pjtime":"2019-03-16 09:50:36","sid":1,"guige":"颜色:白色","name":"asdfsd","headpic":"http://192.168.1.163:8888/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"},{"id":1,"uid":1,"xingji":"5","content":"第一条评价","pjpic":["http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"],"pjtime":"2019-03-16 09:50:04","sid":1,"guige":"颜色:白色","name":"asdfsd","headpic":"http://192.168.1.163:8888/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg"}]
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
         * id : 2
         * uid : 1
         * xingji : 4
         * content : 最新一条评价
         * pjpic : ["http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"]
         * pjtime : 2019-03-16 09:50:36
         * sid : 1
         * guige : 颜色:白色
         * name : asdfsd
         * headpic : http://192.168.1.163:8888/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
         */

        private String id;
        private String uid;
        private String xingji;
        private String content;
        private String pjtime;
        private String sid;
        private String guige;
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

        public String getGuige() {
            return guige;
        }

        public void setGuige(String guige) {
            this.guige = guige;
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
