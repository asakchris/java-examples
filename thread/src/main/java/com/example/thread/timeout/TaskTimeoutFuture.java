package com.example.thread.timeout;

import static com.example.thread.util.ThreadUtil.sleep;
import static com.example.thread.util.ThreadUtil.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskTimeoutFuture {
  public static void main(String[] args) {
    ExecutorService threadPool = Executors.newFixedThreadPool(2);

    // Start task
    Future<?> future = threadPool.submit(() -> tasks());

    // Timeout after 15 seconds
    sleep(TimeUnit.SECONDS, 15);

    // Stop child thread if not complete
    future.cancel(true);

    // Shutdown thread pool
    threadPool.shutdown();
  }
}
