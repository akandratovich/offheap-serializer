package org.machariel.core.access;

import java.lang.reflect.Array;

import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class ArrayAccessor<A> {
  private final int scale;
  private final int offset;
	private final ObjectAccessor<A> sm;
	
	public ObjectAccessor<A> getMemberAccessor() {
	  return sm;
	}
	
  public static <A> ArrayAccessor<A> acquire(Class<A[]> array) {
	  return new ArrayAccessor<A>(array);
	}
  
  @SuppressWarnings("unchecked")
  public static <A> ArrayAccessor<A> acquire(A[] obj) {
    return acquire((Class<A[]>) obj.getClass());
  }
	
  @SuppressWarnings("unchecked")
  private ArrayAccessor(Class<A[]> array) {
    Class<A> cl = (Class<A>) array.getComponentType();
    if (Reflection.isPrimitive(cl)) throw new IllegalArgumentException();
	  
	  scale = u.arrayIndexScale(array);
	  offset = u.arrayBaseOffset(array);
	  
		sm = ObjectAccessor.acquire((Class<A>) array.getComponentType());
	}
	
  public long getReference(long ref, int index) {
    return u.getAddress(ref + offset + index * scale - Reflection.MAGIC_SIZE);
  }
  
  public void putReference(long ref, int index, long value) {
    u.putAddress(ref + offset + index * scale - Reflection.MAGIC_SIZE, value);
  }
  
  public A get(long ref, int index) throws InstantiationException {
    return sm.deserialize(u.getAddress(ref + offset + index * scale - Reflection.MAGIC_SIZE));
  }
  
  public void put(long ref, int index, A value) {
    put(ref, index, value, 0);
  }
  
  public void put(long ref, int index, A value, int deep) {
    u.putAddress(ref + offset + index * scale - Reflection.MAGIC_SIZE, sm.serialize(value, deep));
  }
  
	public static class Raw {
    public static void put(long ref, int index, int value) {
      u.putInt(ref + Unsafe.ARRAY_INT_BASE_OFFSET + index * Unsafe.ARRAY_INT_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, short value) {
      u.putShort(ref + Unsafe.ARRAY_SHORT_BASE_OFFSET + index * Unsafe.ARRAY_SHORT_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, long value) {
      u.putLong(ref + Unsafe.ARRAY_LONG_BASE_OFFSET + index * Unsafe.ARRAY_LONG_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, char value) {
      u.putChar(ref + Unsafe.ARRAY_CHAR_BASE_OFFSET + index * Unsafe.ARRAY_CHAR_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, byte value) {
      u.putByte(ref + Unsafe.ARRAY_BYTE_BASE_OFFSET + index * Unsafe.ARRAY_BYTE_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, boolean value) {
      u.putByte(ref + Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + index * Unsafe.ARRAY_BOOLEAN_INDEX_SCALE - Reflection.MAGIC_SIZE, (byte) (value ? 1 : 0));
    }
    
    public static void put(long ref, int index, double value) {
      u.putDouble(ref + Unsafe.ARRAY_DOUBLE_BASE_OFFSET + index * Unsafe.ARRAY_DOUBLE_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static void put(long ref, int index, float value) {
      u.putFloat(ref + Unsafe.ARRAY_FLOAT_BASE_OFFSET + index * Unsafe.ARRAY_FLOAT_INDEX_SCALE - Reflection.MAGIC_SIZE, value);
    }
    
    public static int getInt(long ref, int index) {
      return u.getInt(ref + Unsafe.ARRAY_INT_BASE_OFFSET + index * Unsafe.ARRAY_INT_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static boolean getBoolean(long ref, int index) {
      return u.getByte(ref + Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + index * Unsafe.ARRAY_BOOLEAN_INDEX_SCALE - Reflection.MAGIC_SIZE) > 0;
    }
    
    public static byte getByte(long ref, int index) {
      return u.getByte(ref + Unsafe.ARRAY_BYTE_BASE_OFFSET + index * Unsafe.ARRAY_BYTE_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static char getChar(long ref, int index) {
      return u.getChar(ref + Unsafe.ARRAY_CHAR_BASE_OFFSET + index * Unsafe.ARRAY_CHAR_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static double getDouble(long ref, int index) {
      return u.getDouble(ref + Unsafe.ARRAY_DOUBLE_BASE_OFFSET + index * Unsafe.ARRAY_DOUBLE_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static float getFloat(long ref, int index) {
      return u.getFloat(ref + Unsafe.ARRAY_FLOAT_BASE_OFFSET + index * Unsafe.ARRAY_FLOAT_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static short getShort(long ref, int index) {
      return u.getShort(ref + Unsafe.ARRAY_SHORT_BASE_OFFSET + index * Unsafe.ARRAY_SHORT_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
    
    public static long getLong(long ref, int index) {
      return u.getLong(ref + Unsafe.ARRAY_LONG_BASE_OFFSET + index * Unsafe.ARRAY_LONG_INDEX_SCALE - Reflection.MAGIC_SIZE);
    }
	}
	
	private static final Unsafe u = U.instance();
}
