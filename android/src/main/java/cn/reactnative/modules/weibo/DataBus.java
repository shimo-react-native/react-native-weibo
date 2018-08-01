/*
 * Copyright (c) 2018. chenqiang Inc. All rights reserved.
 */

package cn.reactnative.modules.weibo;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * 封装的通信总线
 *
 * @author chenqiang
 * @date 2018/8/1
 */
public class DataBus {


    private final Map<String, ObserveWrapper> bus;

    private DataBus() {
        bus = new HashMap<>();
    }

    public ObserveWrapper with(String key) {
        return with(key, Object.class);
    }

    private static class SingletonHolder {
        private static final DataBus DEFAULT_BUS = new DataBus();
    }

    public static DataBus get() {
        return DataBus.SingletonHolder.DEFAULT_BUS;
    }

    public <T> ObserveWrapper<T> with(String key, Class<T> type) {
        ObserveWrapper<T> observeWrapper = null;
        if (!bus.containsKey(key)) {
            PublishSubject<T> subject = PublishSubject.create();
            observeWrapper = new ObserveWrapper(subject);
            bus.put(key, observeWrapper);
        } else {
            observeWrapper = bus.get(key);
        }
        return observeWrapper;
    }

    static class ObserveWrapper<T> {

        private PublishSubject<T> observable;

        public ObserveWrapper(PublishSubject<T> observable) {
            this.observable = observable;
        }

        public void post(T t) {
            observable.onNext(t);
        }

        public void observe(final OnBusResult<T> onBusResult) {
            observable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<T>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(T t) {
                            onBusResult.onResult(t);
                        }
                    });
        }

        protected void release() {
            if (observable != null) {
                observable.onCompleted();
                observable = null;
            }
        }
    }

    public void remove(String key) {
        ObserveWrapper observeWrapper = bus.remove(key);
        if (observeWrapper != null) {
            observeWrapper.release();
        }
    }

    public interface OnBusResult<T> {
        void onResult(T t);
    }

}
