package com.example.thread.scattergather;

import static com.example.thread.scattergather.PriceUtil.getPrice;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureTwoImpl {
  public static void main(String[] args) {
    // Capture price from each vendor asynchronously
    CompletableFuture<Double> reuters = capturePrice("AAPL.OQ", "Reuters");
    CompletableFuture<Double> bloomberg = capturePrice("AAPL.US", "Bloomberg");
    CompletableFuture<Double> nasdaq = capturePrice("AAPL", "Nasdaq");

    List<CompletableFuture<Double>> allTasksList = List.of(reuters, bloomberg, nasdaq);
    CompletableFuture<Void> allTasks = CompletableFuture.allOf(reuters, bloomberg, nasdaq);

    // When all the tasks are completed, call `future.join()` to get their results and collect the
    // results in a list
    final CompletableFuture<List<Double>> priceFutures =
        allTasks.thenApply(
            aVoid ->
                allTasksList
                    .stream()
                    .map(future -> future.join())
                    .collect(toUnmodifiableList())
        );

    try {
      // Wait for all tasks to complete, but max of 10 seconds
      List<Double> prices = priceFutures.get(10, SECONDS);

      // Check price
      log.info("prices: {}", prices);
    } catch (InterruptedException exception) {
      log.error("InterruptedException while waiting for price", exception);
    } catch (ExecutionException exception) {
      log.error("ExecutionException while waiting for price", exception);
    } catch (TimeoutException exception) {
      log.error("TimeoutException while waiting for price", exception);
    }
  }

  private static CompletableFuture<Double> capturePrice(String ticker, String vendor) {
    return CompletableFuture.supplyAsync(
            () -> {
              log.info("About to get {} price from {}", ticker, vendor);
              final double price = getPrice();
              log.info("Received {} price from {}: price={}", ticker, vendor, price);
              return price;
            })
        // Without completeOnTimeout(), future.get() method with timeout throws exception
        // if any of the future didn't complete before timeout
        .completeOnTimeout(Double.MIN_VALUE, 10, SECONDS);
  }
}
