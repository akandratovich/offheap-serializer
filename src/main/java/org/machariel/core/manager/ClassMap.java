package org.machariel.core.manager;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;

import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

import sun.misc.Unsafe;

public class ClassMap {
  private final String[] name;
  private final long[] offset;
  private final Field[] field;
  private final int[] reference;
  
  public ClassMap(Class<?> t) {
    Field[] fs = Reflection.getAllFields(t).toArray(new Field[] {});
    name = new String[fs.length];
    offset = new long[fs.length];
    field = new Field[fs.length];
    
    for (int i = 0; i < fs.length; i++) name[i] = Reflection.name0(fs[i], t);
    Arrays.sort(name);
    
    int[] _reference = new int[fs.length];
    int j = 0;
    for (Field f : fs) {
      int i = Arrays.binarySearch(name, Reflection.name0(f, t));
      offset[i] = u.objectFieldOffset(f);
      field[i] = f;
      
      if (!Reflection.isPrimitive(f.getType())) _reference[j++] = i;
    }
    
    reference = Arrays.copyOf(_reference, j);
  }
  
  protected void assertType(String f, Class<?> type) throws NoSuchFieldException {
    assertType(index(f), type);
  }
  
  protected int[] referenceIndices() {
    return reference;
  }
  
  protected void assertReference(int i) {
    for (int ri : reference) if (i == ri) return;
    throw new InvalidParameterException();
  }
  
  protected void assertType(int i, Class<?> type) {
    if (field[i].getType() != type) throw new InvalidParameterException();
  }
  
  public long offset(String f) throws NoSuchFieldException {
    return offset(index(f));
  }
  
  protected long offset(int i) {
    return offset[i];
  }
  
  protected Field field(int i) {
    return field[i];
  }
  
  public boolean isPrimitive(String f) throws NoSuchFieldException {
    return reference.length == 0 || Reflection.isPrimitive(field[index(f)].getType());
  }
  
  protected int index(String f) throws NoSuchFieldException {
    int position = Arrays.binarySearch(name, f);
    if (position < 0) throw new NoSuchFieldException(f);
    
    return position;
  }
  
  private static final Unsafe u = U.instance();
}
