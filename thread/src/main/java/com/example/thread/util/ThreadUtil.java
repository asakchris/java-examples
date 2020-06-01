package com.example.thread.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

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
    System.out.println(
        "Started " + name + " and it takes " + durationInSeconds + " seconds to complete");
    LocalDateTime start = LocalDateTime.now();
    while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < durationInSeconds) {
      if (Thread.currentThread().isInterrupted()) {
        System.out.println(name + " interrupted");
        break;
      }
    }
    System.out.println("Completed " + name);
  }

  public static void tasks() {
    if (Thread.currentThread().isInterrupted()) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task1", 10);
    if (Thread.currentThread().isInterrupted()) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task2", 20);
    if (Thread.currentThread().isInterrupted()) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task3", 30);
  }
}
