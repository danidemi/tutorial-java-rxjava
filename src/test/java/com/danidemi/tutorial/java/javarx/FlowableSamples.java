package com.danidemi.tutorial.java.javarx;

import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;
import org.junit.Test;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.Callable;

import static io.reactivex.Flowable.*;

public class FlowableSamples {

    @Test
    public void helloWorld() {
        just("Hello world").subscribe(System.out::println);
    }

    @Test
    public void flowableFromCallable() {
        fromCallable(new Callable<String>() {
            @Override public String call() throws Exception {
                return "Hello world";
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void combineSlowCallables() {

        PNRService pnrService = new PNRService();



        Flowable<PNR> pnrs = fromArray("PNR-1", "PNR-2", "PNR-3")
                .map(pnrId -> pnrService.findByPnrId(pnrId))
                .filter(new Predicate<PNR>() {
                    @Override
                    public boolean test(PNR pnr) throws Exception {
                        return false;
                    }
                });


        pnrs.subscribe();

    }

    static class PNR {

        private String id;
        private String owner;
        private String airline;

        public PNR(String id, String owner, String airline) {
            this.id = id;
            this.owner = owner;
            this.airline = airline;
        }
    }

    static class AuthSystem {

        boolean canAccess(String user, PNR pnr){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) { }
            return true;
        }

    }

    static class PNRService {

        PNR findByPnrId(String pnrId)  {
            try {
                Thread.sleep(1000L);
                return new PNR(pnrId, "Mario", "Alitalia");
            } catch (InterruptedException e) {
                throw new UncheckedIOException( new IOException(e) );
            }
        }

    }

}
