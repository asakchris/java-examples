package com.example.thread.timeout;

import static com.example.thread.util.ThreadUtil.sleep;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
class Task implements Runnable {
  public volatile boolean keepRunning = true;

  @Override
  public void run() {
    if (!keepRunning) {
      log.info("Thread interrupted");
      return;
    }
    task("Task1", 10);
    if (!keepRunning) {
      log.info("Thread interrupted");
      return;
    }
    task("Task2", 20);
    if (!keepRunning) {
      log.info("Thread interrupted");
      return;
    }
    task("Task3", 30);
  }

  public void task(String name, long durationInSeconds) {
    log.info("Started {} and it will take {} seconds to complete", name, durationInSeconds);
    LocalDateTime start = LocalDateTime.now();
    while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < durationInSeconds) {
      if (!keepRunning) {
        log.info("{} interrupted", name);
        break;
      }
    }
    log.info("Completed {}", name);
  }
}
