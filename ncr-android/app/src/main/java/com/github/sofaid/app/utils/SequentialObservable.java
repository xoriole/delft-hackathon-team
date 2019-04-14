package com.github.sofaid.app.utils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * An {@link Observable} which takes a list of {@link Observable}, and subscribes to them sequentially
 * This is useful for tasks where we need to perform one asynchronous call after completion another!
 * Created by robik on 1/31/17.
 */
public class SequentialObservable {

    /**
     * Returns an {@link Observable} which when subscribed, subscribes to individual observables one by one.
     * If any observable fires onError, then onError of this Observable is also fired. Subsequent observables are not subscribed to after this!
     * Whenever an observable fires onNext, this observable fires onNext()
     * If an observable fires onCompleted(), then it moves on to next observable. If no observable remains, then it fires onCompleted()
     */
    public Observable observe(final List<Observable> observables) {
        return observe(observables, false, 0);
    }

    /**
     * Returns an {@link Observable} which when subscribed, subscribes to individual observables one by one.
     * If any observable fires onError, then onError of this Observable is also fired. Subsequent observables are not subscribed to after this!
     * Whenever an observable fires onNext, this observable fires onNext()
     * If an observable fires onCompleted(), then it moves on to next observable. If no observable remains, then it fires onCompleted()
     *
     * @param observables - list of {@link Observable} to be subscribed to sequentially
     * @param currIndex   - index from which to start subscribing
     * @return
     */
    public Observable observe(final List<Observable> observables, final int currIndex) {
        return observe(observables, false, currIndex);
    }

    public Observable observe(final List<Observable> observables, final boolean continueOnError, final int currIndex) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                if (currIndex == observables.size()) {
                    subscriber.onCompleted();
                } else {
                    Observable observable = observables.get(currIndex);
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.e(e, "some problem");
                                    if (!continueOnError) {
                                        subscriber.onError(e);
                                    } else {
                                        subscriber.onNext(null);
                                        observe(observables, continueOnError, currIndex + 1)
                                                .subscribe(subscriber);
                                    }
                                }

                                @Override
                                public void onNext(Object o) {
                                    subscriber.onNext(o);
                                    observe(observables, continueOnError, currIndex + 1)
                                            .subscribe(subscriber);
                                }
                            });
                }
            }
        });
    }
}
