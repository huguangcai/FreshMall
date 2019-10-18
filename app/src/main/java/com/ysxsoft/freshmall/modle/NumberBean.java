package com.ysxsoft.freshmall.modle;

public class NumberBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"dfkcount":0,"dfhcount":7,"dshcount":0,"dpjcount":7,"shcount":5}
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
         * dfkcount : 0
         * dfhcount : 7
         * dshcount : 0
         * dpjcount : 7
         * shcount : 5
         */

        private String dfkcount;
        private String dfhcount;
        private String dshcount;
        private String dpjcount;
        private String shcount;

        public String getDfkcount() {
            return dfkcount;
        }

        public void setDfkcount(String dfkcount) {
            this.dfkcount = dfkcount;
        }

        public String getDfhcount() {
            return dfhcount;
        }

        public void setDfhcount(String dfhcount) {
            this.dfhcount = dfhcount;
        }

        public String getDshcount() {
            return dshcount;
        }

        public void setDshcount(String dshcount) {
            this.dshcount = dshcount;
        }

        public String getDpjcount() {
            return dpjcount;
        }

        public void setDpjcount(String dpjcount) {
            this.dpjcount = dpjcount;
        }

        public String getShcount() {
            return shcount;
        }

        public void setShcount(String shcount) {
            this.shcount = shcount;
        }
    }
}
