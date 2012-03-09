package org.machariel.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.manager.ArraySerializationManager.Raw;

import sun.misc.Unsafe;

public class RawArray {
  private Random r = new Random();
  
//  @Test
  public void UnsafeDump() throws IllegalArgumentException, IllegalAccessException {
    for (Field f : Unsafe.class.getDeclaredFields()) {
      f.setAccessible(true);
      System.out.println(f.getName() + " " + f.get(Unsafe.class));
    }
  }
  
  @Test
  public void intArray() {
    int[] a = new int[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value = r.nextInt();
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value0 = a[index];
      int value1 = Raw.getInt(ref, index);
      assertEquals(value0, value1);
    }
    
    int[] b = Raw.deserialize(ref, int[].class);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void booleanArray() {
    boolean[] a = new boolean[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextBoolean();
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      boolean value = r.nextBoolean();
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      boolean value0 = a[index];
      boolean value1 = Raw.getBoolean(ref, index);
      assertEquals(value0, value1);
    }
    
    boolean[] b = Raw.deserialize(ref, boolean[].class);
    
    for (int i = 0; i < a.length; i++) assertEquals(a[i], b[i]);
  }
  
  @Test
  public void charArray() {
    char[] a = new char[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = (char) r.nextInt(255);
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      char value = (char) r.nextInt(255);
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      char value0 = a[index];
      char value1 = Raw.getChar(ref, index);
      assertEquals(value0, value1);
    }
    
    char[] b = Raw.deserialize(ref, char[].class);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void byteArray() {
    byte[] a = new byte[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = (byte) r.nextInt(255);
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      byte value = (byte) r.nextInt(255);
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      byte value0 = a[index];
      byte value1 = Raw.getByte(ref, index);
      assertEquals(value0, value1);
    }
    
    byte[] b = Raw.deserialize(ref, byte[].class);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void shortArray() {
    short[] a = new short[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = (short) r.nextInt(255);
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      short value = (short) r.nextInt(255);
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      short value0 = a[index];
      short value1 = Raw.getShort(ref, index);
      assertEquals(value0, value1);
    }
    
    short[] b = Raw.deserialize(ref, short[].class);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void longArray() {
    long[] a = new long[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextLong();
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      long value = r.nextLong();
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      long value0 = a[index];
      long value1 = Raw.getLong(ref, index);
      assertEquals(value0, value1);
    }
    
    long[] b = Raw.deserialize(ref, long[].class);
    
    assertArrayEquals(a, b);
  }
  
  @Test
  public void floatArray() {
    float[] a = new float[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextFloat();
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      float value = r.nextFloat();
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      float value0 = a[index];
      float value1 = Raw.getFloat(ref, index);
      assertEquals(value0, value1, Float.MIN_VALUE);
    }
    
    float[] b = Raw.deserialize(ref, float[].class);
    
    assertArrayEquals(a, b, Float.MIN_VALUE);
  }
  
  @Test
  public void doubleArray() {
    double[] a = new double[r.nextInt(100)];
    for (int i = 0; i < a.length; i++) a[i] = r.nextDouble();
    
    long ref = Raw.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      double value = r.nextDouble();
      a[index] = value;
      Raw.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      double value0 = a[index];
      double value1 = Raw.getDouble(ref, index);
      assertEquals(value0, value1, 0.1);
    }
    
    double[] b = Raw.deserialize(ref, double[].class);
    
    assertArrayEquals(a, b, 0.1);
  }
}
