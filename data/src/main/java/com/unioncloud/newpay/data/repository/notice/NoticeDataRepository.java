package com.unioncloud.newpay.data.repository.notice;

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

    @Override
    public Observable<List<Notice>> queryNotice(String shopId) {
        return StoreFactory.getNoticeStore().queryNotice(shopId)
                .map(new Func1<List<NoticeEntity>, List<Notice>>() {
                    @Override
                    public List<Notice> call(List<NoticeEntity> entityList) {
                        return null;
                    }
                });
    }

    private static Notice mapper(NoticeEntity entity) {
        if (entity == null) {
            return null;
        }
        Notice notice = new Notice();
        notice.setId(notice.getId());
        notice.setValidityDate(notice.getValidityDate());
        notice.setTitle(notice.getTitle());
        notice.setContent(notice.getContent());
        notice.setFooter(notice.getFooter());
        return notice;
    }
}
