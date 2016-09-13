package com.unioncloud.newpay.data.entity.notice;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cwj on 16/9/12.
 */
@DatabaseTable(tableName = "hs_notice")
public class NoticeEntity {

    @DatabaseField(id = true)
    private String id;          //1
    @DatabaseField(columnName = "notice_content")
    @SerializedName("memo")
    private String content;        //2016.9.12号下午进行培训
    @DatabaseField(columnName = "notice_no")
    private String noticeNo;    //0001
    @DatabaseField(columnName = "notice_title")
    private String title;       //培训通知
    private String validDate;   //2016-09-12 00:00:00

    private String isFlag;      //1
    private String updateBy;    //1
    private String updateTime;  //2016-09-16 00:00:00
    private String createBy;    //1
    private String createDate;  //2016-09-12 10:49:41
    private String createOrgId; //
    private String createTime;  //2016-09-12 00:00:00
    private String dialog;      //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
