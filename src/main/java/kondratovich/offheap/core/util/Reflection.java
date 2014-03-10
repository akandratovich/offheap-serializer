package kondratovich.offheap.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.Unsafe;

public final class Reflection {
  private static final Unsafe u = U.instance();
  public static final int OOP_SIZE = u.arrayIndexScale(Object[].class);
  public static final int ADDRESS_SIZE = u.addressSize();
  public static final long MAGIC_SIZE = ADDRESS_SIZE + OOP_SIZE;
  
  public static int indexScale(Class<?> type) {
    return u.arrayIndexScale(type);
  }
  
  public static int baseOffset(Class<?> type) {
    return u.arrayBaseOffset(type);
  }
  
  /** The value of {@code arrayIndexScale(boolean[].class)} */
  public static final int ARRAY_BOOLEAN_INDEX_SCALE = u.arrayIndexScale(boolean[].class);
  /** The value of {@code arrayIndexScale(byte[].class)} */
  public static final int ARRAY_BYTE_INDEX_SCALE = u.arrayIndexScale(byte[].class);
  /** The value of {@code arrayIndexScale(short[].class)} */
  public static final int ARRAY_SHORT_INDEX_SCALE = u.arrayIndexScale(short[].class);
  /** The value of {@code arrayIndexScale(char[].class)} */
  public static final int ARRAY_CHAR_INDEX_SCALE = u.arrayIndexScale(char[].class);
  /** The value of {@code arrayIndexScale(int[].class)} */
  public static final int ARRAY_INT_INDEX_SCALE = u.arrayIndexScale(int[].class);
  /** The value of {@code arrayIndexScale(long[].class)} */
  public static final int ARRAY_LONG_INDEX_SCALE = u.arrayIndexScale(long[].class);
  /** The value of {@code arrayIndexScale(float[].class)} */
  public static final int ARRAY_FLOAT_INDEX_SCALE = u.arrayIndexScale(float[].class);
  /** The value of {@code arrayIndexScale(double[].class)} */
  public static final int ARRAY_DOUBLE_INDEX_SCALE = u.arrayIndexScale(double[].class);
  /** The value of {@code arrayIndexScale(Object[].class)} */
  public static final int ARRAY_OBJECT_INDEX_SCALE = u.arrayIndexScale(Object[].class);
  
	private Reflection() {}
	
	public static long getOopAddress(long ref) {
	  return OOP_SIZE == 8 ? u.getLong(ref) : u.getInt(ref);
	}
	
	public static long getOopAddress(Object o, long offset) {
	  return OOP_SIZE == 8 ? u.getLong(o, offset) : u.getInt(o, offset);
	}
	
	public static int size(Object o) {
	  if (o.getClass().isPrimitive()) return SCALE.get(o.getClass());
	  return u.getInt(normalize(getOopAddress(o, (long) ADDRESS_SIZE)) + 3 * ADDRESS_SIZE);
	}
	
	public static int arraySize(Object o) {
	  return u.getInt(o, Reflection.MAGIC_SIZE);
	}
	
  public static long normalize(long value) {
    if (value >= 0) return value;
    return (~0L >>> 32) & value;
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
		put(boolean.class, u.arrayIndexScale(boolean[].class));
		put(char.class, u.arrayIndexScale(char[].class));
		put(byte.class, u.arrayIndexScale(byte[].class));
		put(short.class, u.arrayIndexScale(short[].class));
		put(int.class, u.arrayIndexScale(int[].class));
		put(long.class, u.arrayIndexScale(long[].class));
		put(float.class, u.arrayIndexScale(float[].class));
		put(double.class, u.arrayIndexScale(double[].class));
	}};
}