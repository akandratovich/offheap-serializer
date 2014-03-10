package kondratovich.offheap.core;

import java.util.concurrent.ConcurrentHashMap;

public final class ClassBucket {
  private ClassBucket() {}
  
  public static ClassMap acquireMap(int hash) {
    return bucket.get(hash);
  }
  
  public static ClassMap acquireMap(Class<?> cl) {
    if (bucket.containsKey(cl.hashCode())) return bucket.get(cl.hashCode());
    else {
      ClassMap cm = new ClassMap(cl);
      bucket.put(cl.hashCode(), cm);
      return cm;
    }
  }
  
  public static void ensure(Class<?> cl) { acquireMap(cl); }
  
  private static final ConcurrentHashMap<Integer, ClassMap> bucket = new ConcurrentHashMap<Integer, ClassMap>();
}