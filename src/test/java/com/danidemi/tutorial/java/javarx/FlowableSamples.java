package com.danidemi.tutorial.java.javarx;

import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

public class FlowableSamples {

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

}
