package com.unioncloud.newpay.domain.model.erp;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryCardCommand {

    String trackInfo;       // 磁道信息
    String billNo;          // 关联号码(手机号/证件号/二维码)
    String shopId;          // 销售门店

    public String getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(String trackInfo) {
        this.trackInfo = trackInfo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
