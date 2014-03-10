package kondratovich.offheap.test.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import kondratovich.offheap.test.*;

import org.junit.Test;

public class Launcher {
  public static void main(String[] args) throws Exception {
    Class<?>[] pack = classes();
    for (Class<?> cl : pack) {
      Object instance = cl.newInstance();
      for (Method m : cl.getDeclaredMethods()) {
        if (m.getAnnotation(Test.class) != null) run(instance, m, 100000);
      }
    }
    
    System.out.println("finish");
  }
  
  private static void run(Object object, Method method, int count) {
    int i = 0;
    try {
      for (i = 0; i < count; i++) {
        method.invoke(object);
      }
      System.out.println("successed: " + method.getDeclaringClass().getCanonicalName() + "#" + method.getName());
    } catch (Exception e) {
      e.getCause().printStackTrace();
      System.out.println(i + " failed:    " + method.getDeclaringClass().getCanonicalName() + "#" + method.getName());
    }
  }
  
  private static Class<?>[] classes() throws ClassNotFoundException {
    List<Class<?>> back = new ArrayList<Class<?>>();
    back.add(Extends.class);
    back.add(NullReference.class);
    back.add(Primitive.class);
    back.add(RawArray.class);
    back.add(ReferenceArray.class);
    back.add(Reference.class);
    back.add(ZeroDepth.class);
    
    return back.toArray(new Class[] {});
  }
}