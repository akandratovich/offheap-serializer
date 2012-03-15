package org.machariel.test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.core.util.Reflection;
import org.machariel.test.Extends;
import org.machariel.test.NullReference;
import org.machariel.test.Primitive;
import org.machariel.test.RawArray;
import org.machariel.test.Reference;
import org.machariel.test.ReferenceArray;
import org.machariel.test.ZeroDepth;
import org.machariel.test.data.*;

public class Launcher {
  public static void main(String[] args) throws Exception {
    
    System.out.println("magic: " + Reflection.MAGIC_SIZE);
    System.out.println("oopsz: " + Reflection.OOP_SIZE);
    System.out.println("overb: " + Reflection.OVERBOOK);
    
    Class<?>[] pack = classes("org.machariel.test");
    for (Class<?> cl : pack) {
//      System.out.println("          " + cl);
      Object instance = cl.newInstance();
//      System.out.println("          " + instance);
      for (Method m : cl.getDeclaredMethods()) {
//        System.out.println("          " + m);
        if (m.getAnnotation(Test.class) != null) run(instance, m, 100000);
      }
    }
    
    System.out.println("finish");
    
//    System.exit(0);
//    run(new ReferenceArray(), ReferenceArray.class.getDeclaredMethod("testGenericArray2"), 1000);
    
    
//    for (int i = 0; i < 10 * 1000; i++) {
//      long m0 = System.nanoTime();
//      ObjectStreamClass osc = ObjectStreamClass.lookup(Bean1.class);
//      long m1 = System.nanoTime();
//      
//      long m2 = System.nanoTime();
//      ClassMap cm = ClassMap.acquire(Bean1.class);
//      long m3 = System.nanoTime();
//    }
//    
//    long m0 = System.nanoTime();
//    ObjectStreamClass osc = ObjectStreamClass.lookup(Bean1.class);
//    long m1 = System.nanoTime();
//    
//    long m2 = System.nanoTime();
//    ClassMap cm = ClassMap.acquire(Bean1.class);
//    long m3 = System.nanoTime();
//    
//    System.out.println((m1 - m0) + " " + (m3 - m2));
//    
//    System.exit(0);
    
//    Random r = new Random();

//     Bean1 o0 = new Bean1();

//    Object[] o = new Object[r.nextInt(100) + 1];
//     Object[] o = new Object[50];
//     for (int i = 0; i < o.length; i++) o[i] = new Object();
//    for (int i = 0; i < o.length; i++) o[i] = r.nextBoolean() ? new Bean1().randomize() : new Bean3().randomize();
//     for (int i = 0; i < o.length; i++) o[i] = new Bean1();
    
//    int[] o1 = new int[r.nextInt(100) + 1];
//    for (int i = 0; i < o1.length; i++) o1[i] = r.nextInt();
    
//    perf(o);
//    System.out.println(o.getClass() + " [" + o.length + "]");
  }
  
  private static void perf(Object o) throws InstantiationException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, IOException {
    int warm = 20 * 1000;
    int run = 10 * 1000;
    
    long[] unsafe = new long[run];
    long[] java = new long[run];
    long[] bytebuffer = new long[run];
    long[] unsafe_d = new long[run];
    long[] java_d = new long[run];
    long[] bytebuffer_d = new long[run];
    
//    for (int i = 0; i < warm; i++) perf_hybrid(o, false, i, bytebuffer, bytebuffer_d);
//    for (int i = 0; i < run; i++) perf_hybrid(o, true, i, bytebuffer, bytebuffer_d);
//    
//    System.gc();
    
    for (int i = 0; i < warm; i++) perf_unsafe(o, false, i, unsafe, unsafe_d);
    
    long u0 = System.nanoTime();
    for (int i = 0; i < run; i++) perf_unsafe(o, true, i, unsafe, unsafe_d);
    long u1 = System.nanoTime();
    
    System.gc();
    
    for (int i = 0; i < warm; i++) perf_java(o, false, i, java, java_d);
    
    long j0 = System.nanoTime();
    for (int i = 0; i < run; i++) perf_java(o, true, i, java, java_d);
    long j1 = System.nanoTime();
    
    Arrays.sort(unsafe);
    Arrays.sort(unsafe_d);
    
    Arrays.sort(java);
    Arrays.sort(java_d);
    
    Arrays.sort(bytebuffer);
    Arrays.sort(bytebuffer_d);
    
    int max = 10;
    
    System.out.println("serialization: unsafe java bytebuffer");
    for (int i = 0; i < max; i++) {
      int p = i * run / max;
      System.out.println(unsafe[p]/1000. + "\t" + java[p]/1000. + "\t" + 1. * unsafe[p]/java[p]);
    }
    
    System.out.println();
    System.out.println("deserialization: unsafe java bytebuffer");
    for (int i = 0; i < max; i++) {
      int p = i * run / max;
      System.out.println(unsafe_d[p]/1000. + "\t" + java_d[p]/1000. + "\t" + 1. * unsafe_d[p]/java_d[p]);
    }
    
    System.out.println();
    System.out.println((u1 - u0)/run + " " + (j1 - j0)/run + " " + 1. * (u1 - u0)/(j1 - j0));
  }
  
