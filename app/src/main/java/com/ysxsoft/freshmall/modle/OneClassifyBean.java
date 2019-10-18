package com.ysxsoft.freshmall.modle;

import java.util.List;

public class OneClassifyBean {

    /**
     * code : 0
     * msg : 成功
     * data : [{"id":14,"lid":0,"typename":"酒店住宿","typepic":"http://oto.com/uploads/images/20190313/7c3b19aae1cbba9e570c998d6c60991f.png","addtime":"2019-03-13 15:26:30","px":2},{"id":13,"lid":0,"typename":"美容","typepic":"http://oto.com/uploads/images/20190313/5fc03025fc2e032a7bd15c8cdbc78ad8.png","addtime":"2019-03-13 15:26:16","px":3},{"id":12,"lid":0,"typename":"运动健身","typepic":"http://oto.com/uploads/images/20190313/c727f2fdd9bfe10b08c85acc01e7b73d.png","addtime":"2019-03-13 15:26:01","px":4},{"id":11,"lid":0,"typename":"炸鸡鸭脖","typepic":"http://oto.com/uploads/images/20190313/cea27baa54f38ed924c46cd503f96e23.png","addtime":"2019-03-13 15:25:48","px":5},{"id":6,"lid":0,"typename":"蛋糕奶茶","typepic":"http://oto.com/uploads/images/20190313/7eec61d47368f6b7513c506002a22a3d.png","addtime":"2019-02-28 09:17:59","px":6},{"id":7,"lid":0,"typename":"自助餐","typepic":"http://oto.com/uploads/images/20190313/8f8ce926dc9e8d0ce33226e2822bbab3.png","addtime":"2019-02-28 09:18:21","px":7},{"id":8,"lid":0,"typename":"火锅","typepic":"http://oto.com/uploads/images/20190313/6de05dd4fdd78600e4c76a51731d4637.png","addtime":"2019-02-28 09:18:42","px":8}]
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
         * id : 14
         * lid : 0
         * typename : 酒店住宿
         * typepic : http://oto.com/uploads/images/20190313/7c3b19aae1cbba9e570c998d6c60991f.png
         * addtime : 2019-03-13 15:26:30
         * px : 2
         */

        private String id;
        private String lid;
        private String typename;
        private String typepic;
        private String addtime;
        private String px;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getTypepic() {
            return typepic;
        }

        public void setTypepic(String typepic) {
            this.typepic = typepic;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
            this.px = px;
        }
    }
}
