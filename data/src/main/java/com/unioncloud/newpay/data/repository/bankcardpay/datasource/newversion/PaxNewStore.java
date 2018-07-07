package com.unioncloud.newpay.data.repository.bankcardpay.datasource.newversion;

import android.content.Context;

import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.repository.bankcardpay.datasource.BankcardPayStore;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.pax.PaxPayException;
import com.unioncloud.pax.PaxResponse;
import com.unioncloud.pax.PaxService;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class PaxNewStore implements BankcardPayStore {
    PaxService paxService;
    Context context;

    public PaxNewStore(Context context) {
        this.context = context.getApplicationContext();
        this.paxService = new PaxService(this.context);
    }

    @Override
    public Observable<BankcardSaleResult> sale(final BankcardSaleRequest request) {
        return Observable.create(new Observable.OnSubscribe<PaxResponse>() {
            @Override
            public void call(Subscriber<? super PaxResponse> subscriber) {
                try {
                    subscriber.onNext(paxService.sale(MapperUtils.mapperSaleRequest(request)));
                } catch (PaxPayException e) {
                    subscriber.onError(new RemoteDataException(context.getString(e.getMessageRes())));
                }
            }
        }).map(new Func1<PaxResponse, BankcardSaleResult>() {
            @Override
            public BankcardSaleResult call(PaxResponse paxResponse) {
                if (paxResponse.isSuccess()) {
                    return MapperUtils.mapperSaleResponse(paxResponse);
                }
                throw Exceptions.propagate(new RemoteDataException(paxResponse.getRspCode()));
            }
        });
    }

    @Override
    public Observable<BankcardRefundResult> saleVoid(final BankcardRefundRequest request) {
        return Observable.create(new Observable.OnSubscribe<PaxResponse>() {
            @Override
            public void call(Subscriber<? super PaxResponse> subscriber) {
                try {
                    subscriber.onNext(paxService.saleVoid(MapperUtils.mapperSaleVoidRequest(request)));
                } catch (PaxPayException e) {
                    subscriber.onError(new RemoteDataException(context.getString(e.getMessageRes())));
                }
            }
        }).map(new Func1<PaxResponse, BankcardRefundResult>() {
            @Override
            public BankcardRefundResult call(PaxResponse paxResponse) {
                if (paxResponse.isSuccess()) {
                    return MapperUtils.mapperRefundResponse(paxResponse);
                }
                throw Exceptions.propagate(new RemoteDataException(paxResponse.getRspCode()));
            }
        });
    }

    @Override
    public Observable<BankcardRefundResult> refund(final BankcardRefundRequest request) {
        return Observable.create(new Observable.OnSubscribe<PaxResponse>() {
            @Override
            public void call(Subscriber<? super PaxResponse> subscriber) {
                try {
                    subscriber.onNext(paxService.refund(MapperUtils.mapperRefundRequest(request)));
                } catch (PaxPayException e) {
                    subscriber.onError(new RemoteDataException(context.getString(e.getMessageRes())));
                }
            }
        }).map(new Func1<PaxResponse, BankcardRefundResult>() {
            @Override
            public BankcardRefundResult call(PaxResponse paxResponse) {
                if (paxResponse.isSuccess()) {
                    return MapperUtils.mapperRefundResponse(paxResponse);
                }
                throw Exceptions.propagate(new RemoteDataException(paxResponse.getRspCode()));
            }
        });
    }
}
