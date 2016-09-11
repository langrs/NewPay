package com.unioncloud.newpay.data.repository.print;

import android.content.Context;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.repository.PrintRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/19.
 */
public class PrintDataRepository implements PrintRepository {

    private Context context;

    public PrintDataRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<Boolean> printOrder(PrintOrderInfo orderInfo) {
        return StoreFactory.getPrintStore(context).printOrder(orderInfo);
    }
}
