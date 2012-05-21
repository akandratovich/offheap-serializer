package org.machariel.core.allocator;

import org.machariel.core.ClassBucket;
import org.machariel.core.util.Reflection;
import org.machariel.core.util.U;

public class Key {
  private final Key[] out;
  private long pointer;
  private final long size;
  
  public Key(long ptr, long sz, int ref) {
    pointer = ptr;
    size = sz;
    out = new Key[ref];
  }
  
  public long size() {
    return size;
  }
  
  public long pointer() {
    return pointer;
  }
  
  public Key member(int i) {
    return out[i];
  }
  
  public int related() {
    return out.length;
  }
  
  public synchronized void member(int i, Key key) {
    out[i] = key;
  }

  public int klass(Allocator allocator) {
    return allocator.getInt(this, Reflection.ARRAY_INT_INDEX_SCALE);
  }
  
  public boolean array(Allocator allocator) {
    int klass = klass(allocator);
    return ClassBucket.acquireMap(klass).type().isArray();
  }
  
  public void free() {
    if (pointer() == 0L) return;
    for (int i = 0; i < related(); i++) {
      Key member = member(i);
      if (member != null) member.free();
    }
    
    free0();
  }
  
  private synchronized void free0() {
    final long ptr = pointer;
    pointer = 0L;
    
    U.instance().freeMemory(ptr);
  }
  
  public void check(long offset, long sz) {
    if (offset < 0 || offset + sz > size) throw ex;
  }
  
  private final RuntimeException ex = new AccessViolationException(pointer);
}