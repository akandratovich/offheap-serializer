package org.machariel.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.machariel.core.PointerMigrationException;
import org.machariel.core.util.*;
import org.machariel.test.util.*;

import sun.misc.Unsafe;

class A {
  int a = 7;
}

public class Tries {
  private static final Random r = new Random();
  private static final Unsafe u = U.instance();
  
//  @Test
  public void size() {
    Reflection.size(1);
    A a = new A();
    long ka2 = u.getAddress(U.o2a(a) + 4);
//    System.out.println(u.getInt(ka2 + 3 * Unsafe.ADDRESS_SIZE));
  }
  
  @Test
  public void method() {
    klass(getClass(), r, u, new Integer(1), new ArrayList<Integer>(), new ArrayList[1], new List[1], new int[6]);
//    A a = new A();
//    long ka2 = u.getAddress(U.o2a(a) + 4);
//    long time = 0;
//    
//    while (true) {
//      if (System.currentTimeMillis() - time > 4000) {
//        byte[] b = new byte[1024 * 1024 * 128];
//        long ka = u.getAddress(U.o2a(a) + 4);
//        System.out.println();
//        System.out.println(ka);
//        System.out.println(new PointerMigrationException(ka).getMessage());
//        System.out.println(U.klass(ka2));
//        dump(ka2, 60);
//        
//        time = System.currentTimeMillis();
//        System.gc();
//      }
//    }
  }
  
  
  
  public void dump(long ref, int size) {
    byte[] hex = new byte[size];
    for (int j = 0; j < size; j++) hex[j] = u.getByte(ref + j);
    System.out.print(Common.hex(hex));
  }
  
  public void klass(Object... o) {
    List<Long> a = new ArrayList<Long>();
    for (Object q : o) {
      long ref = U.o2a(q);
      a.add(u.getAddress(ref + 4));
//      System.out.print(u.getInt(ref + Reflection.MAGIC_SIZE) + "\t");
    }
    
    byte[] hex = new byte[4];
    for (int i = 0; i < 30; i++) {
      for (Long ka : a) {
        for (int j = 0; j < 4; j++) {
          hex[j] = u.getByte(ka + i * 4 + j);
        }
        System.out.print(Common.hex(hex) + "\t\t");
      }
      
      System.out.println();
    }
    
    System.out.println();
    for (Long ka : a) {
      System.out.print(U.a2o(u.getAddress(ka + 64)) + "\t");
    }
  }
}
