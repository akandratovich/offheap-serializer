package org.machariel.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.ClassMap;
import org.machariel.core.access.ObjectAccessor;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class Performance {
  private static Random r = new Random();
  
  @Test
  public void test() throws Exception {
    List<Bean3> o = new ArrayList<Bean3>();
    for (int i = 0; i < 50; i++) o.add(new Bean3().randomize());
    
    serialization(o, "data: ArrayList<Bean3>(50)");
  }
  
  @Test
  public void test2() throws Exception {
    long[] o = new long[50];
    for (int i = 0; i < 50; i++) o[i] = r.nextLong();
    
    serialization(o, "data: long[50]");
  }
  
  @Test
  public void test3() throws Exception {
    int o = r.nextInt();
    serialization(o, "data: int");
  }
  
  @Test
  public void test4() throws Exception {
    Object o = new Bean0().randomize();
    serialization(o, "data: Bean0");
  }
  
  @Test
  public void object_access() throws Exception {
    Bean3 o[] = new Bean3[100];
    for (int i = 0; i < o.length; i++) o[i] = new Bean3();
    
    ObjectAccessor<Bean3> oa = new ObjectAccessor<Bean3>(Bean3.class);
    ClassMap cm = oa.getClassMap();
    
    long ref[] = new long[o.length];
    for (int i = 0; i < ref.length; i++) ref[i] = UnsafeSerializer.serialize(o[i], 5);
    
    int warm = 10 * 1000;
    int run = 20 * 1000;
    
    long[] getj = new long[run];
    long[] setj = new long[run];
    
    long[] get = new long[run];
    long[] set = new long[run];
    
    long[] geti = new long[run];
    long[] seti = new long[run];
    
    for (int i = 0; i < warm * 10; i++) oa.getLong(ref[i % ref.length], "_long");
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) oa.getLong(ref[k % ref.length], "_long");
      long g1 = Common.time();
      get[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm * 10; i++) oa.putLong(ref[i % ref.length], "_long", r.nextLong());
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) oa.putLong(ref[k % ref.length], "_long", r.nextLong());
      long g1 = Common.time();
      set[i] = (g1 - g0);
    }
    
    long[] tmp = new long[ref.length];
    for (int i = 0; i < warm * 10; i++) tmp[i % ref.length] = o[i % ref.length]._long;
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) tmp[k % ref.length] = o[k % ref.length]._long;
      long g1 = Common.time();
      getj[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm * 10; i++) o[i % ref.length]._long = r.nextLong();
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) o[k % ref.length]._long = r.nextLong();
      long g1 = Common.time();
      setj[i] = (g1 - g0);
    }
    
    int index = cm.index("_long");
    for (int i = 0; i < warm * 10; i++) oa.getLong(ref[i % ref.length], index);
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) oa.getLong(ref[k % ref.length], index);
      long g1 = Common.time();
      geti[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm * 10; i++) oa.putLong(ref[i % ref.length], index, r.nextLong());
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < 1000; k++) oa.putLong(ref[k % ref.length], index, r.nextLong());
      long g1 = Common.time();
      seti[i] = (g1 - g0);
    }
    
    Arrays.sort(get);
    Arrays.sort(set);
    
    Arrays.sort(geti);
    Arrays.sort(seti);
    
    int max = 11;
    System.out.println("field type: long");
    System.out.println("access latency by field/index/java: get (M rps)");
    for (int i = 1; i < max; i++) {
      int p = i * run / max;
      System.out.println(1e6 / get[p] + "\t" + 1e6 / geti[p] + "\t" + 1e6 / getj[p]);
    }
    
    System.out.println();
    System.out.println("access latency by field/index/java: set (M rps)");
    for (int i = 1; i < max; i++) {
      int p = i * run / max;
      System.out.println(1e6 / set[p] + "\t" + 1e6 / seti[p] + "\t" + 1e6 / setj[p]);
    }
    
    System.out.println();
    
    for (int i = 0; i < ref.length; i++) UnsafeSerializer.free(ref[i]);
  }
  
  private static void serialization(Object o, String message) throws Exception {
    int warm = 10 * 1000;
    int run = 20 * 1000;
    
    long[] unsafe = new long[run];
    long[] java = new long[run];
    long[] unsafe_d = new long[run];
    long[] java_d = new long[run];
    
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
    
    int max = 10;
    System.out.println(message);
    System.out.println("serialization throughput: unsafe java (entries per second)");
    for (int i = 0; i < max; i++) {
      int p = i * run / max;
      System.out.println(1e9 / unsafe[p] + "\t" + 1e6 / java[p] + "\t" + 1. * unsafe[p]/java[p]);
    }
    
    System.out.println();
    System.out.println("deserialization throughput: unsafe java (entries per second)");
    for (int i = 0; i < max; i++) {
      int p = i * run / max;
      System.out.println(1e9 / unsafe_d[p] + "\t" + 1e9 / java_d[p] + "\t" + 1. * unsafe_d[p]/java_d[p]);
    }
    
    System.out.println();
    System.out.println("time to serialization -> deserialization (nanoseconds)");
    System.out.println((u1 - u0)/run + " " + (j1 - j0)/run + " " + 1. * (u1 - u0)/(j1 - j0));
    System.out.println();
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
}
