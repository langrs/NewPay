package com.unioncloud.newpay.domain.model.thirdparty;

/**
 * Created by cwj on 16/8/11.
 */
public enum TradeState {
    SUCCESS("SUCCESS", "支付成功"),
    REFUND("REFUND", "转入退款"),
    NOTPAY("NOTPAY", "未支付"),
    CLOSED("CLOSED", "已关闭"),
    REVOKED("REVOKED", "已冲正"),
    PAYERROR("PAYERROR", "支付失败");

    String tag;
    String message;

    TradeState(String tag, String message) {
        this.tag = tag;
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public static TradeState getTradeStateByTag(String tag) {
        if (SUCCESS.tag.equals(tag)){
            return SUCCESS;
        } else if(REFUND.tag.equals(tag)) {
            return REFUND;
        } else if (NOTPAY.tag.equals(tag)) {
            return NOTPAY;
        } else if (CLOSED.tag.equals(tag)) {
            return CLOSED;
        } else if (REVOKED.tag.equals(tag)) {
            return REVOKED;
        } else if (PAYERROR.tag.equals(tag)) {
            return PAYERROR;
        }
        return null;
    }

}
