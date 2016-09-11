package com.unioncloud.newpay.domain.model.appupgrade;

import java.io.File;

/**
 * Created by cwj on 16/8/20.
 */
public class DownloadInfo {
    private long current;   // 下载进度1-100
    private long total;
    private File path;    // 下载文件存放地址

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
