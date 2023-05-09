package com.example.facturas;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor();

    private AppExecutors() {
        // Private constructor to hide the implicit public one
    }

    public static void ioThread(Runnable runnable) {
        IO_EXECUTOR.execute(runnable);
    }
}

