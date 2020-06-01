package com.example.thread.producerconsumer;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.example.thread.util.ThreadUtil;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class BlockingQueueImpl {
  // Handles concurrent thread access
  private BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

  public static void main(String[] args) {
    new BlockingQueueImpl().start();
  }

  public void start() {
    // Producer
    final Runnable producer = () -> IntStream.rangeClosed(1, 100).forEach(this::createItem);
    // Start 2 producer threads
    new Thread(producer).start();
    new Thread(producer).start();

    // Consumer
    final Runnable consumer =
        () -> {
          while (true) {
            processItem();
          }
        };
    // Start 2 consumer threads
    new Thread(consumer).start();
    new Thread(consumer).start();

    // Wait for 10 seconds
    ThreadUtil.sleep(SECONDS, 10);
  }

  private void createItem(Integer value) {
    try {
      System.out.println("Producer about to add: " + value);
      // Thread blocks if queue is full
      queue.put(value);
    } catch (InterruptedException e) {
      System.err.println("InterruptedException in createItem");
    }
  }

  private void processItem() {
    try {
      // Thread blocks if queue is empty
      Integer value = queue.take();
      System.out.println("Consumer processed: " + value);
    } catch (InterruptedException e) {
      System.err.println("InterruptedException in processItem");
    }
  }
}
