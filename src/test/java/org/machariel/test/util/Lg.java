package org.machariel.test.util;

import java.util.ArrayList;
import java.util.List;

public class Lg {
  private static final List<String> track = new ArrayList<String>();
  
  public static void clear() { track.clear(); }
  
  public static void put(String i) { track.add(i); }
  
  public static void dump() {
    for (String i : track) System.out.println(i);
  }
}