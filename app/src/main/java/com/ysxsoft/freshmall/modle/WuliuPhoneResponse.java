package com.ysxsoft.freshmall.modle;

/**
 * Create By 胡
 * on 2020/1/10 0010
 */
public class WuliuPhoneResponse {

    /**
     * code : 200
     * msg : 成功
     * data : 400-123546
     */

    private int code;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
