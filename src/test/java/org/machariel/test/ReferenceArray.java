package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.serialization.ArraySerializationManager;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.util.Common;

public class ReferenceArray {
  private Random r = new Random();
  
  @Test
  public void intArray() throws InstantiationException {
    ArraySerializationManager<Integer> sm = ArraySerializationManager.acquire(Integer[].class);
    
    Integer[] a = new Integer[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    long ref = sm.serialize(a, 5);
    
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
    
    Integer[] b = sm.deserialize(ref);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void testGenericArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Object[] a = new Integer[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    ArraySerializationManager<Object> sm = ArraySerializationManager.acquire(a);
    
    long ref = sm.serialize(a);
    Object[] b = sm.deserialize(ref);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void testGenericArray2() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0[] a = new Bean0[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = new Bean1().randomize();
    
    ArraySerializationManager<Bean0> sm = ArraySerializationManager.acquire(a);
    
    long ref = sm.serialize(a);
    Bean0[] b = sm.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
  }
}
