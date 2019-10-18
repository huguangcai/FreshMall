package com.ysxsoft.freshmall.modle;

import java.util.List;

public class HomelistBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"spid":1,"title":"美如爆款","spname":"大茄子","jiage":108,"vipjiage":105,"fmtp":"http://oto.sanzhima.cn/uploads/images/20190225/57d3b91dbd1f88451db7dc088fcfccf7.png"},{"spid":3,"title":"限时抢购","spname":"发送到","jiage":200,"vipjiage":191.35,"fmtp":"http://oto.sanzhima.cn/uploads/images/20190225/6de9686cd4dec2ba7813820aff8392ec.png"},{"spid":5,"title":"优品特卖","spname":"测试商品1","jiage":112,"vipjiage":110,"fmtp":"http://oto.sanzhima.cn/uploads/images/20190228/ea87ad854c64b02c54c416f19e6fc46b.png"}]
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
         * spid : 1
         * title : 美如爆款
         * spname : 大茄子
         * jiage : 108
         * vipjiage : 105
         * fmtp : http://oto.sanzhima.cn/uploads/images/20190225/57d3b91dbd1f88451db7dc088fcfccf7.png
         */

        private String spid;
        private String title;
        private String spname;
        private String jiage;
        private String vipjiage;
        private String fmtp;

        public String getSpid() {
            return spid;
        }

        public void setSpid(String spid) {
            this.spid = spid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSpname() {
            return spname;
        }

        public void setSpname(String spname) {
            this.spname = spname;
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

        public String getFmtp() {
            return fmtp;
        }

        public void setFmtp(String fmtp) {
            this.fmtp = fmtp;
        }
    }
}
