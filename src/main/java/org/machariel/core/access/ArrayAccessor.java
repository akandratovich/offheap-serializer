package org.machariel.core.access;

import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class ArrayAccessor<A> {
  private final int scale;
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
    if (cl.isPrimitive()) throw new IllegalArgumentException();
	  
	  scale = cl.isPrimitive() ? u.arrayIndexScale(array) : Unsafe.ADDRESS_SIZE;
		sm = new ObjectAccessor<A>((Class<A>) array.getComponentType());
	}
	
  public long getReference(long ref, int index) {
    return u.getAddress(ref + UnsafeSerializer.PTR_OFFSET + index * scale);
  }
  
  public void putReference(long ref, int index, long value) {
    u.putAddress(ref + UnsafeSerializer.PTR_OFFSET + index * scale, value);
  }
  
  @SuppressWarnings("unchecked")
  public A get(long ref, int index) throws InstantiationException {
    return (A) UnsafeSerializer.deserialize(u.getAddress(ref + UnsafeSerializer.PTR_OFFSET + index * scale), false);
  }
  
  public void put(long ref, int index, A value) {
    put(ref, index, value, 0);
  }
  
  public void put(long ref, int index, A value, int deep) {
    u.putAddress(ref + UnsafeSerializer.PTR_OFFSET + index * scale, UnsafeSerializer.serialize(value, deep));
  }
  
	public static class Raw {
    public static void put(long ref, int index, int value) {
      u.putInt(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_INT_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, short value) {
      u.putShort(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_SHORT_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, long value) {
      u.putLong(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_LONG_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, char value) {
      u.putChar(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_CHAR_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, byte value) {
      u.putByte(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_BYTE_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, boolean value) {
      u.putByte(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_BOOLEAN_INDEX_SCALE, (byte) (value ? 1 : 0));
    }
    
    public static void put(long ref, int index, double value) {
      u.putDouble(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_DOUBLE_INDEX_SCALE, value);
    }
    
    public static void put(long ref, int index, float value) {
      u.putFloat(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_FLOAT_INDEX_SCALE, value);
    }
    
    public static int getInt(long ref, int index) {
      return u.getInt(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_INT_INDEX_SCALE);
    }
    
    public static boolean getBoolean(long ref, int index) {
      return u.getByte(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_BOOLEAN_INDEX_SCALE) > 0;
    }
    
    public static byte getByte(long ref, int index) {
      return u.getByte(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_BYTE_INDEX_SCALE);
    }
    
    public static char getChar(long ref, int index) {
      return u.getChar(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_CHAR_INDEX_SCALE);
    }
    
    public static double getDouble(long ref, int index) {
      return u.getDouble(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_DOUBLE_INDEX_SCALE);
    }
    
    public static float getFloat(long ref, int index) {
      return u.getFloat(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_FLOAT_INDEX_SCALE);
    }
    
    public static short getShort(long ref, int index) {
      return u.getShort(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_SHORT_INDEX_SCALE);
    }
    
    public static long getLong(long ref, int index) {
      return u.getLong(ref + UnsafeSerializer.PTR_OFFSET + index * Unsafe.ARRAY_LONG_INDEX_SCALE);
    }
	}
	
	private static final Unsafe u = U.instance();
}
