package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.PointerMigrationException;
import org.machariel.core.serialization.Serializer;
import org.machariel.core.util.U;

import sun.misc.Unsafe;

public class StaticSerializer {
  private static final Unsafe u = U.instance();
  private static final Random r = new Random();
  
  @Test
  public void integerArray() throws InstantiationException, PointerMigrationException {
    Q q0 = new Q();
    
    q0.a = r.nextInt();
    q0.b = r.nextLong();
    q0.i = r.nextInt();
    q0.s = "edcvfr";
    
//    long ref = Serializer.serialize(new int[] {1,2,3,4,5}, 3);
    long ref = Serializer.serialize("hello", 3);
    
//    new Tries().dump(ref, 40);
    
    Object q1 = Serializer.deserialize(ref);
    
    System.out.println(q0);
    System.out.println(q1);
  }
  
  private static class Q {
    public int a = 5;
    public long b = 6;
    public Integer i = new Integer(1);
    public String s = new String("qwerty");
  }
}