package com.unioncloud.newpay.data.repository.notice.datasource;

import com.unioncloud.newpay.data.entity.notice.NoticeEntity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/8.
 */
public class CloudNoticeStore implements NoticeStore {



    @Override
    public Observable<List<NoticeEntity>> queryNotice(String shopId) {
        return Observable.create(new Observable.OnSubscribe<List<NoticeEntity>>() {
            @Override
            public void call(Subscriber<? super List<NoticeEntity>> subscriber) {

            }
        }).flatMap(new Func1<List<NoticeEntity>, Observable<NoticeEntity>>() {
            @Override
            public Observable<NoticeEntity> call(List<NoticeEntity> entityList) {
                return Observable.from(entityList);
            }
        }).filter(new Func1<NoticeEntity, Boolean>() {
            @Override
            public Boolean call(NoticeEntity entityList) {
                return null;
            }
        }).toList();
    }


}
