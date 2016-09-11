package com.unioncloud.newpay.domain.model.notice;

import java.io.Serializable;

/**
 * Created by cwj on 16/9/8.
 */
public class Notice implements Serializable {

    String id;              // ID
    String validityDate;    // 有效时间

    String title;
    String content;
    String footer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
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
}
