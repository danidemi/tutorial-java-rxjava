package com.danidemi.tutorial.java.javarx;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RxJavaSamples {

    @Test
    void helloWorld() {
        Flowable.just("Hello world").subscribe(System.out::println);
    }

    @Test
    void flowableFromCallable() {
        Flowable.fromCallable(new Callable<String>() {
            @Override public String call() throws Exception {
                return "Hello world";
            }
        }).subscribe(System.out::println);
    }

    @Test
    void itSeemsTheObservableAlwaysStartEveryTimeAnObserverIsAdded() {

        // cold observer
        Observable<String> o = Observable.just("hello", "world", "folks");
        o.subscribeWith(new StringObserver("sub1"));
        o.subscribeWith(new StringObserver("sub2"));
    }

    @Test
    void evenComplexObserversRestartFromTheBeginning() throws InterruptedException {

        Observable<Long> iterval = Observable.interval(0L, 500L, TimeUnit.MILLISECONDS);
        AtomicBoolean goOn = new AtomicBoolean(true);

        iterval.subscribeWith(new DefaultObserver<Long>() {

            @Override public void onNext(Long aLong) {
                System.out.println("a:" + aLong);
                if(aLong > 10){
                    goOn.set(false);
                }
                if(aLong == 5){
                    iterval.subscribeWith(new DefaultObserver<Long>() {
                        @Override public void onNext(Long aLong) {
                            System.out.println("b:" + aLong);
                        }

                        @Override public void onError(Throwable e) {

                        }

                        @Override public void onComplete() {

                        }
                    });
                }
            }

            @Override public void onError(Throwable e) {

            }

            @Override public void onComplete() {

            }

        });

        while(goOn.get()){
            Thread.sleep(1000);
        }
        System.out.println("stopping");



    }

    private static class StringObserver implements Observer<String> {

        private final String prefix;

        public StringObserver(String prefix) {
            this.prefix = prefix;
        }

        @Override public void onSubscribe(Disposable d) {
            System.out.println(prefix + ":subscribe");
        }

        @Override public void onNext(String s) {
            System.out.println(prefix + ":" + s);
        }

        @Override public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override public void onComplete() {
            System.out.println(prefix + ":complete");
        }
    }
}
