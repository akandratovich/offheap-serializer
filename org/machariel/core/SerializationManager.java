package org.machariel.core;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


public class SerializationManager<A> {
	private final Class<A> type;
	private final int size;
	
	public SerializationManager(Class<A> cl) {
		type = cl;
		size = Reflection.size(cl);
		
		fillMap();
	}
	
	private void fillMap() {
		Field[] fs = Reflection.getAllFields(this.type);
		for (Field f : fs) OFFSET.put(f.getName(), u.objectFieldOffset(f));
	}
	
	public long serialize(A o) {
		long ref = u.allocateMemory(size);
		long src = U.o2a(o);
		u.copyMemory(src + 8, ref, size);
		
		return ref;
	}
	
	public A deserialize(long ref) throws InstantiationException {
		Object o = u.allocateInstance(type);
		long dst = U.o2a(o);
		
		u.copyMemory(ref, dst + 8, size);
		
		return type.cast(o);
	}
	
	private final Map<String, Long> OFFSET = new ConcurrentHashMap<String, Long>();
	
	private static final Unsafe u = U.instance();
}
