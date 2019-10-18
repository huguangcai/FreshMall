package com.ysxsoft.freshmall.modle;


import java.io.Serializable;
import java.util.List;

public class PackageDetailBean implements Serializable {

    private String sum;
    private List<DetailData> dataList;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public List<DetailData> getDataList() {
        return dataList;
    }

    public void setDataList(List<DetailData> dataList) {
        this.dataList = dataList;
    }


    public static class DetailData implements Serializable {

        private String shopCarId;
        private String id;
        private String url;
        private String content;
        private String guige;
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShopCarId() {
            return shopCarId;
        }

        public void setShopCarId(String shopCarId) {
            this.shopCarId = shopCarId;
        }

        public String getGoodsId() {
            return id;
        }

        public void setGoodsId(String goodsId) {
            this.id = goodsId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGuige() {
            return guige;
        }

        public void setGuige(String guige) {
            this.guige = guige;
        }

        public String getNumber() {
            return num;
        }

        public void setNumber(String number) {
            this.num = number;
        }

        private String num;
    }
}
