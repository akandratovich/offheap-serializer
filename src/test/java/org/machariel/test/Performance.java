package org.machariel.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.ClassMap;
import org.machariel.core.access.ObjectAccessor;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean3;
import org.machariel.test.data.Bean4;
import org.machariel.test.util.Common;

public class Performance {
  private static Random r = new Random();
  
//  @Test
  public void test() throws Exception {
    List<Bean3> o = new ArrayList<Bean3>();
    for (int i = 0; i < 50; i++) o.add(new Bean3().randomize());
    
    serialization(o, "data: ArrayList<Bean3>(50)");
  }
  
//  @Test
  public void test2() throws Exception {
    long[] o = new long[50];
    for (int i = 0; i < 50; i++) o[i] = r.nextLong();
    
    serialization(o, "data: long[50]");
  }
  
//  @Test
  public void test3() throws Exception {
    int o = r.nextInt();
    serialization(o, "data: int");
  }
  
//  @Test
  public void test4() throws Exception {
    Object o = new Bean0().randomize();
    serialization(o, "data: Bean0");
  }
  
//  @Test
  public void object_access() throws Exception {
    object_access(400, "field type: long (L1 cache)");
    object_access(1000, "field type: long (L2 cache)");
    object_access(100000, "field type: long (off cached)");
  }
  
  public void object_access(int count, String message) throws Exception {
    Bean4 o[] = new Bean4[count];
    for (int i = 0; i < o.length; i++) o[i] = new Bean4();
    
    ObjectAccessor<Bean4> oa = new ObjectAccessor<Bean4>(Bean4.class, false);
    ClassMap cm = oa.getClassMap();
    
    Key ref[] = new Key[count];
    for (int i = 0; i < ref.length; i++) ref[i] = Serializer.DIRECT.serialize(o[i], 5);
    
    int warm = 10 * 1000 + count;
    int run = 2000;
    
    long[] getj = new long[run];
    long[] setj = new long[run];
    
    long[] get = new long[run];
    long[] set = new long[run];
    
    long[] geti = new long[run];
    long[] seti = new long[run];
    
    long[] tmp = new long[ref.length];
    for (int i = 0; i < warm; i++) tmp[i % ref.length] = oa.getLong(ref[i % ref.length], "_long");
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) tmp[k] = oa.getLong(ref[k], "_long");
      long g1 = Common.time();
      get[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm; i++) oa.putLong(ref[i % ref.length], "_long", r.nextLong());
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) oa.putLong(ref[k], "_long", g0);
      long g1 = Common.time();
      set[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm; i++) tmp[i % ref.length] = o[i % ref.length]._long;
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) tmp[k] = o[k]._long;
      long g1 = Common.time();
      getj[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm; i++) o[i % ref.length]._long = r.nextLong();
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) o[k]._long = g0;
      long g1 = Common.time();
      setj[i] = (g1 - g0);
    }
    
    int index = cm.index("_long");
    for (int i = 0; i < warm; i++) tmp[i % ref.length] = oa.getLong(ref[i % ref.length], index);
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) tmp[k] = oa.getLong(ref[k], index);
      long g1 = Common.time();
      geti[i] = (g1 - g0);
    }
    
    for (int i = 0; i < warm; i++) oa.putLong(ref[i % ref.length], index, r.nextLong());
    for (int i = 0; i < run; i++) {
      long g0 = Common.time();
      for (int k = 0; k < count; k++) oa.putLong(ref[k], index, g0);
      long g1 = Common.time();
      seti[i] = (g1 - g0);
    }
    
    Arrays.sort(get);
    Arrays.sort(set);
    
    Arrays.sort(geti);
    Arrays.sort(seti);
    
    Arrays.sort(getj);
    Arrays.sort(setj);
    
    int max = 11;
    System.out.println(message);
    System.out.println("access latency by field/index/java: get (M rps)");
    for (int i = 1; i < max; i++) {
      int p = i * run / max;
      System.out.println(String.format("%1$7.2f %2$7.2f %3$7.2f", (count * 1000.) / get[p], (count * 1000.) / geti[p], (count * 1000.) / getj[p]));
    }
    
    System.out.println();
    System.out.println("access latency by field/index/java: set (M rps)");
    for (int i = 1; i < max; i++) {
      int p = i * run / max;
      System.out.println(String.format("%1$7.2f %2$7.2f %3$7.2f", (count * 1000.) / set[p], (count * 1000.) / seti[p], (count * 1000.) / setj[p]));
    }
    
    System.out.println();
    
    for (int i = 0; i < ref.length; i++) ref[i].free();
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
  
  public static void perf_unsafe(Object o0, boolean show, int run, long[] serialize, long[] deserialize) throws Exception {
    long us0 = System.nanoTime();
    Key ref = Serializer.DIRECT.serialize(o0, 5);
    long us1 = System.nanoTime();
    
    long ud0 = System.nanoTime();
    Serializer.DIRECT.deserialize(ref);
    long ud1 = System.nanoTime();
    
    if (show) {
      serialize[run]       = (us1 - us0);
      deserialize[run]     = (ud1 - ud0);
    }
    
    ref.free();
  }
  
  public static void perf_java(Object o0, boolean show, int run, long[] serialize, long[] deserialize) throws Exception {
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
