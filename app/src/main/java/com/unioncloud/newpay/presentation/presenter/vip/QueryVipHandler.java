package com.unioncloud.newpay.presentation.presenter.vip;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.vip.QueryVipCardInteractor;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/14.
 */
public class QueryVipHandler extends UpdateHandler<Void, QueryVipHandler>
        implements Runnable {

    private String cdInfo;
    private String billNo;
    private PosInfo posInfo;

    public QueryVipHandler(String cdInfo, String billNo, PosInfo posInfo) {
        super(null, true);
        this.cdInfo = cdInfo;
        this.billNo = billNo;
        this.posInfo = posInfo;
    }

    @Override
    public void run() {
        QueryVipCardInteractor interactor = new QueryVipCardInteractor(
                PresenterUtils.getExecutorProvider(),
                cdInfo, billNo, posInfo,
                PresenterUtils.getVipRepository());
        interactor.execute(new Subscriber<VipCard>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(VipCard vipCard) {
                CheckoutDataManager.getInstance()
                        .getSelectedVipCard().setSelectedVipCard(vipCard);
                onUpdateCompleted();
            }
        });
    }
}
