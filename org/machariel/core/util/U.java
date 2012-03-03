package org.machariel.core.util;

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
		return u.getLong(new Object[] {o}, 12L);
	}
	
	public synchronized static Object a2o(long address) {
		Object[] ar = new Object[1];
		u.putLong(ar, 12L, address);
		return ar[0];
	}
}