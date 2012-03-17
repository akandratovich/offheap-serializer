package org.machariel.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface Cache<K, V> extends ConcurrentMap<K, V> {
  public Set<K> ascendingKeySet();
  public Set<K> ascendingKeySet(int limit);
  public Map<K, V> ascendingMap();
  public Map<K, V> ascendingMap(int limit);
  public Set<K> descendingKeySet();
  public Set<K> descendingKeySet(int limit);
  public Map<K, V> descendingMap();
  public Map<K, V> descendingMap(int limit);
  
  public int weightedCapacity();
  public int capacity();
  public void setCapacity(int capacity);
}