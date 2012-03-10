package org.machariel.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;
import org.machariel.core.serialization.ArraySerializationManager;

public class GenericArray {
  private Random r = new Random();
  
  @Test
  public void integerArray() throws InstantiationException {
    Integer[] a = new Integer[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    ArraySerializationManager<Integer> sm = ArraySerializationManager.acquire(Integer[].class);
    
    long ref = sm.serialize(a);
    Integer[] b = sm.deserialize(ref);
    
    assertArrayEquals(a, b);
  }
}
