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
	
	public ClassMap getClassMap() {
	  return map;
	}
	
	public long getReference(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getReference(ref, i);
	}
	
	public boolean getBoolean(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getBoolean(ref, i);
	}
	
	public char getChar(long ref, String field) throws NoSuchFieldException {
	  int i = map.index(field);
	  return getChar(ref, i);
	}
	
	public byte getByte(long ref, String field) throws NoSuchFieldException {
	   int i = map.index(field);
     return getByte(ref, i);
	}
	
	private byte getByte0(long ref) throws NoSuchFieldException {
	  return u.getByte(ref);
	}
	
	public short getShort(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getShort(ref, i);
	}
	
	public int getInt(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getInt(ref, i);
	}
	
	public long getLong(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getLong(ref, i);
	}
	
	public float getFloat(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getFloat(ref, i);
	}
	
	public double getDouble(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getDouble(ref, i);
	}
	
  public void putReference(long ref, String field, long value) throws NoSuchFieldException {
    int i = map.index(field);
    putReference(ref, i, value);
  }
	
  public void putBoolean(long ref, String field, boolean value) throws NoSuchFieldException {
    int i = map.index(field);
    putBoolean(ref, i, value);
  }
	
	public void putChar(long ref, String field, char value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putChar(ref, i, value);
	}
	
	public void putByte(long ref, String field, byte value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putByte0(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	private void putByte0(long ref, byte value) throws NoSuchFieldException {
	  u.putByte(ref, value);
	}
	
	public void putShort(long ref, String field, short value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putShort(ref, i, value);
	}
	
	public void putInt(long ref, String field, int value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putInt(ref, i, value);
	}
	
	public void putLong(long ref, String field, long value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putLong(ref, i, value);
	}
	
	public void putFloat(long ref, String field, float value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putFloat(ref, i, value);
	}
	
	public void putDouble(long ref, String field, double value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putDouble(ref, i, value);
	}
	
	public long getReference(long ref, int i) throws NoSuchFieldException {
	  map.assertReference(i);
	  return u.getAddress(ref + map.offset(i) - PTR_DELTA);
	}
	
	public boolean getBoolean(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, boolean.class);
	  return getByte0(ref + map.offset(i) - PTR_DELTA) > 0;
	}
	
	public char getChar(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, char.class);
	  return u.getChar(ref + map.offset(i) - PTR_DELTA);
	}
	
	public byte getByte(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, byte.class);
	  return getByte0(ref + map.offset(i) - PTR_DELTA);
	}
	
	public short getShort(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, short.class);
	  return u.getShort(ref + map.offset(i) - PTR_DELTA);
	}
	
	public int getInt(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, int.class);
	  return u.getInt(ref + map.offset(i) - PTR_DELTA);
	}
	
	public long getLong(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, long.class);
	  return u.getLong(ref + map.offset(i) - PTR_DELTA);
	}
	
	public float getFloat(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, float.class);
	  return u.getFloat(ref + map.offset(i) - PTR_DELTA);
	}
	
	public double getDouble(long ref, int i) throws NoSuchFieldException {
	  map.assertType(i, double.class);
	  return u.getDouble(ref + map.offset(i) - PTR_DELTA);
	}
	
	public void putReference(long ref, int i, long value) throws NoSuchFieldException {
	  map.assertReference(i);
	  u.putAddress(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putBoolean(long ref, int i, boolean value) throws NoSuchFieldException {
	  map.assertType(i, boolean.class);
	  putByte0(ref + map.offset(i) - PTR_DELTA, (byte) (value ? 1 : 0));
	}
	
	public void putChar(long ref, int i, char value) throws NoSuchFieldException {
	  map.assertType(i, char.class);
	  u.putChar(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putByte(long ref, int i, byte value) throws NoSuchFieldException {
	  map.assertType(i, byte.class);
	  putByte0(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putShort(long ref, int i, short value) throws NoSuchFieldException {
	  map.assertType(i, short.class);
	  u.putShort(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putInt(long ref, int i, int value) throws NoSuchFieldException {
	  map.assertType(i, int.class);
	  u.putInt(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putLong(long ref, int i, long value) throws NoSuchFieldException {
	  map.assertType(i, long.class);
	  u.putLong(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putFloat(long ref, int i, float value) throws NoSuchFieldException {
	  map.assertType(i, float.class);
	  u.putFloat(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	public void putDouble(long ref, int i, double value) throws NoSuchFieldException {
	  map.assertType(i, double.class);
	  u.putDouble(ref + map.offset(i) - PTR_DELTA, value);
	}
	
	private static final Unsafe u = U.instance();
}
