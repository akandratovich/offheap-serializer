package kondratovich.offheap.core.serialization;

import kondratovich.offheap.core.allocator.Key;

public interface ISerializer {
  public Key serialize(Object object);
  public Key serialize(Object object, int depth);
  public Object deserialize(Key ref) throws InstantiationException;
}