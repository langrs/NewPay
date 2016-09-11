package com.unioncloud.newpay.presentation.presenter.gift;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.gift.QueryGiftCardInteractor;
import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/3.
 */
public class QueryGiftCardHandler extends UpdateHandler<GiftCard, QueryGiftCardHandler>
        implements Runnable {

    private PosInfo posInfo;
    private String track;

    public QueryGiftCardHandler(String track, PosInfo posInfo) {
        super(null, true);
        this.track = track;
        this.posInfo = posInfo;
    }

    @Override
    public void run() {
        QueryGiftCardInteractor interactor = new QueryGiftCardInteractor(
                PresenterUtils.getExecutorProvider(),
                track,
                posInfo,
                PresenterUtils.getGiftRepository());
        interactor.execute(new Subscriber<GiftCard>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(GiftCard giftCard) {
                data = giftCard;
                onUpdateCompleted();
            }
        });

    }
}
