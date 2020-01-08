package com.ysxsoft.freshmall.modle;

import java.util.List;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class GoodsListResponse {

    /**
     * code : 200
     * data : {"current_page":1,"data":[{"id":1,"istop":"1","spgg":"大包，500kg","spkc":110,"sppic":"http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg","sptitle":"一包粮食"}],"last_page":1,"per_page":10,"total":1}
     * msg : 成功
     */

    private int code;
    private DataBeanX data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        /**
         * current_page : 1
         * data : [{"id":1,"istop":"1","spgg":"大包，500kg","spkc":110,"sppic":"http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg","sptitle":"一包粮食"}]
         * last_page : 1
         * per_page : 10
         * total : 1
         */

        private int current_page;
        private int last_page;
        private int per_page;
        private int total;
        private List<DataBean> data;

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
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
             * istop : 1
             * spgg : 大包，500kg
             * spkc : 110
             * sppic : http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg
             * sptitle : 一包粮食
             */

            private int id;
            private String istop;
            private String spgg;
            private int spkc;
            private String sppic;
            private String sptitle;
            private boolean isClick;

            public boolean isClick() {
                return isClick;
            }

            public void setClick(boolean click) {
                isClick = click;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIstop() {
                return istop;
            }

            public void setIstop(String istop) {
                this.istop = istop;
            }

            public String getSpgg() {
                return spgg;
            }

            public void setSpgg(String spgg) {
                this.spgg = spgg;
            }

            public int getSpkc() {
                return spkc;
            }

            public void setSpkc(int spkc) {
                this.spkc = spkc;
            }

            public String getSppic() {
                return sppic;
            }

            public void setSppic(String sppic) {
                this.sppic = sppic;
            }

            public String getSptitle() {
                return sptitle;
            }

            public void setSptitle(String sptitle) {
                this.sptitle = sptitle;
            }
        }
    }
}
