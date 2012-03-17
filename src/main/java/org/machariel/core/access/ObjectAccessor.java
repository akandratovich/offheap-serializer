package org.machariel.core.access;

import org.machariel.core.ClassBucket;
import org.machariel.core.ClassMap;
import org.machariel.core.allocator.Allocator;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.core.util.Reflection;


public class ObjectAccessor<A> {
  private static final long PTR_DELTA = Reflection.MAGIC_SIZE - Serializer.PTR_OFFSET;
	private final ClassMap map;
	private final Allocator allocator;
	
	public ObjectAccessor(A obj) {
	  this(obj, true);
	}
	
	@SuppressWarnings("unchecked")
  public ObjectAccessor(A obj, boolean checked) {
	  this((Class<A>) obj.getClass(), checked);
	}
	
	public ObjectAccessor(Class<A> cl) {
	  this(cl, true);
	}
	
	public ObjectAccessor(Class<A> cl, boolean checked) {
	  if (cl.isArray()) throw new IllegalArgumentException();
	  if (cl.isPrimitive()) throw new IllegalArgumentException();
		map = ClassBucket.acquireMap(cl);
		allocator = checked ? Allocator.CHECKED : Allocator.DIRECT;
	}
	
	public ClassMap getClassMap() {
	  return map;
	}
	
	public Key getMember(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getMember(ref, i);
	}
	
	public boolean getBoolean(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getBoolean(ref, i);
	}
	
	public char getChar(Key ref, String field) throws NoSuchFieldException {
	  int i = map.index(field);
	  return getChar(ref, i);
	}
	
	public byte getByte(Key ref, String field) throws NoSuchFieldException {
	   int i = map.index(field);
     return getByte(ref, i);
	}
	
	private byte getByte0(Key ref, long offset) {
	  return allocator.getByte(ref, offset);
	}
	
	public short getShort(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getShort(ref, i);
	}
	
	public int getInt(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getInt(ref, i);
	}
	
	public long getLong(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getLong(ref, i);
	}
	
	public float getFloat(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getFloat(ref, i);
	}
	
	public double getDouble(Key ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    return getDouble(ref, i);
	}
	
  public void putMember(Key ref, String field, Key value) throws NoSuchFieldException {
    int i = map.index(field);
    putMember(ref, i, value);
  }
	
  public void putBoolean(Key ref, String field, boolean value) throws NoSuchFieldException {
    int i = map.index(field);
    putBoolean(ref, i, value);
  }
	
	public void putChar(Key ref, String field, char value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putChar(ref, i, value);
	}
	
	public void putByte(Key ref, String field, byte value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putByte0(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	private void putByte0(Key ref, long offset, byte value) {
	  allocator.put(ref, offset, value);
	}
	
	public void putShort(Key ref, String field, short value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putShort(ref, i, value);
	}
	
	public void putInt(Key ref, String field, int value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putInt(ref, i, value);
	}
	
	public void putLong(Key ref, String field, long value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putLong(ref, i, value);
	}
	
	public void putFloat(Key ref, String field, float value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putFloat(ref, i, value);
	}
	
	public void putDouble(Key ref, String field, double value) throws NoSuchFieldException {
	  int i = map.index(field);
	  putDouble(ref, i, value);
	}
	
	public Key getMember(Key ref, int i) {
	  map.assertReference(i);
	  for (int j = 0; j < map.refs().length; j++) if (i == map.refs()[j]) return ref.member(i);
	  return null;
	}
	
	public boolean getBoolean(Key ref, int i) {
	  map.assertType(i, boolean.class);
	  return getByte0(ref, map.offset(i) - PTR_DELTA) > 0;
	}
	
	public char getChar(Key ref, int i) {
	  map.assertType(i, char.class);
	  return allocator.getChar(ref, map.offset(i) - PTR_DELTA);
	}
	
	public byte getByte(Key ref, int i) {
	  map.assertType(i, byte.class);
	  return getByte0(ref, map.offset(i) - PTR_DELTA);
	}
	
	public short getShort(Key ref, int i) throws NoSuchFieldException {
	  map.assertType(i, short.class);
	  return allocator.getShort(ref, map.offset(i) - PTR_DELTA);
	}
	
	public int getInt(Key ref, int i) throws NoSuchFieldException {
	  map.assertType(i, int.class);
	  return allocator.getInt(ref, map.offset(i) - PTR_DELTA);
	}
	
	public long getLong(Key ref, int i) throws NoSuchFieldException {
	  map.assertType(i, long.class);
	  return allocator.getLong(ref, map.offset(i) - PTR_DELTA);
	}
	
	public float getFloat(Key ref, int i) throws NoSuchFieldException {
	  map.assertType(i, float.class);
	  return allocator.getFloat(ref, map.offset(i) - PTR_DELTA);
	}
	
	public double getDouble(Key ref, int i) throws NoSuchFieldException {
	  map.assertType(i, double.class);
	  return allocator.getDouble(ref, map.offset(i) - PTR_DELTA);
	}
	
	public void putMember(Key ref, int i, Key value) throws NoSuchFieldException {
	  map.assertReference(i);
	  for (int j = 0; j < map.refs().length; j++) if (i == map.refs()[j]) {
	    ref.member(i, value);
	    return;
	  }
	}
	
	public void putBoolean(Key ref, int i, boolean value) throws NoSuchFieldException {
	  map.assertType(i, boolean.class);
	  putByte0(ref, map.offset(i) - PTR_DELTA, (byte) (value ? 1 : 0));
	}
	
	public void putChar(Key ref, int i, char value) throws NoSuchFieldException {
	  map.assertType(i, char.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putByte(Key ref, int i, byte value) throws NoSuchFieldException {
	  map.assertType(i, byte.class);
	  putByte0(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putShort(Key ref, int i, short value) throws NoSuchFieldException {
	  map.assertType(i, short.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putInt(Key ref, int i, int value) throws NoSuchFieldException {
	  map.assertType(i, int.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putLong(Key ref, int i, long value) throws NoSuchFieldException {
	  map.assertType(i, long.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putFloat(Key ref, int i, float value) throws NoSuchFieldException {
	  map.assertType(i, float.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
	
	public void putDouble(Key ref, int i, double value) throws NoSuchFieldException {
	  map.assertType(i, double.class);
	  allocator.put(ref, map.offset(i) - PTR_DELTA, value);
	}
}
