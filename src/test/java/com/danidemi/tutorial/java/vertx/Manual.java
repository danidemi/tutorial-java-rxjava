package com.danidemi.tutorial.java.vertx;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manual {

    private static Vertx vertx;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeClass
    public static void setUpVertx() {
        vertx = Vertx.vertx();
    }

    @Test public void createInstances() {

        {
            // You can’t do much in Vert.x-land unless you can communicate with a Vertx object!
            // It’s the control centre of Vert.x and is how you do pretty much everything,
            // including creating clients and servers, getting a reference to the event bus, setting timers, as well as many other things.
            // So how do you get an instance?
            // If you’re embedding Vert.x then you simply create an instance as follows.
            Vertx aVertx = Vertx.vertx();
        }

        {
            // you can also specify options if the defaults aren’t right for you
            Vertx aVertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));
        }
    }

    @Test public void vrtxIsEventDriven() throws InterruptedException {

        vertx.setPeriodic(1000, timerId -> {
            // This handler will get called every 1000 millis
            log.info("1st handler timer fired! {}", timerId);
        });

        vertx.setPeriodic(1000, new Handler<Long>() {
            @Override public void handle(Long timerId) {
                // This handler will get called every 1000 millis
                log.info("2nd handler timer fired! {}", timerId);
            }
        });

        Thread.sleep(5000);
    }

    @Test public void dontBlockTheVrtxEventLoopOrItWillWarnYouInTheLog() throws InterruptedException {

        vertx.setPeriodic(20, threadId -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignore) {

            }
        });
        // something like that should appear in the err stream.
        //   Aug 08, 2017 10:55:07 PM io.vertx.core.impl.BlockedThreadChecker
        //   WARNING: Thread Thread[vert.x-eventloop-thread-0,5,main] has been blocked for 4876 ms, time limit is 2000

        Thread.sleep(5000);
    }

    @Test public void youCouldAfterAllCallBlockingCode() {

        vertx.executeBlocking(future -> {
            // it took very long to compute this answer
            quietlySleep(4000);
            String theAnswer = "42";
            future.complete(theAnswer);
        }, res -> {
            log.info("Result is {}", res.result());
        });

        vertx.executeBlocking(new Handler<Future<String>>() {
            @Override public void handle(Future<String> future) {
                // it took very long to compute this answer
                Manual.this.quietlySleep(4000);
                String theAnswer = "42";
                future.complete(theAnswer);
            }
        }, new Handler<AsyncResult<String>>() {
            @Override public void handle(AsyncResult<String> res) {
                log.info("Result is {}", res.result());
            }
        });

        quietlySleep(5000);

    }

    @Test public void composeFutures() {

        // The "all" composition waits until all futures are successful or the firts one fail concurrently.
        Future<String> f1 = Future.future();
        Future<String> f2 = Future.future();
        CompositeFuture compositeFuture = CompositeFuture.all(f1, f2);

        compositeFuture.setHandler(new Handler<AsyncResult<CompositeFuture>>() {
            @Override public void handle(AsyncResult<CompositeFuture> event) {
                log.info("Succedeed ? {}", event.succeeded());
                event.result().list().stream().forEach( e-> log.info( "{}", e ) );
            }
        });

        f1.complete("Hello");
        f2.complete("World");

        quietlySleep(5000);

    }

    @Test public void runAVerticle() {

        Verticle myVerticle = new Verticle() {

            public Vertx vertx = null;

            @Override public Vertx getVertx() {
                return vertx;
            }

            @Override public void init(Vertx vertx, Context context) {
                this.vertx = vertx;
                log.info("init");
            }

            @Override public void start(Future<Void> startFuture) throws Exception {
                log.info("start");
                startFuture.complete();
            }

            @Override public void stop(Future<Void> stopFuture) throws Exception {
                log.info("stop");
                stopFuture.complete();
            }
        };

        vertx.deployVerticle(myVerticle, deploy -> {
            log.info("Deploy completed.");
            String deploymentId = deploy.result();
            vertx.undeploy(deploymentId, undeplpy -> {
                log.info("Undeploy completed.");
            });
        });


        quietlySleep(2000);

    }

    @Test public void eventBus() {

        EventBus bus = vertx.eventBus();

        bus.consumer("rx1", message -> {
           log.info("I received this message '{}'.", message.body());
        });

        bus.publish("rx1", "Take that!");

    }

    public void quietlySleep(long amount) {
        try {
            Thread.sleep(amount);
        } catch (InterruptedException ignored) {

        }
    }

}
