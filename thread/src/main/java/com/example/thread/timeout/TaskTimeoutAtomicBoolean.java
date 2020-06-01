package com.example.thread.timeout;

import static com.example.thread.util.ThreadUtil.sleep;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskTimeoutAtomicBoolean {
  public static void main(String[] args) {
    // Create task
    TaskAtomicBoolean task = new TaskAtomicBoolean();
    Thread childThread = new Thread(task);

    // Start task
    childThread.start();

    // Timeout after 15 seconds
    sleep(TimeUnit.SECONDS, 15);

    // Stop child thread if not complete
    task.keepRunning.set(false);
  }
}

class TaskAtomicBoolean implements Runnable {
  public AtomicBoolean keepRunning = new AtomicBoolean(true);

  @Override
  public void run() {
    if (!keepRunning.get()) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task1", 10);
    if (!keepRunning.get()) {
      System.out.println("Thread interrupted");
      return;
    }
    task("Task2", 20);
    if (!keepRunning.get()) {
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
      if (!keepRunning.get()) {
        System.out.println(name + " interrupted");
        break;
      }
    }
    System.out.println("Completed " + name);
  }
}
