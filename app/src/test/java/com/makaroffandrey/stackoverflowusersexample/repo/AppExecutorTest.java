package com.makaroffandrey.stackoverflowusersexample.repo;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

public class AppExecutorTest {

    private Thread executorThread;

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void threadingTest() throws InterruptedException {
        //checks that appExecutor proivides a background thread for network requests
        AppExecutor appExecutor = new AppExecutor();
        Executor executor = appExecutor.networkIO();
        assertThat(executor).isNotNull();
        executor.execute(() -> {
            executorThread = Thread.currentThread();
            latch.countDown();
        });
        latch.await();
        assertThat(executorThread).isNotEqualTo(Thread.currentThread());
    }

}
