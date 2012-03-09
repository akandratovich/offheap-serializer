package org.machariel.core.manager;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.machariel.core.manager.ArraySerializationManager.Raw;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class ReferenceSerializationManager<A> {
	private final Class<A> type;
	private final int size;
	private final ClassMap map;
	
	@SuppressWarnings("unchecked")
  public static <T> ReferenceSerializationManager<T> acquire(Class<T> cl) {
	  synchronized (cl) {
	    ReferenceSerializationManager<?> sm = bucket.get(cl);
      if (sm == null) {
        sm = new ReferenceSerializationManager<T>(cl);
        bucket.put(cl, sm);
      }
      
      return (ReferenceSerializationManager<T>) sm;
    }
	}
	
	@SuppressWarnings("unchecked")
  public static <T> ReferenceSerializationManager<T> acquire(T obj) {
	  Class<T> cl = (Class<T>) obj.getClass();
	  synchronized (cl) {
	    ReferenceSerializationManager<?> sm = bucket.get(cl);
	    if (sm == null) {
	      sm = new ReferenceSerializationManager<T>(cl);
	      bucket.put(cl, sm);
	    }
	    
	    return (ReferenceSerializationManager<T>) sm;
	  }
	}
	
	private ReferenceSerializationManager(Class<A> cl) {
	  if (cl.isArray()) throw new IllegalArgumentException();
	  if (Reflection.isPrimitive(cl)) throw new IllegalArgumentException();
		type = cl;
		size = Reflection.size(cl);
		map = new ClassMap(cl);
	}
	
	private long serialize0(Object o, int level) {
    long ref = u.allocateMemory(size);
    long src = U.o2a(o);
    u.copyMemory(src + Reflection.MAGIC_SIZE, ref, size);
    
    int[] refi = map.referenceIndices();
    for (int i : refi) {
      if (level > 0) serialize1(ref, i, level);
      else u.setMemory(ref + map.offset(i) - Reflection.MAGIC_SIZE, 4, (byte) 0);
    }
    
    return ref;
	}
	
	@SuppressWarnings("unchecked")
  private static <Q> long serialize2(Object o, int deep, Class<Q> ct) {
	  ArraySerializationManager<Q> sm = ArraySerializationManager.acquire(Reflection.arrayClass(ct));
	  return sm.serialize((Q[]) o, deep);
	}
	
	private void serialize1(long base, int index, int level) {
	  long address = base + map.offset(index) - Reflection.MAGIC_SIZE;
    Object in = U.a2o(u.getAddress(address));
    if (in == null) return;
    
    Class<?> ftype = map.field(index).getType();
    if (ftype.isArray()) {
      Class<?> ct = ftype.getComponentType();
      if (Reflection.isPrimitive(ct)) u.putAddress(address, Raw.serialize(in));
      else u.putAddress(address, serialize2(in, level, ct));
    } else {
      ReferenceSerializationManager<?> sm = acquire(ftype);
      u.putAddress(address, sm.serialize0(in, level - 1));
    }
	}
	
	public long serialize(A o) {
	  return serialize(o, 0);
	}
	
	public long serialize(A o, int deep) {
	  return serialize0(o, deep);
	}
	
	public A deserialize(long ref) throws InstantiationException {
		return type.cast(deserialize0(ref));
	}
	
	private Object deserialize0(long ref) throws InstantiationException {
	  Object o = u.allocateInstance(type);
	  long dst = U.o2a(o);
	  
	  u.copyMemory(ref, dst + Reflection.MAGIC_SIZE, size);
	  
	  int[] refi = map.referenceIndices();
    for (int i : refi) {
      long offset = map.offset(i);
      long address = ref + offset - Reflection.MAGIC_SIZE;
      long in_ref = u.getAddress(address);
      if (in_ref != 0) u.putAddress(dst + offset, U.o2a(deserialize1(in_ref, dst + offset, map.field(i))));
    }
	  
	  return o;
	}
	
	private Object deserialize1(long in_ref, long dst, Field f) throws InstantiationException {
	  Class<?> ftype = f.getType();
    if (ftype.isArray()) {
      Class<?> ct = ftype.getComponentType();
      if (Reflection.isPrimitive(ct)) return Raw.deserialize(in_ref, ftype);
      else return ArraySerializationManager.acquire(Reflection.arrayClass(ct)).deserialize(in_ref);
    } else return acquire(f.getType()).deserialize0(in_ref);
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
  private static final Map<Class<?>, ReferenceSerializationManager<?>> bucket = new TreeMap<Class<?>, ReferenceSerializationManager<?>>(new Comparator<Class<?>>() {
    @Override
    public int compare(Class<?> o1, Class<?> o2) { return o1.getCanonicalName().compareTo(o2.getCanonicalName()); }});
}
