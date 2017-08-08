package com.danidemi.tutorial.java.javarx;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;


public class ObservableSamples {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void byDefaultAnObservableEmitsTheWholeSequenceOfItemsEachTimeItIsSubscribed() {

        // given
        log.info("Hello!");

        // "Cold" observable, emits all the events each time it is subscribed.
        Observable<String> o = Observable.just("hello", "world", "folks");

        // then

        assertEquals("sub1: hello world folks.", o.subscribeWith(new StringObserver("sub1")).toString() );
        assertEquals("sub2: hello world folks.", o.subscribeWith(new StringObserver("sub2")).toString() );
    }

    private static class StringObserver implements Observer<String> {

        private final String prefix;
        private StringBuffer sb;

        public StringObserver(String prefix) {
            this.prefix = prefix;
        }

        @Override public void onSubscribe(Disposable d) {
            sb = new StringBuffer(prefix + ":");
        }

        @Override public void onNext(String s) {
            if(sb.length() > 0) sb.append(" ");
            sb.append(s);
        }

        @Override public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override public void onComplete() {
            sb.append(".");
        }

        @Override public String toString() {
            return sb.toString();
        }
    }
}
