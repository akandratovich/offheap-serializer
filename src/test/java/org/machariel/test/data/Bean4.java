package org.machariel.test.data;

import java.util.Random;

public class Bean4 {
  public long _long0;
  public long _long1;
  public long _long2;
  
  public long _long;
  
  public long _long3;
  public long _long4;
  public long _long5;
  
  public Bean4 randomize() {
    Random r = new Random();

    _long = r.nextLong();
    
    return this;
  }
}