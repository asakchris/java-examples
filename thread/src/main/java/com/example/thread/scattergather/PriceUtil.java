package com.example.thread.scattergather;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class PriceUtil {
  public static double getPrice() {
    final int timeTaken = ThreadLocalRandom.current().nextInt(1, 15);
    LocalDateTime start = LocalDateTime.now();
    while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < timeTaken) {}
    return ThreadLocalRandom.current().nextDouble(1, 15);
  }
}
