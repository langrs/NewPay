package com.unioncloud.newpay.presentation.presenter.notice;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.notice.QueryNoticeInteractor;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/12.
 */
public class QueryNoticeHandler extends UpdateHandler<List<Notice>, QueryNoticeHandler>
    implements Runnable {

    private String shopId;

    public QueryNoticeHandler(String shopId) {
        super(null, true);
        this.shopId = shopId;
    }

    @Override
    public void run() {
        QueryNoticeInteractor interactor = new QueryNoticeInteractor(
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
