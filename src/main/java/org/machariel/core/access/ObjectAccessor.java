package org.machariel.core.access;

import org.machariel.core.ClassBucket;
import org.machariel.core.ClassMap;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class ObjectAccessor<A> {
  private static final long PTR_DELTA = Reflection.MAGIC_SIZE - UnsafeSerializer.PTR_OFFSET;
	private final ClassMap map;
	
	@SuppressWarnings("unchecked")
  public ObjectAccessor(A obj) {
	  this((Class<A>) obj.getClass());
	}
	
	public ObjectAccessor(Class<A> cl) {
	  if (cl.isArray()) throw new IllegalArgumentException();
	  if (cl.isPrimitive()) throw new IllegalArgumentException();
		map = ClassBucket.acquireMap(cl);
	}
	
	public long getReference(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertReference(i);
    return u.getAddress(ref + map.offset(i) - PTR_DELTA);
	}
	
	public boolean getBoolean(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, boolean.class);
    return getByte0(ref + map.offset(i) - PTR_DELTA) > 0;
	}
	
	public char getChar(long ref, String field) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, char.class);
	  return u.getChar(ref + map.offset(i) - PTR_DELTA);
	}
	
	public byte getByte(long ref, String field) throws NoSuchFieldException {
	   int i = map.index(field);
     map.assertType(i, byte.class);
     return getByte0(ref + map.offset(i) - PTR_DELTA);
	}
	
	private byte getByte0(long ref) throws NoSuchFieldException {
	  return u.getByte(ref);
	}
	
	public short getShort(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, short.class);
    return u.getShort(ref + map.offset(i) - PTR_DELTA);
	}
	
	public int getInt(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, int.class);
    return u.getInt(ref + map.offset(i) - PTR_DELTA);
	}
	
	public long getLong(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, long.class);
    return u.getLong(ref + map.offset(i) - PTR_DELTA);
	}
	
	public float getFloat(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, float.class);
    return u.getFloat(ref + map.offset(i) - PTR_DELTA);
	}
	
	public double getDouble(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, double.class);
    return u.getDouble(ref + map.offset(i) - PTR_DELTA);
	}
	
  public void putReference(long ref, String field, long value) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertReference(i);
    u.putAddress(ref + map.offset(i) - PTR_DELTA, value);
  }
	
  public void putBoolean(long ref, String field, boolean value) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, boolean.class);
    putByte0(ref + map.offset(i) - PTR_DELTA, (byte) (value ? 1 : 0));
  }
	
	public void putChar(long ref, String field, char value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, char.class);
	  u.putChar(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putByte(long ref, String field, byte value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, byte.class);
	  putByte0(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	private void putByte0(long ref, byte value) throws NoSuchFieldException {
	  u.putByte(ref, value);
	}
	
	public void putShort(long ref, String field, short value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, short.class);
	  u.putShort(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putInt(long ref, String field, int value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, int.class);
	  u.putInt(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putLong(long ref, String field, long value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, long.class);
	  u.putLong(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putFloat(long ref, String field, float value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, float.class);
	  u.putFloat(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putDouble(long ref, String field, double value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, double.class);
	  u.putDouble(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	private static final Unsafe u = U.instance();
}
