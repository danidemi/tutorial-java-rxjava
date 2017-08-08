package com.danidemi.tutorial.java.javarx;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;


public class CreatingObservables {

    private Logger log = LoggerFactory.getLogger(getClass());

    @After
    public void logTestEnd() {
        log.info("Test ended.");
    }

    @Test
    public void createAnObserver() {

        Observable<String> observable = Observable.create(observer -> {
            for (int i = 0; i < 5; i++) {
                log.info("emitting '{}'", i);
                observer.onNext(String.valueOf(i));
            }
            observer.onComplete();
        });

        StringBuffer sb = new StringBuffer();
        observable.subscribe(next -> {
            log.info("observing '{}'", next);
            sb.append(next);
        });

        assertEquals( "01234", sb.toString() );

    }

    @Test
    public void anObserverThatEmitsDataAtIntervals() throws InterruptedException {

        Observable<Long> observableTimer = Observable.timer(1500, TimeUnit.MILLISECONDS);

        AtomicLong theLong = new AtomicLong(999L);
        observableTimer.subscribe(new Consumer<Long>() {
            @Override public void accept(Long aLong) throws Exception {
                log.info("observing '{}'", aLong);
                theLong.set(aLong);
            }
        });

        while(theLong.get() != 0L){
            log.info("waiting");
            Thread.sleep(250);
        }

        assertEquals( 0L, theLong.get() );


    }

}
