package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.PointerMigrationException;
import org.machariel.core.access.ArrayAccessor;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.util.Common;
import org.machariel.test.util.Lg;

public class ReferenceArray {
  private Random r = new Random();
  
  @Test
  public void intArray() throws InstantiationException, PointerMigrationException, IllegalArgumentException, IllegalAccessException {
    ArrayAccessor<Integer> sm = ArrayAccessor.acquire(Integer[].class);
    
    Integer[] a = new Integer[r.nextInt(3) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    long ref = Serializer.serialize(a, 5);
    
//    for (int i = 0; i < 10; i++) {
//      int index = r.nextInt(a.length);
//      int value = r.nextInt();
//      a[index] = value;
//      sm.put(ref, index, value);
//    }
//    
//    for (int i = 0; i < 10; i++) {
//      int index = r.nextInt(a.length);
//      int value0 = a[index];
//      int value1 = sm.get(ref, index);
//      assertEquals(value0, value1);
//    }
    
    Integer[] b = (Integer[]) Serializer.deserialize(ref);
    
    try {
      assertArrayEquals(a, b);
    } catch (Throwable e) {
      Lg.dump();
      e.printStackTrace();
    }
  }
  
  @Test
  public void testGenericArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    Object[] a = new Integer[r.nextInt(3) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    long ref = Serializer.serialize(a, 2);
    Object[] b = (Object[]) Serializer.deserialize(ref);
    
    try {
      assertArrayEquals(a, b);
    } catch (Throwable e) {
      Lg.dump();
      e.printStackTrace();
    }
  }
  
  @Test
  public void testGenericArray2() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    Bean0[] a = new Bean0[r.nextInt(3) + 1];
    for (int i = 0; i < a.length; i++) a[i] = new Bean1().randomize();
    
    long ref = Serializer.serialize(a, 2);
    Bean0[] b = (Bean0[]) Serializer.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
  }
}
