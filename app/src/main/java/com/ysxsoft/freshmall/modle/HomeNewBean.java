package com.ysxsoft.freshmall.modle;

import java.util.List;

public class HomeNewBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":3,"spname":"发送到","jiage":200,"vipjiage":191.35,"pic":"http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg"}]
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
         * id : 3
         * spname : 发送到
         * jiage : 200
         * vipjiage : 191.35
         * pic : http://oto.sanzhima.cn/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg
         */

        private String id;
        private String spname;
        private String jiage;
        private String vipjiage;
        private String pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
