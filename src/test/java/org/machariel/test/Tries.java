package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.util.U;

import sun.misc.Unsafe;

public class Tries {
  private static final Random r = new Random();
  private static final Unsafe u = U.instance();
  
  @Test
  public void method() {
    Object[] a = new Object[1];
    a[0] = new Integer(1);
    
    System.out.println(a.getClass().getComponentType());
    
    time(new Runnable() {
      @Override
      public void run() {
        
      }
    });
  }
  
  private static void time(Runnable r) {
    time(r, "time");
  }
  
  private static void time(Runnable r, String message) {
    long a = System.currentTimeMillis();
    r.run();
    long b = System.currentTimeMillis();
    System.out.println(message + ": " + (b - a));
  }
}
