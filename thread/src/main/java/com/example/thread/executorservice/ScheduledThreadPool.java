package com.example.thread.executorservice;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduledThreadPool {
  public static void main(String[] args) throws InterruptedException {
    // Tasks
    Runnable task = () -> log.info("Running scheduled task");
    Runnable fixedRateTask = () -> log.info("Running task every 10 seconds");
    Runnable fixedDelayTask = () -> log.info("Running task every 10 seconds after previous task");

    // Scheduling tasks
    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    // Run after 10 seconds delay (Run only once)
    service.schedule(task, 10, SECONDS);

    // Run repeatedly every 10 seconds with initial delay of 15 seconds
    service.scheduleAtFixedRate(fixedRateTask, 15, 10, SECONDS);

    // Run repeatedly every 10 seconds after previous task completes after initial delay of 15 seconds
    service.scheduleWithFixedDelay(fixedDelayTask, 15, 10, SECONDS);

    // Initiate shutdown
    service.shutdown();

    // block until all tasks are completed ot if timeout occurs
    service.awaitTermination(30, SECONDS);
  }
}
