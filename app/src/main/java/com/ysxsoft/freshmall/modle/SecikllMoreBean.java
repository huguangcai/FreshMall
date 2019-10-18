package com.ysxsoft.freshmall.modle;

import java.util.List;

public class SecikllMoreBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"msstart":"已结束","sytime":0,"states":3,"splist":[{"id":5,"spname":"测试商品1","kucun":"111","jiage":112,"vipjiage":110,"pic":"http://oto.sanzhima.cn/uploads/images/20190315/8d744c1e0926b6b718ff4bb775c02589.png"}],"pages":11}
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
         * msstart : 已结束
         * sytime : 0
         * states : 3
         * splist : [{"id":5,"spname":"测试商品1","kucun":"111","jiage":112,"vipjiage":110,"pic":"http://oto.sanzhima.cn/uploads/images/20190315/8d744c1e0926b6b718ff4bb775c02589.png"}]
         * pages : 11
         */

        private String msstart;
        private String sytime;
        private String states;
        private int pages;
        private List<SplistBean> splist;

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

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<SplistBean> getSplist() {
            return splist;
        }

        public void setSplist(List<SplistBean> splist) {
            this.splist = splist;
        }

        public static class SplistBean {
            /**
             * id : 5
             * spname : 测试商品1
             * kucun : 111
             * jiage : 112
             * vipjiage : 110
             * pic : http://oto.sanzhima.cn/uploads/images/20190315/8d744c1e0926b6b718ff4bb775c02589.png
             */

            private String id;
            private String spname;
            private String kucun;
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

            public String getKucun() {
                return kucun;
            }

            public void setKucun(String kucun) {
                this.kucun = kucun;
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
}
