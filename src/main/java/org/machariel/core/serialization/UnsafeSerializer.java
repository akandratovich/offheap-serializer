package org.machariel.core.serialization;

import java.lang.reflect.Array;

import org.machariel.core.ClassBucket;
import org.machariel.core.ClassMap;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;


// [int: size] [ptr: klass] [values]
public final class UnsafeSerializer {
  public static final long PTR_OFFSET = 2 * Unsafe.ARRAY_INT_INDEX_SCALE;
  private static final Unsafe u = U.instance();
  
  public static long serialize(Object object) {
    return serialize(object, 0);
  }
  
  public static long serialize(Object object, int depth) {
    if (object == null) return 0L;
    
    if (object.getClass().isArray()) return serialize0(object, depth);
    
    return serializecm(object, depth, ClassBucket.acquireMap(object.getClass()));
  }
  
  private static long serializecm(Object object, int depth, ClassMap cm) {
    final int size = Reflection.size(object);
    final long ref = u.allocateMemory(size + PTR_OFFSET);
    
    u.putInt(ref, size);
    u.putInt(ref + Unsafe.ARRAY_INT_INDEX_SCALE, object.getClass().hashCode());
    u.copyMemory(object, Reflection.MAGIC_SIZE, null, ref + PTR_OFFSET, size - Reflection.MAGIC_SIZE);
    
    for (int i : cm.refs()) {
      final long slot = ref + PTR_OFFSET + cm.offset(i) - Reflection.MAGIC_SIZE;
      if (depth > 0) {
        Object member = u.getObject(object, cm.offset(i));
        if (member != null) u.putAddress(slot, serialize(member, depth - 1));
        else u.setMemory(slot, Unsafe.ADDRESS_SIZE, (byte) 0);
      } else u.setMemory(slot, Unsafe.ADDRESS_SIZE, (byte) 0);
    }
    
    return ref;
  }
  
  // arrays
  private static long serialize0(Object object, int depth) {
    final int size = u.getInt(object, Reflection.MAGIC_SIZE);
    
    Class<?> array_class = object.getClass();
    ClassBucket.ensure(array_class);
    
    final int scale = u.arrayIndexScale(array_class);
    final int offset = u.arrayBaseOffset(array_class);
    final long ref = u.allocateMemory(size * scale + PTR_OFFSET);
    
    u.putInt(ref + Unsafe.ARRAY_INT_INDEX_SCALE, array_class.hashCode());
    
    boolean monotype = true && depth > 0;
    if (array_class.getComponentType().isPrimitive()) u.copyMemory(object, offset, null, ref + PTR_OFFSET, size * scale);
    else {
      if (depth > 0) {
        Class<?> in = null;
        Object[] _object = (Object[]) object;
        if (!array_class.getComponentType().isArray()) {
          for (Object m : _object) if (m != null && monotype) {
            if (in == null) in = m.getClass();
            else if (in != m.getClass()) {
              monotype = false;
              break;
            };
          }
        } else monotype = false;
        
        if (monotype && in != null) {
          ClassMap cm = ClassBucket.acquireMap(in);
          for (int i = 0; i < size; i++) {
            final Object member = _object[i];
            if (member != null) u.putAddress(ref + PTR_OFFSET + i * scale, serializecm(member, depth - 1, cm));
            else u.setMemory(ref + PTR_OFFSET + i * scale, scale, (byte) 0);
          }
        } else {
          for (int i = 0; i < size; i++) {
            final Object member = _object[i];
            if (member != null) u.putAddress(ref + PTR_OFFSET + i * scale, serialize(member, depth - 1));
            else u.setMemory(ref + PTR_OFFSET + i * scale, scale, (byte) 0);
          }
        }
      } else u.setMemory(ref + PTR_OFFSET, size * scale, (byte) 0);
    }
    
    u.putInt(ref, monotype ? -size : size);
    
    return ref;
  }
  
  public static Object deserialize(long ref) throws InstantiationException {
    return deserialize(ref, true);
  }
  
  public static Object deserialize(long ref, boolean free) throws InstantiationException {
    if (ref == 0L) return null;
    
    final int klass = u.getInt(ref + Unsafe.ARRAY_INT_INDEX_SCALE);
    ClassMap cm = ClassBucket.acquireMap(klass);
    if (cm.type().isArray()) return deserialize0(ref, free, cm.type());
    
    return deserializecm(ref, free, cm);
  }
  
  private static Object deserializecm(long ref, boolean free, ClassMap cm) throws InstantiationException {
    Object object = u.allocateInstance(cm.type());
    
    for (int i : cm.prims()) {
      final long fo = cm.offset(i);
      final long slot = ref + PTR_OFFSET + fo - Reflection.MAGIC_SIZE;
      Class<?> mtype = cm.field(i).getType();
      
      if (mtype == int.class) u.putInt(object, fo, u.getInt(slot));
      else if (mtype == boolean.class) u.putBoolean(object, fo, u.getByte(slot) > 0);
      else if (mtype == char.class) u.putChar(object, fo, u.getChar(slot));
      else if (mtype == byte.class) u.putByte(object, fo, u.getByte(slot));
      else if (mtype == short.class) u.putShort(object, fo, u.getShort(slot));
      else if (mtype == double.class) u.putDouble(object, fo, u.getDouble(slot));
      else if (mtype == float.class) u.putFloat(object, fo, u.getFloat(slot));
      else if (mtype == long.class) u.putLong(object, fo, u.getLong(slot));
    }
    
    for (int i : cm.refs()) {
      final long fo = cm.offset(i);
      final long slot = ref + PTR_OFFSET + fo - Reflection.MAGIC_SIZE;
      final long member_ptr = u.getAddress(slot);
      if (member_ptr != 0L) u.putObject(object, fo, deserialize(member_ptr, free));
    }
    
    // TODO remove
    if (free) free(ref);
    return object;
  }
  
  // arrays
  private static Object deserialize0(long ref, boolean free, Class<?> clazz) throws InstantiationException {
    int size = u.getInt(ref);
    final boolean monotype = size < 0;
    if (monotype) size = -size;
    
    Object object = Array.newInstance(clazz.getComponentType(), size);
    
    final int scale = u.arrayIndexScale(clazz);
    final int offset = u.arrayBaseOffset(clazz);
    
    if (clazz.getComponentType().isPrimitive()) u.copyMemory(null, ref + PTR_OFFSET, object, offset, size * scale);
    else {
      Object[] _object = (Object[]) object;
      if (monotype) {
        ClassMap cm = null;
        for (int i = 0; i < size; i++) {
          final long member_ptr = u.getAddress(ref + PTR_OFFSET + i * scale);
          if (member_ptr != 0L) {
            cm = ClassBucket.acquireMap(u.getInt(member_ptr + Unsafe.ARRAY_INT_INDEX_SCALE));
            i = size;
          }
        }
        
        for (int i = 0; i < size; i++) {
          final long member_ptr = u.getAddress(ref + PTR_OFFSET + i * scale);
          if (member_ptr != 0L) _object[i] = deserializecm(member_ptr, free, cm);
        }
      } else {
        for (int i = 0; i < size; i++) {
          final long member_ptr = u.getAddress(ref + PTR_OFFSET + i * scale);
          if (member_ptr != 0L) _object[i] = deserialize(member_ptr, free);
        }
      }
    }
    
    // TODO remove
    if (free) free(ref);
    return object;
  }
  
  public static void free(long ptr) {
    u.freeMemory(ptr);
  }
}