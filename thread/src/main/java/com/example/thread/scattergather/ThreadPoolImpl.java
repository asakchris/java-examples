package com.example.thread.scattergather;

import static com.example.thread.scattergather.PriceUtil.getPrice;
import static com.example.thread.util.ThreadUtil.sleep;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadPoolImpl {
  public static void main(String[] args) {
    // Store price in this set
    Set<Double> prices = new HashSet<>(3);

    // Create thread pool to capture price
    ExecutorService threadPool = Executors.newFixedThreadPool(3);

    // Submit job in thread pool to get price from 3 different vendors
    threadPool.submit(new PriceTask("AAPL.OQ", "Reuters", prices));
    threadPool.submit(new PriceTask("AAPL.US", "Bloomberg", prices));
    threadPool.submit(new PriceTask("AAPL", "Nasdaq", prices));

    // Wait for 10 seconds and process prices received
    // Even if we receive price from 3 vendors before 10 seconds,
    // main thread still wait for 10 seconds before proceeding
    sleep(TimeUnit.SECONDS, 10);

    // Check price
    log.info("prices: {}", prices);

    // Shutdown thread pool
    threadPool.shutdown();
  }
}

@Slf4j
class PriceTask implements Runnable {

  private final String ticker;
  private final String vendor;
  private Set<Double> prices;

  public PriceTask(String ticker, String vendor, Set<Double> prices) {
    this.ticker = ticker;
    this.vendor = vendor;
    this.prices = prices;
  }

  @Override
  public void run() {
    log.info("About to get {} price from {}", ticker, vendor);
    final double price = getPrice();
    log.info("Received {} price from {}: price={}", ticker, vendor, price);
    prices.add(price);
  }
}
