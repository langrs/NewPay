package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.notice.Notice;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/9/8.
 */
public interface NoticeRepository {

    Observable<List<Notice>> queryAllNotice(String shopId);

    Observable<List<Notice>> queryNewNotice(String shopId);
}
