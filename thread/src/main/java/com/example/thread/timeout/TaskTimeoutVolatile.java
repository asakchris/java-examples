package com.example.thread.timeout;

import static com.example.thread.util.ThreadUtil.sleep;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TaskTimeoutVolatile {
  public static void main(String[] args) {
    // Create task
    Task task = new Task();
    Thread childThread = new Thread(task);

    // Start task
    childThread.start();

    // Timeout after 15 seconds
    sleep(TimeUnit.SECONDS, 15);

    // Stop child thread if not complete
    task.keepRunning = false;
  }
}

class Task implements Runnable {
  public volatile boolean keepRunning = true;

  @Override
  public void run() {
    if (!keepRunning) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task1", 10);
    if (!keepRunning) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task2", 20);
    if (!keepRunning) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task3", 30);
  }

  public void task(String name, long durationInSeconds) {
    System.out.println(
        "Started " + name + " and it takes " + durationInSeconds + " seconds to complete");
    LocalDateTime start = LocalDateTime.now();
    while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < durationInSeconds) {
      if (!keepRunning) {
        System.out.println(name + " interrupted");
        break;
      }
    }
    System.out.println("Completed " + name);
  }
}
