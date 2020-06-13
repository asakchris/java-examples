package com.example.thread.executorservice;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachedThreadPool {
  public static void main(String[] args) throws InterruptedException {
    // Short Task
    Runnable task = () -> log.info("Thread Name: {}", Thread.currentThread().getName());

    // Create thread pool
    ExecutorService threadPool = Executors.newCachedThreadPool();

    // Submit tasks for execution
    IntStream.rangeClosed(1, 100).forEach(value -> threadPool.execute(task));

    // Initiate shutdown
    threadPool.shutdown();

    // block until all tasks are completed ot if timeout occurs
    threadPool.awaitTermination(10, SECONDS);
  }
}
