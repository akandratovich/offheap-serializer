package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.access.ArrayAccessor;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class ReferenceArray {
  private Random r = new Random();
  
  @Test
  public void intArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ArrayAccessor<Integer> sm = ArrayAccessor.acquire(Integer[].class);

    Integer[] a = new Integer[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();

    long ref = UnsafeSerializer.serialize(a, 5);

    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value = r.nextInt();
      a[index] = value;
      sm.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value0 = a[index];
      int value1 = sm.get(ref, index);
      assertEquals(value0, value1);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value0 = a[index];
      long vref = sm.getReference(ref, index);
      int value1 = (Integer) UnsafeSerializer.deserialize(vref, false);
      assertEquals(value0, value1);
    }

    Integer[] b = (Integer[]) UnsafeSerializer.deserialize(ref);

    assertArrayEquals(a, b);
  }
  
  @Test
  public void testGenericArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Object[] a = new Integer[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    long ref = UnsafeSerializer.serialize(a, 2);
    Object[] b = (Object[]) UnsafeSerializer.deserialize(ref);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void testGenericArrayRandomized() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Object[] a = new Object[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextBoolean() ? new Bean1().randomize() : new Bean3().randomize();
    
    long ref = UnsafeSerializer.serialize(a, 2);
    Object[] b = (Object[]) UnsafeSerializer.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
  }
  
  @Test
  public void testGenericArray2() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0[] a = new Bean0[r.nextInt(3) + 1];
    for (int i = 0; i < a.length; i++) a[i] = new Bean1().randomize();
    
    long ref = UnsafeSerializer.serialize(a, 2);
    Bean0[] b = (Bean0[]) UnsafeSerializer.deserialize(ref, false);
    UnsafeSerializer.free(ref);
    
    assertTrue(Common.array_equal(a, b));
  }
}
