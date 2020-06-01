package com.example.thread.scattergather;

import static com.example.thread.scattergather.PriceUtil.getPrice;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureImpl {
  public static void main(String[] args) {
    // Store price in this set
    Set<Double> prices = new HashSet<>(3);

    // Capture price from each vendor asynchronously
    CompletableFuture<Void> reuters =
        CompletableFuture.runAsync(new PriceTask("AAPL.OQ", "Reuters", prices));
    CompletableFuture<Void> bloomberg =
        CompletableFuture.runAsync(new PriceTask("AAPL.US", "Bloomberg", prices));
    CompletableFuture<Void> nasdaq =
        CompletableFuture.runAsync(new PriceTask("AAPL", "Nasdaq", prices));

    CompletableFuture<Void> allTasks = CompletableFuture.allOf(reuters, bloomberg, nasdaq);
    try {
      // Wait for all tasks to complete, but max of 10 seconds
      allTasks.get(10, SECONDS);
    } catch (InterruptedException exception) {
      log.error("InterruptedException while waiting for price", exception);
    } catch (ExecutionException exception) {
      log.error("ExecutionException while waiting for price", exception);
    } catch (TimeoutException exception) {
      log.error("TimeoutException while waiting for price", exception);
    }

    // Check price
    log.info("prices: {}", prices);
  }
}
