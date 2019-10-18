package com.ysxsoft.freshmall.modle;

import java.util.List;

public class GetTypeInfoBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":1,"lid":0,"typename":"大鱼","typepic":"http://192.168.1.163:8888/uploads/images/20190225/5937ccb7f541970d8f7c59cf75cdab83.png","addtime":"2019-02-25 14:46:51","px":1},{"id":2,"lid":0,"typename":"大肉","typepic":"http://192.168.1.163:8888/uploads/images/20190225/0445ca1d0e61f9f1679f57863dcde728.png","addtime":"2019-02-25 14:47:07","px":36},{"id":3,"lid":0,"typename":"大虾","typepic":"http://192.168.1.163:8888/uploads/images/20190225/57d3b91dbd1f88451db7dc088fcfccf7.png","addtime":"2019-02-25 14:52:05","px":5}]
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
         * id : 1
         * lid : 0
         * typename : 大鱼
         * typepic : http://192.168.1.163:8888/uploads/images/20190225/5937ccb7f541970d8f7c59cf75cdab83.png
         * addtime : 2019-02-25 14:46:51
         * px : 1
         */

        private int id;
        private int lid;
        private String typename;
        private String typepic;
        private String addtime;
        private int px;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLid() {
            return lid;
        }

        public void setLid(int lid) {
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

        public int getPx() {
            return px;
        }

        public void setPx(int px) {
            this.px = px;
        }
    }
}
