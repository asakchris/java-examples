package com.example.thread.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtil {

  private ThreadUtil() {}

  public static void sleep(TimeUnit timeUnit, long duration) {
    try {
      timeUnit.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void task(String name, long durationInSeconds) {
    log.info("Started {} and it will take {} seconds to complete", name, durationInSeconds);
    LocalDateTime start = LocalDateTime.now();
    while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < durationInSeconds) {
      if (Thread.currentThread().isInterrupted()) {
        log.info("{} interrupted", name);
        break;
      }
    }
    log.info("Completed {}", name);
  }

  public static void tasks() {
    if (Thread.currentThread().isInterrupted()) {
      log.info("Thread interrupted");
      return;
    }
    task("Task1", 10);
    if (Thread.currentThread().isInterrupted()) {
      log.info("Thread interrupted");
      return;
    }
    task("Task2", 20);
    if (Thread.currentThread().isInterrupted()) {
      log.info("Thread interrupted");
      return;
    }
    task("Task3", 30);
  }
}