  private static void run(Object object, Method method, int count) {
    int i = 0;
    try {
      for (i = 0; i < count; i++) {
        Lg.clear();
        method.invoke(object);
      }
      System.out.println("successed: " + method.getDeclaringClass().getCanonicalName() + "#" + method.getName());
    } catch (Exception e) {
      e.getCause().printStackTrace();
      Lg.dump();
      System.out.println(i + " failed:    " + method.getDeclaringClass().getCanonicalName() + "#" + method.getName());
    }
  }
  
  private static Class<?>[] classes(String pack) throws ClassNotFoundException {
    List<Class<?>> back = new ArrayList<Class<?>>();
    back.add(Extends.class);
    back.add(NullReference.class);
    back.add(Primitive.class);
    back.add(RawArray.class);
    back.add(ReferenceArray.class);
    back.add(Reference.class);
    back.add(ZeroDepth.class);
    
//    String root = new File(".").getAbsolutePath() + "/src/test/java";
//    for (String path :  pack.split("\\.")) root += "/" + path;
//    
//    File pkg = new File(root);
//    for (File cl : pkg.listFiles()) {
//      if (cl.getName().endsWith(".java")) {
//        String name = pack + "." + cl.getName().split("\\.")[0];
//        back.add(Class.forName(name));
//      }
//    }
//    
    return back.toArray(new Class[] {});
  }
  
  public static String printArray(Object[] obj) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object o : obj) sb.append(o + ", ");
    sb.append("]");
    return sb.toString();
  }
  
  public static void perf_unsafe(Object o0, boolean show, int run, long[] serialize, long[] deserialize) throws IOException, InstantiationException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
    long us0 = System.nanoTime();
    long ref = UnsafeSerializer.serialize(o0, 5);
    long us1 = System.nanoTime();
    
    long ud0 = System.nanoTime();
    UnsafeSerializer.deserialize(ref);
    long ud1 = System.nanoTime();
    
    if (show) {
      serialize[run]       = (us1 - us0);
      deserialize[run]     = (ud1 - ud0);
    }
  }
  
  public static void perf_java(Object o0, boolean show, int run, long[] serialize, long[] deserialize) throws IOException, InstantiationException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    
    long js0 = System.nanoTime();
    oos.writeObject(o0);
    byte[] data = baos.toByteArray();
    long js1 = System.nanoTime();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ObjectInputStream ois = new ObjectInputStream(bais);
    
    long jd0 = System.nanoTime();
    ois.readObject();
    long jd1 = System.nanoTime();
    
    if (show) {
      serialize[run]         = (js1 - js0);
      deserialize[run]       = (jd1 - jd0);
    }
  }
  
  public static void perf_hybrid(Object o0, boolean show, int run, long[] serialize, long[] deserialize) throws IOException, InstantiationException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    
    long js0 = System.nanoTime();
    oos.writeObject(o0);
    byte[] data = baos.toByteArray();
    long ref = UnsafeSerializer.serialize(data);
    long js1 = System.nanoTime();
    
    byte[] data1 = (byte[]) UnsafeSerializer.deserialize(ref);
    ByteArrayInputStream bais = new ByteArrayInputStream(data1);
    ObjectInputStream ois = new ObjectInputStream(bais);
    
    long jd0 = System.nanoTime();
    ois.readObject();
    long jd1 = System.nanoTime();
    
    if (show) {
      serialize[run]         = (js1 - js0);
      deserialize[run]       = (jd1 - jd0);
    }
  }
}