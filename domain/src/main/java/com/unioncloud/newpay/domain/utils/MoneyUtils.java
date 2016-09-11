package com.unioncloud.newpay.domain.utils;

import java.math.BigDecimal;

/**
 * Created by cwj on 16/8/15.
 */
public class MoneyUtils {

    public static int getFen(String amount) {
        if (amount == null || "".equals(amount)) {
            return 0;
        }
        BigDecimal bigDecimal = DecimalUtils.getHalfUp(2, amount);
        return bigDecimal.movePointRight(2).intValue();
    }

    public static String fenToString(int fen) {
        BigDecimal bigDecimal = BigDecimal.valueOf(fen);
        return String.format("%.2f", bigDecimal.movePointLeft(2).floatValue());
    }

}
