package com.example.thread.producerconsumer;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.example.thread.util.ThreadUtil;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class LockImpl {
  private BlockingQueueLock<Integer> queue = new BlockingQueueLock<>(10);

  public static void main(String[] args) {
    new LockImpl().start();
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
    System.out.println("Producer about to add: " + value);
    // Thread blocks if queue is full
    queue.put(value);
  }

  private void processItem() {
    // Thread blocks if queue is empty
    Integer value = queue.take();
    System.out.println("Consumer processed: " + value);
  }
}

class BlockingQueueLock<E> {
  // Can also use array, but need to track head and tail index
  private Queue<E> queue;
  private int maxSize = 16;
  private ReentrantLock lock = new ReentrantLock(true);
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();

  public BlockingQueueLock(int size) {
    queue = new LinkedList<>();
    this.maxSize = size;
  }

  public void put(E e) {
    lock.lock();
    try {
      // When consumer removes an item, it signals not full condition, but there can be more than
      // one producer thread in waiting state for not full condition. So need to check queue size
      // again before adding an item
      while (queue.size() == maxSize) {
        try {
          // If the queue size is full, wait for item to be removed, lock will be released and
          // thread goes to waiting state
          notFull.await();
        } catch (InterruptedException exception) {
          System.err.println("InterruptedException while waiting for not full condition");
        }
      }
      queue.add(e);
      // New item is added into the queue, so signal all thread waiting on not empty condition
      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
  }

  public E take() {
    lock.lock();
    try {
      // When producer adds an item, it signals not empty condition, but there can be more than
      // one consumer thread in waiting state for not empty condition. So need to check queue size
      // again before removing an item
      while (queue.size() == 0) {
        try {
          // If queue is empty, wait for an item to be added, lock will be released and thread
          // goes to waiting state
          notEmpty.await();
        } catch (InterruptedException exception) {
          System.err.println("InterruptedException while waiting for not empty condition");
        }
      }
      E item = queue.remove();
      // An item is removed from the queue, so signal all thread waiting on not full condition
      notFull.signalAll();
      return item;
    } finally {
      lock.unlock();
    }
  }
}
