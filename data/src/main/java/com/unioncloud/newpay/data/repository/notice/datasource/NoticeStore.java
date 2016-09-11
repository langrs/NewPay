package com.unioncloud.newpay.data.repository.notice.datasource;

import com.unioncloud.newpay.data.entity.notice.NoticeEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/9/8.
 */
public interface NoticeStore {

    Observable<List<NoticeEntity>> queryNotice(String shopId);

}
