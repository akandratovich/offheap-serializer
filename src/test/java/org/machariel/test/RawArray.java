package org.machariel.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.access.ArrayAccessor.Raw;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;

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
  public void intArray() throws InstantiationException {
    int[] a = new int[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value = r.nextInt();
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      int value0 = a[index];
      int value1 = Raw.DIRECT.getInt(ref, index);
      assertEquals(value0, value1);
    }
    
    int[] b = (int[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void booleanArray() throws InstantiationException {
    boolean[] a = new boolean[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextBoolean();
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      boolean value = r.nextBoolean();
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      boolean value0 = a[index];
      boolean value1 = Raw.DIRECT.getBoolean(ref, index);
      assertEquals(value0, value1);
    }
    
    boolean[] b = (boolean[]) Serializer.DIRECT.deserialize(ref);
    
    for (int i = 0; i < a.length; i++) assertEquals(a[i], b[i]);
    
    ref.free();
  }
  
  @Test
  public void charArray() throws InstantiationException {
    char[] a = new char[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = (char) r.nextInt(255);
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      char value = (char) r.nextInt(255);
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      char value0 = a[index];
      char value1 = Raw.DIRECT.getChar(ref, index);
      assertEquals(value0, value1);
    }
    
    char[] b = (char[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void byteArray() throws InstantiationException {
    byte[] a = new byte[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = (byte) r.nextInt(255);
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      byte value = (byte) r.nextInt(255);
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      byte value0 = a[index];
      byte value1 = Raw.DIRECT.getByte(ref, index);
      assertEquals(value0, value1);
    }
    
    byte[] b = (byte[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void shortArray() throws InstantiationException {
    short[] a = new short[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = (short) r.nextInt(255);
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      short value = (short) r.nextInt(255);
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      short value0 = a[index];
      short value1 = Raw.DIRECT.getShort(ref, index);
      assertEquals(value0, value1);
    }
    
    short[] b = (short[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void longArray() throws InstantiationException {
    long[] a = new long[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextLong();
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      long value = r.nextLong();
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      long value0 = a[index];
      long value1 = Raw.DIRECT.getLong(ref, index);
      assertEquals(value0, value1);
    }
    
    long[] b = (long[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b);
    
    ref.free();
  }
  
  @Test
  public void floatArray() throws InstantiationException {
    float[] a = new float[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextFloat();
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      float value = r.nextFloat();
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      float value0 = a[index];
      float value1 = Raw.DIRECT.getFloat(ref, index);
      assertEquals(value0, value1, Float.MIN_VALUE);
    }
    
    float[] b = (float[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b, Float.MIN_VALUE);
    
    ref.free();
  }
  
  @Test
  public void doubleArray() throws InstantiationException {
    double[] a = new double[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextDouble();
    
    Key ref = Serializer.DIRECT.serialize(a);
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      double value = r.nextDouble();
      a[index] = value;
      Raw.DIRECT.put(ref, index, value);
    }
    
    for (int i = 0; i < 10; i++) {
      int index = r.nextInt(a.length);
      double value0 = a[index];
      double value1 = Raw.DIRECT.getDouble(ref, index);
      assertEquals(value0, value1, 0.1);
    }
    
    double[] b = (double[]) Serializer.DIRECT.deserialize(ref);
    
    assertArrayEquals(a, b, 0.1);
    
    ref.free();
  }
}
