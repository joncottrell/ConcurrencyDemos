package com.concurrency.demo.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo sample of how a CyclicBarrier works in Java.
 */
public class CyclicBarrierDemo {
  private static final Logger logger = Logger.getLogger(CyclicBarrierDemo.class.getSimpleName());

  private static final int NUM_THREADS = 4;
  private static final int MAX_WAIT_TIME_MS = 5000;

  public static void main(String[] args) {
    CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo(NUM_THREADS);
    cyclicBarrierDemo.launchDemo();
  }

  private final int numThreads;

  public CyclicBarrierDemo(int numThreads) {
    this.numThreads = numThreads;
  }

  public void launchDemo() {
    final CyclicBarrier cyclicBarrier = new CyclicBarrier(numThreads, new Runnable() {
      @Override
      public void run() {
        logger.log(Level.INFO, "All threads reached barrier.");
      }
    });

    final Random randomGenerator = new Random();
    for (int i = 1; i <= numThreads; i++) {
      final int currThread = i;
      final int waitTimeMs = randomGenerator.nextInt(MAX_WAIT_TIME_MS);
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          logger.log(Level.INFO, "Thread started: " + currThread);

          // Thread will sleep for a random amount of time.
          try {
            Thread.sleep(waitTimeMs);
          } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
          }

          logger.log(Level.INFO, "Thread " + currThread + " woke up.");

          // Ready to wait for barrier.
          try {
            cyclicBarrier.await();
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
          } catch (BrokenBarrierException ex) {
            Thread.currentThread().interrupt();
            return;
          }

          logger.log(Level.INFO, "Thread " + currThread + " passed barrier.");
        }
      });
      thread.start();
    }
  }
}