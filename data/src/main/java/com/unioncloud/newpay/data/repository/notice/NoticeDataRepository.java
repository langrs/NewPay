package com.unioncloud.newpay.data.repository.notice;

import android.content.Context;

import com.unioncloud.newpay.data.entity.notice.NoticeEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.domain.repository.NoticeRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/8.
 */
public class NoticeDataRepository implements NoticeRepository {

    Context context;

    public NoticeDataRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Notice>> queryAllNotice(String shopId) {
        return StoreFactory.getNoticeStore(context).queryAllNotice(shopId)
                .flatMap(new Func1<List<NoticeEntity>, Observable<NoticeEntity>>() {
                    @Override
                    public Observable<NoticeEntity> call(List<NoticeEntity> entityList) {
                        return Observable.from(entityList);
                    }
                }).map(new Func1<NoticeEntity, Notice>() {
                    @Override
                    public Notice call(NoticeEntity entity) {
                        if (entity == null) {
                            return null;
                        }
                        Notice notice = new Notice();
                        notice.setId(entity.getId());
                        notice.setValidityDate(entity.getValidDate());
                        notice.setTitle(entity.getTitle());
                        notice.setContent(entity.getContent());
                        return notice;
                    }
                }).toList();
    }

    @Override
    public Observable<List<Notice>> queryNewNotice(String shopId) {
        return StoreFactory.getNoticeStore(context).queryNewNotice(shopId)
                .flatMap(new Func1<List<NoticeEntity>, Observable<NoticeEntity>>() {
                    @Override
                    public Observable<NoticeEntity> call(List<NoticeEntity> entityList) {
                        return Observable.from(entityList);
                    }
                }).map(new Func1<NoticeEntity, Notice>() {
                    @Override
                    public Notice call(NoticeEntity entity) {
                        if (entity == null) {
                            return null;
                        }
                        Notice notice = new Notice();
                        notice.setId(entity.getId());
                        notice.setValidityDate(entity.getValidDate());
                        notice.setTitle(entity.getTitle());
                        notice.setContent(entity.getContent());
                        return notice;
                    }
                }).toList();
    }
}
