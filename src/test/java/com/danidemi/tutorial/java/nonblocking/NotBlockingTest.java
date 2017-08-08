package com.danidemi.tutorial.java.nonblocking;



import org.junit.Test;

import java.io.IOException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousFileChannel;

import java.nio.file.*;

public class NotBlockingTest {

    @Test
    public void notBlockingFileAccess() throws IOException {

        Path path = FileSystems.getDefault().getPath("src", "test", "resources", "lorem.txt");
        AsynchronousChannel afc = AsynchronousFileChannel.open(
                path,
                StandardOpenOption.READ
        );


    }

}
