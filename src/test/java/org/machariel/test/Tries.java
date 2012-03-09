package org.machariel.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.machariel.core.util.*;
import org.machariel.test.util.*;

import sun.misc.Unsafe;

class A {
  int a = 7;
}

public class Tries {
  private static final Random r = new Random();
  private static final Unsafe u = U.instance();
  
  @Test
  public void method() {
    A a = new A();

    long ref = U.o2a(a);
    // long ref = U.o2a(new ArrayList().getClass());
    // byte[] hex = new byte[20];
    // for (int i = 0; i < 20; i++) hex[i] = u.getByte(ref + i);

    // System.out.println(Common.hex(hex));
    // System.out.println();

    long ka = u.getAddress(ref + 4);
    // long assc = u.getAddress(ref + 20);

    // hex = new byte[80];
    // for (int i = 0; i < 80; i++) hex[i] = u.getByte(ka + i);
    // System.out.println(Common.hex(hex));
    // System.out.println();

    // int offset = u.getInt(ka + 16);
    // hex = new byte[80];
    // for (int i = 0; i < 80; i++) hex[i] = u.getByte(ka + offset + 16 + i);
    // System.out.println(Common.hex(hex));
    // System.out.println();
    // System.out.println(offset);

    // long ca = u.getAddress(ka + 36 + 16 + 12);
    // Object cc = U.a2o(ca);
    // System.out.println(cc);
  }

  @Test
  public void check() {
    long time = 0;
    // while (true) {
    for (int i = 0; i < 10; i++) {
      byte[] q = new byte[1024 * 1024 * 128];
      long ref = U.o2a(new ArrayList());
      long ka = u.getAddress(ref + 4); // klass pointer
      // if (System.currentTimeMillis() - time > 2000) {
        System.out.println(ka);
        // time = System.currentTimeMillis();
      // }
      System.gc();
    }
  }
}
