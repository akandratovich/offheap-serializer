package org.machariel.test.util;

import java.lang.reflect.Field;
import java.util.List;

import org.machariel.core.util.Reflection;

public final class Common {
	private Common() {}
	
	public static String hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : bytes) {
			sb.append(String.format("%1$02X ", b));
			if (i++ % 4 == 3) sb.append("\n");
		}

		return sb.toString();
	}
	
	public static String toString(Object o) throws IllegalArgumentException, IllegalAccessException {
	  StringBuilder sb = new StringBuilder();
	  List<Field> fs = Reflection.getAllFields(o.getClass());
	  for (Field f : fs) {
	    f.setAccessible(true);
	    sb.append("\n").append(f.getName()).append(" ").append(f.get(o));
	  }
	  
	  return sb.toString();
	}
	
  public static boolean equal(Object arg0, Object arg1) throws IllegalArgumentException, IllegalAccessException {
    if (arg0 == null && arg1 == null) return true;
    if (arg0 == null || arg1 == null) return false;
    if (arg0.getClass() != arg1.getClass()) return false;
    
    if (Reflection.isPrimitive(arg0.getClass())) return arg0 == arg1;
    
    List<Field> fs = Reflection.getAllFields(arg0.getClass());
    
    boolean value = true;
    for (Field f : fs) {
      f.setAccessible(true);
      
      if (Reflection.isPrimitive(f.getType())) {
        if (!f.get(arg0).equals(f.get(arg1)))
          return false;
      } else if (!equal(f.get(arg0), f.get(arg1)))
        return false;
    }
    
    return value;
  }
  
  public static boolean array_equal(Object[] arg0, Object[] arg1) throws IllegalArgumentException, IllegalAccessException {
    if (arg0 == null && arg1 == null) return true;
    if (arg0.length != arg1.length) return false;
    
    for (int i = 0; i < arg0.length; i++)
      if (!equal(arg0[i], arg1[i])) return false;
    
    return true;
  }
}