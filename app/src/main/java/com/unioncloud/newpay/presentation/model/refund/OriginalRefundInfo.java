package com.unioncloud.newpay.presentation.model.refund;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by cwj on 16/9/9.
 */
public class OriginalRefundInfo implements Parcelable {

    int refundAmount;
    String originalOrderId;
    String originalBillNo;
    String originalSaleDate;  //MMDD

    public String getOriginalSaleDate() {
        return originalSaleDate;
    }

    public void setOriginalSaleDate(String originalSaleDate) {
        this.originalSaleDate = originalSaleDate;
    }

    public OriginalRefundInfo() {
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(String originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public String getOriginalBillNo() {
        return originalBillNo;
    }

    public void setOriginalBillNo(String originalBillNo) {
        this.originalBillNo = originalBillNo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.refundAmount);
        dest.writeString(this.originalOrderId);
        dest.writeString(this.originalBillNo);
        dest.writeString(this.originalSaleDate);
    }

    protected OriginalRefundInfo(Parcel in) {
        this.refundAmount = in.readInt();
        this.originalOrderId = in.readString();
        this.originalBillNo = in.readString();
        this.originalSaleDate = in.readString();
    }

    public static final Creator<OriginalRefundInfo> CREATOR = new Creator<OriginalRefundInfo>() {
        @Override
        public OriginalRefundInfo createFromParcel(Parcel source) {
            return new OriginalRefundInfo(source);
        }

        @Override
        public OriginalRefundInfo[] newArray(int size) {
            return new OriginalRefundInfo[size];
        }
    };
}
