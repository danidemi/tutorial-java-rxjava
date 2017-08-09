package com.danidemi.tutorial.java.article;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.rx.java.RxHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class VertxAndJavaRx {

    private static Vertx vertx;

    @BeforeClass
    public static void setUpVertex() {
        vertx = Vertx.vertx();
    }

    @AfterClass
    public static void shutDownVertex() {
        vertx.close();
    }

    @Test
    public void example() {

        FileSystem fileSystem = vertx.fileSystem();
        fileSystem.open("/data.txt", new OpenOptions(), result -> {
            AsyncFile file = result.result();
            rx.Observable<Buffer> observable = RxHelper.toObservable(file);
            observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
        });

    }

}
