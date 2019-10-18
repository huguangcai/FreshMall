package com.ysxsoft.freshmall.modle;

import java.util.List;

public class OrderListBean {

    /**
     * code : 0
     * msg : 获取成功
     * pages : 6
     * data : [{"id":13,"uid":3,"sh_name":"赵志鹏","sh_address":"河南省郑州市大学科技园","sh_phone":"13937253688","add_time":"2019-03-21 16:58:54","pay_time":null,"end_time":null,"order_num":"201903210458549599777","pay_type":1,"order_status":0,"send_wuliu":null,"exit_wuliu":null,"order_money":null,"goods":[{"id":1,"order_id":13,"sid":3,"guige":"多少","num":2,"name":"发送到","pic":"http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","money":20},{"id":2,"order_id":13,"sid":5,"guige":"大","num":2,"name":"测试商品1","pic":"http://192.168.1.163:8888/uploads/images/20190315/8d744c1e0926b6b718ff4bb775c02589.png","money":110}]}]
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
         * id : 13
         * uid : 3
         * sh_name : 赵志鹏
         * sh_address : 河南省郑州市大学科技园
         * sh_phone : 13937253688
         * add_time : 2019-03-21 16:58:54
         * pay_time : null
         * end_time : null
         * order_num : 201903210458549599777
         * pay_type : 1
         * order_status : 0
         * send_wuliu : null
         * exit_wuliu : null
         * order_money : null
         * goods : [{"id":1,"order_id":13,"sid":3,"guige":"多少","num":2,"name":"发送到","pic":"http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg","money":20},{"id":2,"order_id":13,"sid":5,"guige":"大","num":2,"name":"测试商品1","pic":"http://192.168.1.163:8888/uploads/images/20190315/8d744c1e0926b6b718ff4bb775c02589.png","money":110}]
         */

        private String id;
        private String uid;
        private String sh_name;
        private String sh_address;
        private String sh_phone;
        private String add_time;
        private String pay_time;
        private String end_time;
        private String order_num;
        private int pay_type;
        private int order_status;
        private String send_wuliu;
        private String exit_wuliu;
        private String order_money;
        private List<GoodsBean> goods;

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

        public String getSh_name() {
            return sh_name;
        }

        public void setSh_name(String sh_name) {
            this.sh_name = sh_name;
        }

        public String getSh_address() {
            return sh_address;
        }

        public void setSh_address(String sh_address) {
            this.sh_address = sh_address;
        }

        public String getSh_phone() {
            return sh_phone;
        }

        public void setSh_phone(String sh_phone) {
            this.sh_phone = sh_phone;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public String getSend_wuliu() {
            return send_wuliu;
        }

        public void setSend_wuliu(String send_wuliu) {
            this.send_wuliu = send_wuliu;
        }

        public String getExit_wuliu() {
            return exit_wuliu;
        }

        public void setExit_wuliu(String exit_wuliu) {
            this.exit_wuliu = exit_wuliu;
        }

        public String getOrder_money() {
            return order_money;
        }

        public void setOrder_money(String order_money) {
            this.order_money = order_money;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 1
             * order_id : 13
             * sid : 3
             * guige : 多少
             * num : 2
             * name : 发送到
             * pic : http://192.168.1.163:8888/uploads/images/20190311/2766fbe3980d20c583413b1552d10549.jpg
             * money : 20
             */

            private String id;
            private String order_id;
            private String sid;
            private String guige;
            private String num;
            private String name;
            private String pic;
            private String money;

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            private String pid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
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

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }
        }
    }
}
