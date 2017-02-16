package com.unioncloud.pax;

class PaxPreconditions {

    public static void checkAmount(String amount) throws PaxPayException {
        if ((amount == null) || (amount.length() == 0)) {
            throw new PaxPayException(R.string.Pax_Exception_EmptyAmount);
        }
        if (amount.length() > 12) {
            throw new PaxPayException(R.string.Pax_Exception_InvalidAmount);
        }
    }

    public static void checkVoucherNumber(String voucherNumber) throws PaxPayException {
        if ((voucherNumber == null) || (voucherNumber.length() == 0)) {
            throw new PaxPayException(R.string.Pax_Exception_EmptyVoucherNumber);
        }
        if (voucherNumber.length() > 6) {
            throw new PaxPayException(R.string.Pax_Exception_InvalidVoucherNumber);
        }
    }

    public static void checkReferenceNumber(String refNumber) throws PaxPayException {
        if ((refNumber == null) || (refNumber.length() == 0)) {
            throw new PaxPayException(R.string.Pax_Exception_EmptyReference);
        }
        if (refNumber.length() > refNumber.length()) {
            throw new PaxPayException(R.string.Pax_Exception_InvalidReference);
        }
    }

    public static void checkTransDate(String date) throws PaxPayException {
        if ((date == null) || (date.length() != 4)) {
            throw new PaxPayException(R.string.Pax_Exception_InvalidDate);
        }
    }
}
