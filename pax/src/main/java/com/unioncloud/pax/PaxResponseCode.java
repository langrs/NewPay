package com.unioncloud.pax;

import android.support.annotation.StringRes;

/**
 * 百富应答码. 百富API返回的公共结果参数rspCode, 可以用来匹配对应的提示信息
 */
enum PaxResponseCode {
    SUCCESS("0", R.string.PaxResp_Success),
    TIMEOUT("-1", R.string.PaxResp_Timeout),
    CONNECT_TIMEOUT("-2", R.string.PaxResp_ConnectTimeout),
    SEND_FAIL("-3", R.string.PaxResp_SendFail),
    RECEIVE_FAIL("-4", R.string.PaxResp_ReceiveFail),
    PACKAGE_ENCODE_ERROR("-5", R.string.PaxResp_PackageEncodeError),
    PACKAGE_DECODE_ERROR("-6", R.string.PaxResp_PackageDecodeError),
    PACKAGE_Invalid("-7", R.string.PaxResp_PackageInvalid),
    MAC_ERROR("-8", R.string.PaxResp_MacError),
    PROCESS_ERROR("-9", R.string.PaxResp_ProcessCodeNotMatch),
    MESSAGE_TYPE_NOT_MATCH("-10", R.string.PaxResp_MessageTypeNotMatch),
    AMOUNT_NOT_MATCH("-11", R.string.PaxResp_AmountNotMatch),
    VOUCHER_NUMBER_NOT_MATCH("-12", R.string.PaxResp_VoucherNumberNotMatch),
    TERMINAL_NUMBER_NOT_MATCH("-13", R.string.PaxResp_TerminalNumberMatch),
    MERCHAT_NUMBER_NOT_MATCH("-14", R.string.PaxResp_MerchantNumberNotMatch),
    NOT_FOUND_TRANSACTION("-15", R.string.PaxResp_NotFoundTransaction),
    NOT_FOUND_ORIGINAL_TRANSACTION("-16", R.string.PaxResp_NotFoundOriginalTransaction),
    TRANSACTION_IS_VOIDED("-17", R.string.PaxResp_TransactionIsVoided),
    TRANSACTION_CANNOT_VOID("-18", R.string.PaxResp_TransactionCannotVoid),
    OPEN_COM_PORT_ERROR("-19", R.string.PaxResp_OpenComPortError),
    FORBIDDEN("-20", R.string.PaxResp_Forbidden),
    STOP("21", R.string.PaxResp_Stop),
    NOT_LOGIN("22", R.string.PaxResp_NotLogin),
    NUMBER_LIMIT_AND_SETTLE_NOW("23", R.string.PaxResp_NumberLimitAndSettleNow),
    NUMBER_LIMIT_AND_SETTLE_LATER("24", R.string.PaxResp_NumberLimitAndSettleLater),
    NOT_ENOUGH_STORAGE("25", R.string.PaxResp_NotEnoughStorage),
    NOT_SUPPORT("26", R.string.PaxResp_NotSupport);

    public String code;
    @StringRes
    public int description;

    PaxResponseCode(String code, int description) {
        this.code = code;
        this.description = description;
    }

    public static PaxResponseCode matchByCode(String code) {
        switch (code) {
            case "0":
                return SUCCESS;
            case "-1":
                return TIMEOUT;
            case "-2":
                return CONNECT_TIMEOUT;
            case "-3":
                return SEND_FAIL;
            case "-4":
                return RECEIVE_FAIL;
            case "-5":
                return PACKAGE_ENCODE_ERROR;
            case "-6":
                return PACKAGE_DECODE_ERROR;
            case "-7":
                return PACKAGE_Invalid;
            case "-8":
                return MAC_ERROR;
            case "-9":
                return PROCESS_ERROR;
            case "-10":
                return MESSAGE_TYPE_NOT_MATCH;
            case "-11":
                return AMOUNT_NOT_MATCH;
            case "-12":
                return VOUCHER_NUMBER_NOT_MATCH;
            case "-13":
                return TERMINAL_NUMBER_NOT_MATCH;
            case "-14":
                return MERCHAT_NUMBER_NOT_MATCH;
            case "-15":
                return NOT_FOUND_TRANSACTION;
            case "-16":
                return NOT_FOUND_ORIGINAL_TRANSACTION;
            case "-17":
                return TRANSACTION_IS_VOIDED;
            case "-18":
                return TRANSACTION_CANNOT_VOID;
            case "-19":
                return OPEN_COM_PORT_ERROR;
            case "-20":
                return FORBIDDEN;
            case "-21":
                return STOP;
            case "-22":
                return NOT_LOGIN;
            case "-23":
                return NUMBER_LIMIT_AND_SETTLE_NOW;
            case "-24":
                return NUMBER_LIMIT_AND_SETTLE_LATER;
            case "-25":
                return NOT_ENOUGH_STORAGE;
            case "-26":
                return NOT_SUPPORT;
            default:
                throw new IllegalArgumentException("unknown response code: " + code);
        }
    }
}
