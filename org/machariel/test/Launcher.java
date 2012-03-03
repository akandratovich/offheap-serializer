package org.machariel.test;

//import java.nio.ByteBuffer;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.machariel.core.SerializationManager;
//import org.machariel.core.util.Reflection;
//import org.machariel.core.util.U;
//import org.machariel.test.data.Bean;
//
//import sun.misc.Unsafe;


public class Launcher {
	public static void main(String[] args) {
//		SerializationManager<Bean> sm = new SerializationManager<Bean>(Bean.class);
//		
//		
//		T.o2a(sm);
////		T.o2a_2(sm);
//		
//		long q0 = System.nanoTime();
//		T.o2a(sm);
//		long q1 = System.nanoTime();
//		System.out.println(q1 - q0);
//
//		long q2 = System.nanoTime();
////		T.o2a_2(sm);
//		long q3 = System.nanoTime();
//		System.out.println(q3 - q2);
//		
//		System.exit(0);
//		
//		Bean b = new Bean();
//		
//		long a0 = System.nanoTime();
//		sm.deserialize(sm.serialize(b));
//		long a3 = System.nanoTime();
//		System.out.println(a3 - a0);
//		
//		
//		ByteBuffer bb = ByteBuffer.allocateDirect(1024);
//		long c = System.nanoTime();
//		bb.putLong(b._long);
//		bb.putDouble(b._double);
//		bb.putInt(b._int);
//		bb.putFloat(b._float);
//		bb.putShort(b._short);
//		bb.putChar(b._char);
//		bb.put(b._byte);
//		bb.putInt(b._boolean ? 1 : 0);
//		
//		bb.getLong();
//		bb.getDouble();
//		bb.getInt();
//		bb.getFloat();
//		bb.getShort();
//		bb.getChar();
//		bb.get();
//		bb.getInt();
//		
//		long d = System.nanoTime();
//		System.out.println(d - c);
//		
//		System.exit(0);
//		
//		Unsafe u = T.get();
////		t(u, 1);
//		t(u, new Bean());
//		
////		System.out.println(u.objectFieldOffset(Bean.class.getDeclaredField("_long")));
////		print_helper(u, new Bean[2]);
////		print_helper(u, new Bean());
//		
////		System.out.println(u.getAddress(u.getAddress(oa+4)+(12)));
	}
//	
//	private static void t(Unsafe u, Object o) {
//		long oa = T.o2a(o);
//		
//		int size = 20 * 4;
//		byte[] hex = new byte[size];
//		for (int i = 0; i < size; i++) hex[i] = u.getByte(oa + i);
//		System.out.println(U.hex(hex));
//		
//		System.out.println(u.getAddress(u.getAddress(oa+4)+(12)));
//		System.out.println(Reflection.size(o.getClass()));
//		
//		long ca = u.getAddress(oa+4);
//		for (int i = 0; i < size; i++) hex[i] = u.getByte(ca + i + 16);
//		System.out.println(U.hex(hex));
//		
//		//class insatnce offset
////		long offset = u.getAddress(ca + 16);
////		long cca = u.getAddress(ca + offset + 16 + 12);
////		System.out.println(cca);
//
//		long offset = u.getAddress(ca + 20);
////		long cca = u.getAddress(ca + offset + 16 + 12);
//		System.out.println(offset);
//		for (int i = 0; i < size; i++) hex[i] = u.getByte(offset + i);
//		System.out.println(U.hex(hex));
//		
////		System.out.println(T.a2o(cca));
//	}
//	
//	private static void print_helper(Unsafe u, Object o) {
//		int size = 1 * 4;
//		byte[] hex = new byte[size];
//		
//		long ca = u.getAddress(T.o2a(o)+4) + 12;
//		
//		System.out.println(u.getInt(ca));
//		
//		for (int i = 0; i < size; i++) hex[i] = u.getByte(ca + i);
//		System.out.println(U.hex(hex));
//	}
}