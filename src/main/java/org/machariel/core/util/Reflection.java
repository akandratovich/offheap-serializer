package org.machariel.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.Unsafe;

public final class Reflection {
  public static final int MAGIC_SIZE = 2 * Unsafe.ADDRESS_SIZE;
  private static final Unsafe u = U.instance();
  
	private Reflection() {}
	
	public static int size(Class<?> c) {
	  if (isPrimitive(c)) return SCALE.get(c);
	  
	  int size = 0;
	  
    Class<?> parent = c.getSuperclass();
    if (parent != null) size = size(parent);
    
    for (Field f : c.getDeclaredFields())
      if (!(Modifier.isStatic(f.getModifiers()) || f.isSynthetic()))
        size += u.arrayIndexScale(arrayClass(f.getType()));
    
		return correctSize(size);
	}
	
	public static int size(Object o) {
	  if (isPrimitive(o.getClass())) return SCALE.get(o.getClass());
	  return u.getInt(klassPtr(o) + 3 * Unsafe.ADDRESS_SIZE);
	}
	
	public static long klassPtr(Object o) {
	  return u.getAddress(U.o2a(o) + Unsafe.ADDRESS_SIZE);
	}
	
	@SuppressWarnings("unchecked")
  public static <A> Class<A[]> arrayClass(Class<A> type) {
	  return (Class<A[]>) Array.newInstance(type, 0).getClass();
	}
	
	private static int correctSize(int size) {
		return size % 8 == 0 ? size : 8 * (size / 8 + 1);
	}
	
	public static List<Field> getAllFields(Class<?> type) {
	  List<Field> result = new ArrayList<Field>();
		Field[] fields = type.getDeclaredFields();
		
		for (Field f : fields)
		  if (!(Modifier.isStatic(f.getModifiers()) || f.isSynthetic()))
		    result.add(f);
		
		Class<?> parent = type.getSuperclass();
		if (parent != null) result.addAll(getAllFields(parent));
		
		return result;
	}
	
	public static boolean isPrimitive(Class<?> type) {
		return SCALE.containsKey(type);
	}
	
	public static String name0(Field f, Class<?> domain) {
	  if (Modifier.isPrivate(f.getModifiers())) return f.getDeclaringClass().getCanonicalName() + "#" + f.getName();
	  if (overrided0(f, domain)) return f.getDeclaringClass().getCanonicalName() + "#" + f.getName();
	  
	  return f.getName();
	}
	
	private static boolean overrided0(Field f, Class<?> middle) {
	  if (middle == f.getDeclaringClass()) return false;
	  Field mf = getField(f.getName(), middle);
	  if (mf != null && (Modifier.isPublic(mf.getModifiers()) || Modifier.isProtected(mf.getModifiers()))) return true;
	  
	  return overrided0(f, middle.getSuperclass());
	}
	
	public static Field getField(String fname, Class<?> type) {
	  try {
	    return type.getDeclaredField(fname);
	  } catch (NoSuchFieldException e) {
	    return null;
    }
	}
	
	@SuppressWarnings("serial")
	private static final Map<Class<?>, Integer> SCALE = new HashMap<Class<?>, Integer>() {{
		put(boolean.class, Unsafe.ARRAY_BOOLEAN_INDEX_SCALE);
		put(char.class, Unsafe.ARRAY_CHAR_INDEX_SCALE);
		put(byte.class, Unsafe.ARRAY_BYTE_INDEX_SCALE);
		put(short.class, Unsafe.ARRAY_SHORT_INDEX_SCALE);
		put(int.class, Unsafe.ARRAY_INT_INDEX_SCALE);
		put(long.class, Unsafe.ARRAY_LONG_INDEX_SCALE);
		put(float.class, Unsafe.ARRAY_FLOAT_INDEX_SCALE);
		put(double.class, Unsafe.ARRAY_DOUBLE_INDEX_SCALE);
	}};
}