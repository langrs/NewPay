package com.unioncloud.newpay.presentation.presenter.notice;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.notice.QueryNewNoticeInteractor;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/12.
 */
public class QueryNewNoticeHandler extends UpdateHandler<List<Notice>, QueryNewNoticeHandler>
implements Runnable {

    private String shopId;

    public QueryNewNoticeHandler(String shopId) {
        super(null, true);
    }

    @Override
    public void run() {
        QueryNewNoticeInteractor interactor = new QueryNewNoticeInteractor(
                PresenterUtils.getExecutorProvider(),
                shopId,
                PresenterUtils.getNoticeRepository(NewPayApplication.getInstance()));
        interactor.execute(new Subscriber<List<Notice>>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(List<Notice> notices) {
                data = notices;
                onUpdateCompleted();
            }
        });
    }
}
