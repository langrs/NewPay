package com.unioncloud.newpay.data.repository.testp;

import com.unioncloud.newpay.data.entity.testp.TestpEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.domain.repository.TestpRepository;

import rx.Observable;
import rx.functions.Func1;

public class TestpDataRepository implements TestpRepository {
    @Override
    public Observable<Testp> queryByNo(Testp testp) {

        Observable<TestpEntity> observable = StoreFactory.getTestpStore().queryByNo(testp.getCkcode());
        Observable<Testp> observable1 = observable.map(new Func1<TestpEntity, Testp>() {
            @Override
            public Testp call(TestpEntity testpEntity) {
                return mapper(testpEntity);
            }
        });
        return observable1;
    }

    @Override
    public Observable<Boolean> update(Testp testp) {
        return Observable.just(true);
    }

    private Testp mapper(TestpEntity testpEntity){
        Testp testp = new Testp();
        testp.setCustName(testpEntity.getCustName() + "/转换");
        testp.setMkcode(testpEntity.getMkcode());
        testp.setCustMobile(testpEntity.getCustMobile());
        testp.setCustAddr(testpEntity.getCustAddr()+ "转换");
        testp.setCkcode(testpEntity.getCkcode());
        return testp;
    }
}