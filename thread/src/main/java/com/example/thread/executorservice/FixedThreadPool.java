package com.example.thread.executorservice;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FixedThreadPool {
  public static void main(String[] args) throws InterruptedException {
    // Task
    Runnable task = () -> log.info("Thread Name: {}", Thread.currentThread().getName());

    // Create thread pool
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    // Submit tasks for execution
    IntStream.rangeClosed(1, 100).forEach(value -> threadPool.execute(task));

    // Initiate shutdown
    threadPool.shutdown();

    // will throw RejectionExecutionException
    //threadPool.execute(task);

    // Will return true, since shutdown has begun
    //threadPool.isShutdown();

    // Will return true if all tasks are completed including queued ones
    //threadPool.isTerminated();

    // block until all tasks are completed ot if timeout occurs
    threadPool.awaitTermination(10, SECONDS);

    // Will initiate shutdown and return all queued tasks
    //final List<Runnable> runnables = threadPool.shutdownNow();
  }
}
