package org.machariel.core.serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import org.machariel.core.PointerMigrationException;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


// [int: size] [ptr: klass] [values]
public class Serializer {
  private static final Unsafe u = U.instance();
  
  public static long serialize(Object object) {
    return serialize(object, 0);
  }
  
  public static long serialize(Object object, int depth) {
    if (object == null) return 0L;
    
    final int size = Reflection.size(object);
    if (size < 0) return serialize0(object, depth);
    
    final long klass = Reflection.klassPtr(object);
    final long _offset = Unsafe.ADDRESS_SIZE + Unsafe.ARRAY_INT_INDEX_SCALE;
    final long ref = u.allocateMemory(size + _offset);
    
    u.putInt(ref, size);
    u.putAddress(ref + Unsafe.ARRAY_INT_INDEX_SCALE, klass);
    u.copyMemory(object, Reflection.MAGIC_SIZE, null, ref + _offset, size - Reflection.MAGIC_SIZE);
    
    for (Field f : Reflection.getAllFields(object.getClass())) {
      if (!Reflection.isPrimitive(f.getType())) {
        final long slot = ref + _offset + u.objectFieldOffset(f) - Reflection.MAGIC_SIZE;
        if (depth > 0) {
          final long member_ptr = u.getAddress(slot);
          if (member_ptr != 0L) u.putAddress(slot, serialize(U.a2o(member_ptr), depth - 1));
          else u.setMemory(slot, Unsafe.ADDRESS_SIZE, (byte) 0);
        } else u.setMemory(slot, Unsafe.ADDRESS_SIZE, (byte) 0);
      }
    }
    
    return ref;
  }
  
  // arrays
  private static long serialize0(Object object, int depth) {
    final int size = u.getInt(object, Reflection.MAGIC_SIZE);
    final long klass = Reflection.klassPtr(object);
    final long _offset = Unsafe.ADDRESS_SIZE + Unsafe.ARRAY_INT_INDEX_SCALE;
    
    Class<?> array_class = object.getClass();
    
    final int scale = u.arrayIndexScale(array_class);
    final int offset = u.arrayBaseOffset(array_class);
    final long ref = u.allocateMemory(size * scale + _offset);
    
    u.putInt(ref, size);
    u.putAddress(ref + Unsafe.ARRAY_INT_INDEX_SCALE, klass);
    
    u.copyMemory(object, offset, null, ref + _offset, size * scale);
    if (!Reflection.isPrimitive(array_class.getComponentType())) {
      Object[] _object = (Object[]) object;
      if (depth > 0) {
        for (int i = 0; i < size; i++) {
          final Object member = _object[i];
          if (member != null) u.putAddress(ref + _offset + i * scale, serialize(member, depth - 1));
        }
      } else u.setMemory(ref + _offset, size * scale, (byte) 0);
    }
    
    return ref;
  }
  
  public static Object deserialize(long ref) throws PointerMigrationException, InstantiationException {
    if (ref == 0L) return null;
    
    final long klass = u.getAddress(ref + Unsafe.ARRAY_INT_INDEX_SCALE);
    if (!U.klass(klass)) throw new PointerMigrationException(klass);
    
    Class<?> clazz = U.clazz(klass);
    if (clazz.isArray()) return deserialize0(ref);
    
    final long _offset = Unsafe.ADDRESS_SIZE + Unsafe.ARRAY_INT_INDEX_SCALE;
    final int size = u.getInt(ref);
    
    Object object = u.allocateInstance(clazz);
    
//    u.monitorEnter(object);
    // TODO VERY BAD
    long a0 = U.o2a(object);
    u.copyMemory(ref + _offset, a0 + Reflection.MAGIC_SIZE, size - Reflection.MAGIC_SIZE);
    
//    long a1 = U.o2a(object);
//    u.monitorExit(object);
//    if (a0 != a1) {
//      System.out.println(a0 + " " + a1);
//    }
    
    for (Field f : Reflection.getAllFields(clazz)) {
      if (Reflection.isPrimitive(f.getType())) {
        
      } else {
        final long fo = u.objectFieldOffset(f);
        final long slot = ref + _offset + fo - Reflection.MAGIC_SIZE;
        final long member_ptr = u.getAddress(slot);
        if (member_ptr != 0L) u.putObject(object, fo, deserialize(member_ptr));
      }
    }
    
    // TODO remove
    free(ref);
    return object;
  }
  
  // arrays
  private static Object deserialize0(long ref) throws InstantiationException, PointerMigrationException {
    final int size = u.getInt(ref);
    final long klass = u.getAddress(ref + Unsafe.ARRAY_INT_INDEX_SCALE);
    
    final long _offset = Unsafe.ADDRESS_SIZE + Unsafe.ARRAY_INT_INDEX_SCALE;
    
    Class<?> clazz = U.clazz(klass);
    Object object = Array.newInstance(clazz.getComponentType(), size);
    
    final int scale = u.arrayIndexScale(clazz);
    final int offset = u.arrayBaseOffset(clazz);
    
    if (Reflection.isPrimitive(clazz.getComponentType())) u.copyMemory(null, ref + _offset, object, offset, size * scale);
    else {
      Object[] _object = (Object[]) object;
      for (int i = 0; i < size; i++) {
        final long slot0 = ref + _offset + i * scale;
        final long member_ptr = u.getAddress(slot0);
        if (member_ptr != 0L) _object[i] = deserialize(member_ptr);
      }
    }
    
    // TODO remove
    free(ref);
    return object;
  }
  
  public static void free(long ptr) {
    u.freeMemory(ptr);
  }
}