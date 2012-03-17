package org.machariel.core.allocator;

import org.machariel.core.util.U;

public class Key {
  private final Key[] out;
  private long pointer;
  private final long size;
  private final boolean array;
  private final int klass;
  
  public Key(long ptr, long sz, int ref, boolean arr, int kl) {
    pointer = ptr;
    size = sz;
    out = new Key[ref];
    array = arr;
    klass = kl;
  }
  
  public Key(long ptr, long sz, int ref, boolean arr) {
    this(ptr, sz, ref, arr, 0);
  }
  
  public Key(long ptr, long sz, int ref) {
    this(ptr, sz, ref, false);
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

  public int klass() {
    return klass;
  }
  
  public boolean array() {
    return array;
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
}