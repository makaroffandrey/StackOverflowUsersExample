package com.makaroffandrey.stackoverflowusersexample.repo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class AppExecutor {
    private final Executor networkIO;

    @Inject
    AppExecutor() {
        networkIO = Executors.newFixedThreadPool(3);
    }

    public Executor networkIO() {
        return networkIO;
    }
}
