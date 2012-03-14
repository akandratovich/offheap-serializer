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
}