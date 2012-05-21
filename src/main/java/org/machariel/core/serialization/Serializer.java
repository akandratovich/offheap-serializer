package org.machariel.core.serialization;

import java.lang.reflect.Array;

import org.machariel.core.ClassBucket;
import org.machariel.core.ClassMap;
import org.machariel.core.allocator.Allocator;
import org.machariel.core.allocator.Key;
import org.machariel.core.util.Reflection;


// [int: size] [id: klass] [values]
public final class Serializer implements ISerializer {
  public static final long PTR_OFFSET = 2 * Reflection.ARRAY_INT_INDEX_SCALE;
  public static final Serializer DIRECT = new Serializer(Allocator.DIRECT);
  public static final Serializer CHECKED = new Serializer(Allocator.CHECKED);
  
  private final Allocator allocator;
  
  private Serializer(Allocator a) {
    allocator = a;
  }
  
  public Key serialize(Object object) {
    return serialize(object, 0);
  }
  
  public Key serialize(Object object, int depth) {
    if (object == null) return null;
    if (object.getClass().isArray()) return serialize0(object, depth);
    return serializecm(object, depth, ClassBucket.acquireMap(object.getClass()));
  }
  
  private Key serializecm(Object object, int depth, ClassMap cm) {
    final int size = Reflection.size(object);
    final Key ref = allocator.allocate(cm, size - Reflection.MAGIC_SIZE + PTR_OFFSET);
    
    allocator.put(ref, 0, size);
    allocator.put(ref, Reflection.ARRAY_INT_INDEX_SCALE, object.getClass().hashCode());
    allocator.copy(object, Reflection.MAGIC_SIZE, ref, PTR_OFFSET, size - Reflection.MAGIC_SIZE);
    
    for (int j = 0; j < cm.refs().length; j++) {
      final int i = cm.refs()[j];
      allocator.fill(ref, PTR_OFFSET - Reflection.MAGIC_SIZE + cm.offset(i), Reflection.ADDRESS_SIZE, (byte) 0);
      if (depth > 0) {
        Object member = Allocator.getObject(object, cm.offset(i));
        if (member != null) ref.member(j, serialize(member, depth - 1));
      }
    }
    
    return ref;
  }
  
  // arrays
  private Key serialize0(Object object, int depth) {
    final int size = Reflection.arraySize(object);
    
    Class<?> array_class = object.getClass();
    Class<?> ct = array_class.getComponentType();
    ClassBucket.ensure(array_class);
    
    final int scale = Reflection.indexScale(array_class);
    final int offset = Reflection.baseOffset(array_class);
    
    final Key ref = allocator.allocate(PTR_OFFSET + (ct.isPrimitive() ? size * scale : 0), ct.isPrimitive() ? 0 : size, true);
    
    boolean monotype = true && depth > 0;
    if (ct.isPrimitive()) allocator.copy(object, offset, ref, PTR_OFFSET, size * scale);
    else if (depth > 0) {
      Class<?> in = null;
      Object[] _object = (Object[]) object;
      if (!ct.isArray()) {
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
          if (member != null) ref.member(i, serializecm(member, depth - 1, cm));
        }
      } else {
        for (int i = 0; i < size; i++) {
          final Object member = _object[i];
          if (member != null) ref.member(i, serialize(member, depth - 1));
        }
      }
    }
    
    allocator.put(ref, 0, monotype ? -size : size);
    allocator.put(ref, Reflection.ARRAY_INT_INDEX_SCALE, array_class.hashCode());
    
    return ref;
  }
  
  public Object deserialize(Key ref) throws InstantiationException {
    if (ref.pointer() == 0L) return null;
    
    final int klass = ref.klass(allocator);
    ClassMap cm = ClassBucket.acquireMap(klass);
    if (ref.array(allocator)) return deserialize0(ref, cm.type());
    
    return deserializecm(ref, cm);
  }
  
  private Object deserializecm(Key ref, ClassMap cm) throws InstantiationException {
    Object object = allocator.allocate(cm.type());
    
    for (int i : cm.prims()) {
      final long offset = PTR_OFFSET - Reflection.MAGIC_SIZE + cm.offset(i);
      Class<?> mtype = cm.field(i).getType();
      
      if (mtype == int.class) Allocator.put(object, cm.offset(i), allocator.getInt(ref, offset));
      else if (mtype == boolean.class) Allocator.put(object, cm.offset(i), allocator.getBoolean(ref, offset));
      else if (mtype == char.class) Allocator.put(object, cm.offset(i), allocator.getChar(ref, offset));
      else if (mtype == byte.class) Allocator.put(object, cm.offset(i), allocator.getByte(ref, offset));
      else if (mtype == short.class) Allocator.put(object, cm.offset(i), allocator.getShort(ref, offset));
      else if (mtype == double.class) Allocator.put(object, cm.offset(i), allocator.getDouble(ref, offset));
      else if (mtype == float.class) Allocator.put(object, cm.offset(i), allocator.getFloat(ref, offset));
      else if (mtype == long.class) Allocator.put(object, cm.offset(i), allocator.getLong(ref, offset));
    }
    
    for (int j = 0; j < cm.refs().length; j++) {
      final int i = cm.refs()[j];
      final Key mkey = ref.member(j);
      if (mkey != null && mkey.pointer() != 0L) Allocator.put(object, cm.offset(i), deserialize(mkey));
    }
    
    return object;
  }
  
  // arrays
  private Object deserialize0(Key ref, Class<?> clazz) throws InstantiationException {
    int size = allocator.getInt(ref, 0);
    final boolean monotype = size < 0;
    if (monotype) size = -size;
    
    Class<?> ct = clazz.getComponentType();
    Object object = Array.newInstance(ct, size);
    
    if (ct.isPrimitive()) {
      final int scale = Reflection.indexScale(clazz);
      final int offset = Reflection.baseOffset(clazz);
      allocator.copy(ref, PTR_OFFSET, object, offset, size * scale);
    } else {
      Object[] _object = (Object[]) object;
      if (monotype) {
        ClassMap cm = null;
        for (int i = 0; i < size; i++) {
          final Key mkey = ref.member(i);
          if (mkey != null && mkey.pointer() != 0L && mkey.klass(allocator) != 0) {
            final int hash = mkey.klass(allocator);
            cm = ClassBucket.acquireMap(hash);
            i = size;
          }
        }
        
        for (int i = 0; i < size; i++) {
          final Key mkey = ref.member(i);
          if (mkey != null && mkey.pointer() != 0L) _object[i] = deserializecm(mkey, cm);
        }
      } else {
        for (int i = 0; i < size; i++) {
          final Key mkey = ref.member(i);
          if (mkey != null && mkey.pointer() != 0L) _object[i] = deserialize(mkey);
        }
      }
    }
    
    return object;
  }
}