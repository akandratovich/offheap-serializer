package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.access.ArrayAccessor;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class ReferenceArray {
  private Random r = new Random();
  
  @Test
  public void intArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ArrayAccessor<Integer> sm = new ArrayAccessor<Integer>(Integer[].class, false);

    Integer[] a = new Integer[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();

    Key ref = Serializer.DIRECT.serialize(a, 5);

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
      Key vref = sm.getMember(ref, index);
      int value1 = (Integer) Serializer.DIRECT.deserialize(vref);
      assertEquals(value0, value1);
    }

    Integer[] b = (Integer[]) Serializer.DIRECT.deserialize(ref);

    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void testGenericArray() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Object[] a = new Integer[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    Key ref = Serializer.DIRECT.serialize(a, 2);
    Object[] b = (Object[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void testGenericArrayRandomized() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Object[] a = new Object[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextBoolean() ? new Bean1().randomize() : new Bean3().randomize();
    
    Key ref = Serializer.DIRECT.serialize(a, 3);
    Object[] b = (Object[]) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
    
    ref.free();
  }
  
  @Test
  public void testGenericArray2() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0[] a = new Bean0[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = new Bean1().randomize();
    
    Key ref = Serializer.DIRECT.serialize(a, 2);
    Bean0[] b = (Bean0[]) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
    
    ref.free();
  }
  
  @Test
  public void testString() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    String[] a = new String[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = Common.random(r.nextInt(50));
    
    Key ref = Serializer.DIRECT.serialize(a, 2);
    String[] b = (String[]) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.array_equal(a, b));
    
    ref.free();
  }
}
