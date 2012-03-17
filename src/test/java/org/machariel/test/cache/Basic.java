package org.machariel.test.cache;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.machariel.cache.Cache;
import org.machariel.cache.EvictionListener;
import org.machariel.cache.LinkedLRUCache;

import sun.misc.Cleaner;

public class Basic {
  private static final Random r = new Random();
  
  @Test
  public void test() throws Exception {
    System.out.println(Thread.currentThread());
    
    EvictionListener<Long, Long> listener = new EvictionListener<Long, Long>() {
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      @Override public void onEviction(final Long key, final Long value) {
        executor.submit(new Callable<Void>() {
          @Override public Void call() throws IOException {
            System.out.println(Thread.currentThread() + ": " + key + " = " + value);
            return null;
          }
        });
      }
    };
    
    Cache<Long, Long> cache = new LinkedLRUCache.Builder<Long, Long>()
        .maximumWeightedCapacity(5)
        .listener(listener)
        .build();
    
    cache.put(1L, r.nextLong());
    
    Long arg = new Long(2L);
    Cleaner.create(arg, new Runnable() {
      @Override
      public void run() {
        System.out.println("cleaned");
      }
    });
    cache.put(arg, r.nextLong());
    
    cache.put(3L, r.nextLong());
    cache.put(4L, r.nextLong());
    cache.put(5L, r.nextLong());
    
    cache.get(1L);
    cache.get(arg);
    cache.get(3L);
    cache.get(4L);
    cache.get(5L);
    cache.get(1L);
    
    cache.put(6L, r.nextLong());
    cache.get(6L);
    
    for (Long key : cache.keySet()) { System.out.println(key + " " + cache.get(key)); }
    
    arg = null;
    System.gc();
    
    System.in.read();
  }
}
