package com.ysxsoft.freshmall.modle;

import java.util.List;

public class OrderDetailBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"id":78,"uid":4,"sh_name":"黑芝麻胡","sh_address":"河南省郑州市中原区河南大学科技园16号楼C座","sh_phone":"13253565026","add_time":"2019-04-13 18:01:50","pay_time":null,"end_time":null,"order_num":"201904130601500566621","pay_type":1,"order_status":6,"send_wuliu":"邮政EMS/1412412415364756i7","order_money":105,"exit_wuliu":"","exit_time":"2019-04-13 18:03:09","exit_title":"a手动阀","exit_content":"答复","exit_type":1,"exit_pics":["http://oto.sanzhima.cn/uploads/apifile/20190413/adfa929b59db6bfdef337ed0c07b60c1.jpg","http://oto.sanzhima.cn/uploads/apifile/20190413/035051899a05ff91dd07576e19935b75.jpg","http://oto.sanzhima.cn/uploads/apifile/20190413/4323b7e6c5d87aaa1fcc11b3ffaeec12.jpg"],"exit_num":"201904130601500566621","goods":[{"id":140,"order_id":78,"sid":1,"guige":"颜色:红色","num":1,"name":"大茄子","pic":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","money":105,"pid":0}]}
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
         * id : 78
         * uid : 4
         * sh_name : 黑芝麻胡
         * sh_address : 河南省郑州市中原区河南大学科技园16号楼C座
         * sh_phone : 13253565026
         * add_time : 2019-04-13 18:01:50
         * pay_time : null
         * end_time : null
         * order_num : 201904130601500566621
         * pay_type : 1
         * order_status : 6
         * send_wuliu : 邮政EMS/1412412415364756i7
         * order_money : 105
         * exit_wuliu :
         * exit_time : 2019-04-13 18:03:09
         * exit_title : a手动阀
         * exit_content : 答复
         * exit_type : 1
         * exit_pics : ["http://oto.sanzhima.cn/uploads/apifile/20190413/adfa929b59db6bfdef337ed0c07b60c1.jpg","http://oto.sanzhima.cn/uploads/apifile/20190413/035051899a05ff91dd07576e19935b75.jpg","http://oto.sanzhima.cn/uploads/apifile/20190413/4323b7e6c5d87aaa1fcc11b3ffaeec12.jpg"]
         * exit_num : 201904130601500566621
         * goods : [{"id":140,"order_id":78,"sid":1,"guige":"颜色:红色","num":1,"name":"大茄子","pic":"http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","money":105,"pid":0}]
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
        private String pay_type;
        private String order_status;
        private String send_wuliu;
        private String order_money;
        private String exit_wuliu;
        private String exit_time;
        private String exit_title;
        private String exit_content;
        private String exit_type;
        private String exit_num;
        private List<String> exit_pics;
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

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public String getSend_wuliu() {
            return send_wuliu;
        }

        public void setSend_wuliu(String send_wuliu) {
            this.send_wuliu = send_wuliu;
        }

        public String getOrder_money() {
            return order_money;
        }

        public void setOrder_money(String order_money) {
            this.order_money = order_money;
        }

        public String getExit_wuliu() {
            return exit_wuliu;
        }

        public void setExit_wuliu(String exit_wuliu) {
            this.exit_wuliu = exit_wuliu;
        }

        public String getExit_time() {
            return exit_time;
        }

        public void setExit_time(String exit_time) {
            this.exit_time = exit_time;
        }

        public String getExit_title() {
            return exit_title;
        }

        public void setExit_title(String exit_title) {
            this.exit_title = exit_title;
        }

        public String getExit_content() {
            return exit_content;
        }

        public void setExit_content(String exit_content) {
            this.exit_content = exit_content;
        }

        public String getExit_type() {
            return exit_type;
        }

        public void setExit_type(String exit_type) {
            this.exit_type = exit_type;
        }

        public String getExit_num() {
            return exit_num;
        }

        public void setExit_num(String exit_num) {
            this.exit_num = exit_num;
        }

        public List<String> getExit_pics() {
            return exit_pics;
        }

        public void setExit_pics(List<String> exit_pics) {
            this.exit_pics = exit_pics;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 140
             * order_id : 78
             * sid : 1
             * guige : 颜色:红色
             * num : 1
             * name : 大茄子
             * pic : http://oto.sanzhima.cn/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
             * money : 105
             * pid : 0
             */

            private String id;
            private String order_id;
            private String sid;
            private String guige;
            private String num;
            private String name;
            private String pic;
            private String money;
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

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }
        }
    }
}
