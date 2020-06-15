package com.example.thread.deadlock;

import static com.example.thread.util.ThreadUtil.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {
  private Lock lockA = new ReentrantLock();
  private Lock lockB = new ReentrantLock();

  public static void main(String[] args) {
    DeadLock deadLock = new DeadLock();
    new Thread(deadLock::processA).start();
    new Thread(deadLock::processB).start();
  }

  public void processA() {
    lockA.lock();
    System.out.println("Performing task A1");
    sleep(SECONDS, 2);
    lockB.lock();
    System.out.println("Performing task A2");
    sleep(SECONDS, 2);
    lockA.unlock();
    lockB.unlock();
  }

  public void processB() {
    lockB.lock();
    System.out.println("Performing task B1");
    sleep(SECONDS, 2);
    lockA.lock();
    System.out.println("Performing task B2");
    sleep(SECONDS, 2);
    lockB.unlock();
    lockA.unlock();
  }
}
