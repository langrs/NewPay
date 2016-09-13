package com.unioncloud.newpay.presentation.model.notice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cwj on 16/9/12.
 */
public class NoticeModel implements Parcelable {

    String title;
    String content;
    String footer;

    public NoticeModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.footer);
    }

    protected NoticeModel(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.footer = in.readString();
    }

    public static final Creator<NoticeModel> CREATOR = new Creator<NoticeModel>() {
        @Override
        public NoticeModel createFromParcel(Parcel source) {
            return new NoticeModel(source);
        }

        @Override
        public NoticeModel[] newArray(int size) {
            return new NoticeModel[size];
        }
    };
}
