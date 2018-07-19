package com.unioncloud.newpay.data.repository.testp.datasource;

import android.util.Log;

import com.unioncloud.newpay.data.entity.testp.TestpEntity;
import com.unioncloud.newpay.domain.model.testp.Testp;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;

public class CloudTestpStore implements TestpStore{
    @Override
    public Observable<TestpEntity> queryByNo(String ckcode) {
        final TestpEntity testpEntity = new TestpEntity();
        testpEntity.setCkcode("0001");
        testpEntity.setCustAddr("深圳龙岗");
        testpEntity.setCustMobile("13434773383");
        testpEntity.setCustName("李志敏");
        testpEntity.setMkcode("1000");
        Log.i("-------queryByNo-------",ckcode);
//        Observable<TestpEntity> observable  = Observable.just(testpEntity);
        Observable<TestpEntity> observable  = Observable.just(testpEntity);

//        Observable<TestpEntity> observable = Observable.create(new Observable.OnSubscribe<TestpEntity>() {
//            @Override
//            public void call(Subscriber<? super TestpEntity> subscriber) {
//                subscriber.onNext(testpEntity);
//            }
//        });
        return null;
    }

    @Override
    public Observable<Boolean> update(Testp testp) {
//        如果没有异常的话,就是成功的,否则失败,只要捕捉异常就可以了
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
        Log.i("---------update------", "success" + sd.format(new Date()));
        return Observable.just(true);
    }
}