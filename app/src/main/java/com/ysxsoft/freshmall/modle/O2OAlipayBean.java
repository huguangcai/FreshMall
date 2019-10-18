package com.ysxsoft.freshmall.modle;

/**
 * Create By 胡
 * on 2019/7/9 0009
 */
public class O2OAlipayBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : alipay_sdk=alipay-sdk-php-20180705&app_id=2018121262523476&biz_content=%7B%22body%22%3A%22APP%E5%95%86%E5%93%81%E8%B4%AD%E4%B9%B0%E6%94%AF%E4%BB%98%22%2C%22subject%22%3A+%22%E5%95%86%E5%93%81%E8%B4%AD%E4%B9%B0%22%2C%22out_trade_no%22%3A+%22201903221044339763640%22%2C%22timeout_express%22%3A+%2230m%22%2C%22total_amount%22%3A+%22603%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=https%3A%2F%2Fwww.ydjapp.com%2Findex.php%2Fapi%2Fzfb%2Fnotify_url&sign_type=RSA2×tamp=2019-03-22+10%3A44%3A34&version=1.0&sign=Ut5ozycFX2vZjtJM50Nv20PwozNrU%2Fkgz67nc0zuAq%2BiVr%2Ba3bNKqFHNvrTeWGM12KxpCieD7ylsJd4ulQDilF1rXDUEOkY5GUYuQ1wzb%2F9P14DdX%2BSjmJBsHHkIkX9rrdg0va0MYin7dkLf0abOa99rcb5SiiKWnDHB6fxA9vKjxZlgdnHtqBJpWhR2003C7CZgIUMMqaftfkgt%2Fb5aGsbkp4D%2B85sP5kWl%2BqGRN6bQhHa3eZwL9OtlNd4crOaGjI7vTt%2FpsnT6yZ6qRRSFhgiQDzHef7w1dY6WWWsuCet60u0Imj5ZbJXKd8Lerdc%2FSPFKU2sZztifihKNOrNPrQ%3D%3D
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
