package org.machariel.core;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;

import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;

public final class ClassMap {
  private final Class<?> type;
  private final String[] name;
  private final long[] offset;
  private final Field[] field;
  private final int[] reference;
  private final int[] primitive;
  private final int length;
  
  protected ClassMap(Class<?> t) {
    type = t;
    
    Field[] fs = t.isArray() ? new Field[0] : Reflection.getAllFields(t).toArray(new Field[] {});
    name = new String[fs.length];
    offset = new long[fs.length];
    field = new Field[fs.length];
    
    for (int i = 0; i < fs.length; i++) name[i] = Reflection.name0(fs[i], t);
    Arrays.sort(name);
    
    int[] _reference = new int[fs.length];
    int[] _primitive = new int[fs.length];
    int k = 0;
    int j = 0;
    for (Field f : fs) {
      int i = Arrays.binarySearch(name, Reflection.name0(f, t));
      offset[i] = u.objectFieldOffset(f);
      field[i] = f;
      
      if (f.getType().isPrimitive()) _primitive[k++] = i;
      else _reference[j++] = i;
    }
    
    primitive = new int[k];
    reference = new int[j];
    System.arraycopy(_primitive, 0, primitive, 0, primitive.length);
    System.arraycopy(_reference, 0, reference, 0, reference.length);
    
    length = fs.length;
  }
  
  public Class<?> type() {
    return type;
  }
  
  public int length() {
    return length;
  }
  
  public int[] refs() {
    return reference;
  }
  
  public int[] prims() {
    return primitive;
  }
  
  public void assertType(String f, Class<?> type) throws NoSuchFieldException {
    assertType(index(f), type);
  }
  
  public void assertReference(int i) {
    if (field[i].getType().isPrimitive()) throw new InvalidParameterException();
  }
  
  public void assertType(int i, Class<?> type) {
    if (field[i].getType() != type) throw new InvalidParameterException();
  }
  
  public long offset(String f) throws NoSuchFieldException {
    return offset(index(f));
  }
  
  public long offset(int i) {
    return offset[i];
  }
  
  public Field field(int i) {
    return field[i];
  }
  
  public int index(String f) throws NoSuchFieldException {
    int position = Arrays.binarySearch(name, f);
    if (position < 0) throw new NoSuchFieldException(f);
    
    return position;
  }
  
  private static final Unsafe u = U.instance();
}
