package com.unioncloud.newpay.domain.model.thirdparty;

/**
 * Created by cwj on 16/8/11.
 */
public enum RefundState {
    SUCCESS("SUCCESS", "退款成功"),
    FAIL("FAIL", "退款失败"),
    PROCESSING("PROCESSING", "退款处理中"),
    NOTSURE("NOTSURE", "未确定,需原退款单号重新发起"),
    CHANGE("CHANGE", "转入代发");

    String message;
    String tag;

    RefundState(String tag, String message) {
        this.tag = tag;
        this.message = message;
    }

    public static RefundState getRefundStateByTag(String tag) {
        if (tag == null || "".equals(tag)) {
            return null;
        }
        if (SUCCESS.tag.equals(tag)) {
            return SUCCESS;
        } else if (FAIL.tag.equals(tag)) {
            return FAIL;
        } else if (PROCESSING.tag.equals(tag)) {
            return PROCESSING;
        } else if (NOTSURE.tag.equals(tag)) {
            return NOTSURE;
        } else if (CHANGE.tag.equals(tag)) {
            return CHANGE;
        }
        return null;
    }
}
