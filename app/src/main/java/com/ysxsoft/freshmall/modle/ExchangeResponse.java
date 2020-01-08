package com.ysxsoft.freshmall.modle;

import java.util.List;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class ExchangeResponse {

    /**
     * code : 200
     * data : {"current_page":1,"data":[{"ddsh":"202001080424561442770","dhkhs":"364655697102525","dhsliang":"1","dhtime":"1578471896","fhtime":"2020-01-08 16:30:55","id":3,"kddhs":"31232232","kdname":"SF","product":[{"ddid":3,"id":2,"spgg":"大包，500kg","spname":"一包粮食","sppic":"http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg"}],"shdzs":"河南省郑州市中原区呵呵红红火火恍恍惚惚","shphone":"13253565026","shrname":"好尴尬","statess":"0","xduid":24}],"last_page":1,"per_page":10,"total":1}
     * mgs : 成功
     */

    private int code;
    private DataBeanX data;
    private String mgs;

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

    public String getMgs() {
        return mgs;
    }

    public void setMgs(String mgs) {
        this.mgs = mgs;
    }

    public static class DataBeanX {
        /**
         * current_page : 1
         * data : [{"ddsh":"202001080424561442770","dhkhs":"364655697102525","dhsliang":"1","dhtime":"1578471896","fhtime":"2020-01-08 16:30:55","id":3,"kddhs":"31232232","kdname":"SF","product":[{"ddid":3,"id":2,"spgg":"大包，500kg","spname":"一包粮食","sppic":"http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg"}],"shdzs":"河南省郑州市中原区呵呵红红火火恍恍惚惚","shphone":"13253565026","shrname":"好尴尬","statess":"0","xduid":24}]
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
             * ddsh : 202001080424561442770
             * dhkhs : 364655697102525
             * dhsliang : 1
             * dhtime : 1578471896
             * fhtime : 2020-01-08 16:30:55
             * id : 3
             * kddhs : 31232232
             * kdname : SF
             * product : [{"ddid":3,"id":2,"spgg":"大包，500kg","spname":"一包粮食","sppic":"http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg"}]
             * shdzs : 河南省郑州市中原区呵呵红红火火恍恍惚惚
             * shphone : 13253565026
             * shrname : 好尴尬
             * statess : 0
             * xduid : 24
             */

            private String ddsh;
            private String dhkhs;
            private String dhsliang;
            private String dhtime;
            private String fhtime;
            private int id;
            private String kddhs;
            private String kdname;
            private String shdzs;
            private String shphone;
            private String shrname;
            private String statess;
            private int xduid;
            private List<ProductBean> product;

            public String getDdsh() {
                return ddsh;
            }

            public void setDdsh(String ddsh) {
                this.ddsh = ddsh;
            }

            public String getDhkhs() {
                return dhkhs;
            }

            public void setDhkhs(String dhkhs) {
                this.dhkhs = dhkhs;
            }

            public String getDhsliang() {
                return dhsliang;
            }

            public void setDhsliang(String dhsliang) {
                this.dhsliang = dhsliang;
            }

            public String getDhtime() {
                return dhtime;
            }

            public void setDhtime(String dhtime) {
                this.dhtime = dhtime;
            }

            public String getFhtime() {
                return fhtime;
            }

            public void setFhtime(String fhtime) {
                this.fhtime = fhtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getKddhs() {
                return kddhs;
            }

            public void setKddhs(String kddhs) {
                this.kddhs = kddhs;
            }

            public String getKdname() {
                return kdname;
            }

            public void setKdname(String kdname) {
                this.kdname = kdname;
            }

            public String getShdzs() {
                return shdzs;
            }

            public void setShdzs(String shdzs) {
                this.shdzs = shdzs;
            }

            public String getShphone() {
                return shphone;
            }

            public void setShphone(String shphone) {
                this.shphone = shphone;
            }

            public String getShrname() {
                return shrname;
            }

            public void setShrname(String shrname) {
                this.shrname = shrname;
            }

            public String getStatess() {
                return statess;
            }

            public void setStatess(String statess) {
                this.statess = statess;
            }

            public int getXduid() {
                return xduid;
            }

            public void setXduid(int xduid) {
                this.xduid = xduid;
            }

            public List<ProductBean> getProduct() {
                return product;
            }

            public void setProduct(List<ProductBean> product) {
                this.product = product;
            }

            public static class ProductBean {
                /**
                 * ddid : 3
                 * id : 2
                 * spgg : 大包，500kg
                 * spname : 一包粮食
                 * sppic : http://o2o.ysxapp.com/uploads/images/20200103/14d3310356e9af1495e489282bc59ab0.jpg
                 */

                private int ddid;
                private int id;
                private String spgg;
                private String spname;
                private String sppic;

                public int getDdid() {
                    return ddid;
                }

                public void setDdid(int ddid) {
                    this.ddid = ddid;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getSpgg() {
                    return spgg;
                }

                public void setSpgg(String spgg) {
                    this.spgg = spgg;
                }

                public String getSpname() {
                    return spname;
                }

                public void setSpname(String spname) {
                    this.spname = spname;
                }

                public String getSppic() {
                    return sppic;
                }

                public void setSppic(String sppic) {
                    this.sppic = sppic;
                }
            }
        }
    }
}
