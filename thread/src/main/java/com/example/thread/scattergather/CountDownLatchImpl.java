package com.example.thread.scattergather;

import static com.example.thread.scattergather.PriceUtil.getPrice;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountDownLatchImpl {
  public static void main(String[] args) {
    // Store price in this set
    Set<Double> prices = new HashSet<>(3);

    // Create thread pool to capture price
    ExecutorService threadPool = Executors.newFixedThreadPool(3);

    // Create countdown latch with count as 3
    CountDownLatch latch = new CountDownLatch(3);

    // Submit job in thread pool to get price from 3 different vendors
    threadPool.submit(new CountDownLatchPriceTask("AAPL.OQ", "Reuters", prices, latch));
    threadPool.submit(new CountDownLatchPriceTask("AAPL.US", "Bloomberg", prices, latch));
    threadPool.submit(new CountDownLatchPriceTask("AAPL", "Nasdaq", prices, latch));

    // Wait for 10 seconds and process prices received
    // Countdown latch waits for 10 seconds, if price received from all 3 vendors
    // before 10 seconds, it immediately moves to next step
    // and doesn't wait for timeout seconds
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException exception) {
      log.error("InterruptedException while waiting for price", exception);
    }

    // Check price
    log.info("prices: {}", prices);

    // Shutdown thread pool
    threadPool.shutdown();
  }
}

@Slf4j
class CountDownLatchPriceTask implements Runnable {

  private final String ticker;
  private final String vendor;
  private final Set<Double> prices;
  private final CountDownLatch latch;

  public CountDownLatchPriceTask(
      String ticker, String vendor, Set<Double> prices, CountDownLatch latch) {
    this.ticker = ticker;
    this.vendor = vendor;
    this.prices = prices;
    this.latch = latch;
  }

  @Override
  public void run() {
    log.info("About to get {} price from {}", ticker, vendor);
    final double price = getPrice();
    log.info("Received {} price from {}: price={}", ticker, vendor, price);
    prices.add(price);
    // Price received, decrement the countdown by 1
    latch.countDown();
  }
}
