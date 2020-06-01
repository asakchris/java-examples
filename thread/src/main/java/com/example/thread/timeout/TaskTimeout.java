package com.example.thread.timeout;

import static com.example.thread.util.ThreadUtil.sleep;
import static com.example.thread.util.ThreadUtil.tasks;

import java.util.concurrent.TimeUnit;

public class TaskTimeout {
  public static void main(String[] args) {
    Thread childThread = new Thread(() -> tasks());

    // Start task
    childThread.start();

    // Timeout after 15 seconds
    sleep(TimeUnit.SECONDS, 15);

    // Stop child thread if not complete
    childThread.interrupt();
  }
}
