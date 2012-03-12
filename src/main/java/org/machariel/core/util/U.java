package org.machariel.core.util;

import sun.misc.Unsafe;

public final class U {
	private U() {}
	
	private static final sun.misc.Unsafe u = acquire();
	
	public static sun.misc.Unsafe instance() { return u; }
	
	private static sun.misc.Unsafe acquire() {
		sun.misc.Unsafe unsafe = null;
		try {
			java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (sun.misc.Unsafe) field.get(null);
		} catch (Exception e) { throw new AssertionError(e); }
		return unsafe;
	}
	
	public static long o2a(Object o) {
	  return u.getLong(new Object[] {o}, 3L * Unsafe.ADDRESS_SIZE);
	}
	
	public static Object a2o(long address) {
		Object[] ar = new Object[1];
		u.putLong(ar, 3L * Unsafe.ADDRESS_SIZE, (int) address);
		return ar[0];
	}
//	
	public static boolean klass(long ptr) {
	  return
	      u.getAddress(Reflection.klassPtr(r) + Unsafe.ADDRESS_SIZE) == u.getAddress(ptr + Unsafe.ADDRESS_SIZE) ||
	      u.getAddress(Reflection.klassPtr(ra) + Unsafe.ADDRESS_SIZE) == u.getAddress(ptr + Unsafe.ADDRESS_SIZE) ||
	      u.getAddress(Reflection.klassPtr(pa) + Unsafe.ADDRESS_SIZE) == u.getAddress(ptr + Unsafe.ADDRESS_SIZE);
	}
	
	public static Class<?> clazz(long ptr) {
	  if (!klass(ptr)) throw new IllegalArgumentException();
	  return (Class<?>) U.a2o(u.getAddress(ptr + 16 * Unsafe.ADDRESS_SIZE));
	}
	
	private static final int[] pa = new int[0];
	private static final Integer[] ra = new Integer[0];
	private static final Integer r = new Integer(9);
}