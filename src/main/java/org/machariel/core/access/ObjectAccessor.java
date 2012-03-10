package org.machariel.core.access;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.machariel.core.ClassMap;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class ObjectAccessor<A> {
	private final ClassMap map;
	
	@SuppressWarnings("unchecked")
  public static <T> ObjectAccessor<T> acquire(Class<T> cl) {
	  synchronized (cl) {
	    ObjectAccessor<?> sm = bucket.get(cl);
      if (sm == null) {
        sm = new ObjectAccessor<T>(cl);
        bucket.put(cl, sm);
      }
      
      return (ObjectAccessor<T>) sm;
    }
	}
	
	@SuppressWarnings("unchecked")
  public static <T> ObjectAccessor<T> acquire(T obj) {
	  return acquire((Class<T>) obj.getClass());
	}
	
	private ObjectAccessor(Class<A> cl) {
	  if (cl.isArray()) throw new IllegalArgumentException();
	  if (Reflection.isPrimitive(cl)) throw new IllegalArgumentException();
		map = new ClassMap(cl);
	}
	
	public long getReference(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertReference(i);
    return u.getAddress(ref + map.offset(i));
	}
	
	public boolean getBoolean(long ref, String field) throws NoSuchFieldException {
	  return getByte(ref, field) > 0;
	}
	
	public char getChar(long ref, String field) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, char.class);
	  return u.getChar(ref + map.offset(i));
	}
	
	public byte getByte(long ref, String field) throws NoSuchFieldException {
	   int i = map.index(field);
     map.assertType(i, byte.class);
     return u.getByte(ref + map.offset(i));
	}
	
	public short getShort(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, short.class);
    return u.getShort(ref + map.offset(i));
	}
	
	public int getInt(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, int.class);
    return u.getInt(ref + map.offset(i));
	}
	
	public long getLong(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, long.class);
    return u.getLong(ref + map.offset(i));
	}
	
	public float getFloat(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, float.class);
    return u.getFloat(ref + map.offset(i));
	}
	
	public double getDouble(long ref, String field) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertType(i, double.class);
    return u.getDouble(ref + map.offset(i));
	}
	
  public void putReference(long ref, String field, long value) throws NoSuchFieldException {
    int i = map.index(field);
    map.assertReference(i);
    u.putAddress(ref + map.offset(i), value);
  }
	
	public void putBoolean(long ref, String field, boolean value) throws NoSuchFieldException {
	  putByte(ref, field, (byte) (value ? 1 : 0));
	}
	
	public void putChar(long ref, String field, char value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, char.class);
	  u.putChar(ref + map.offset(i), value);
	}
	
	public void putByte(long ref, String field, byte value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, byte.class);
	  u.putByte(ref + map.offset(i), value);
	}
	
	public void putShort(long ref, String field, short value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, short.class);
	  u.putShort(ref + map.offset(i), value);
	}
	
	public void putInt(long ref, String field, int value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, int.class);
	  u.putInt(ref + map.offset(i), value);
	}
	
	public void putLong(long ref, String field, long value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, long.class);
	  u.putLong(ref + map.offset(i), value);
	}
	
	public void putFloat(long ref, String field, float value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, float.class);
	  u.putFloat(ref + map.offset(i), value);
	}
	
	public void putDouble(long ref, String field, double value) throws NoSuchFieldException {
	  int i = map.index(field);
	  map.assertType(i, double.class);
	  u.putDouble(ref + map.offset(i), value);
	}
	
	private static final Unsafe u = U.instance();
  private static final Map<Class<?>, ObjectAccessor<?>> bucket = new TreeMap<Class<?>, ObjectAccessor<?>>(new Comparator<Class<?>>() {
    public int compare(Class<?> o1, Class<?> o2) { return o1.getCanonicalName().compareTo(o2.getCanonicalName()); }});
}
