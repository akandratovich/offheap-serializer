package org.machariel.core.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class Reflection {
	private Reflection() {}
	
	public static int size(Class<?> c) {
		int size = 0;
		
		Field[] dfs = getAllFields(c);
		for (Field f : dfs) size += _size(f.getType());
		
		return correctSize(size);
	}
	
	private static int correctSize(int size) {
		return size % 8 == 0 ? size : 8 * (size / 8 + 1);
	}
	
	private static int _size(Class<?> type) {
		if (isWrapper(type)) return WRAPPERS.get(type);
		return size(type);
	}
	
	public static Field[] getAllFields(Class<?> type) {
		Field[] fields = type.getDeclaredFields();
		Arrays.sort(fields, fieldComparator);
		
		Class<?> parent = type.getSuperclass();
		if (parent != null) {
			Field[] inherited = getAllFields(parent);
			int offset = fields.length;
			fields = Arrays.copyOf(fields, offset + inherited.length);
			System.arraycopy(inherited, 0, fields, offset, inherited.length);
		}
		
		return fields;
	}
	
	private static final Comparator<Field> fieldComparator = new Comparator<Field>() {
		@Override
		public int compare(Field o1, Field o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	private static boolean isWrapper(Class<?> type) {
		return WRAPPERS.containsKey(type);
	}
	
	@SuppressWarnings("serial")
	private static final Map<Class<?>, Integer> WRAPPERS = new HashMap<Class<?>, Integer>() {{
		put(Boolean.class, 1);
		put(boolean.class, 1);
		
		put(Character.class, 2);
		put(char.class, 2);
		
		put(Byte.class, 1);
		put(byte.class, 1);
		
		put(Short.class, 2);
		put(short.class, 2);
		
		put(Integer.class, 4);
		put(int.class, 4);
		
		put(Long.class, 8);
		put(long.class, 8);
		
		put(Float.class, 4);
		put(float.class, 4);
		
		put(Double.class, 8);
		put(double.class, 8);
		
//		put(Void.class, 0);
	}};
	
	
}