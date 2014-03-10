package kondratovich.offheap.core.allocator;

import kondratovich.offheap.core.ClassMap;


public interface IAllocator {
  public void put(Key ref, long offset, int value);
  public void put(Key ref, long offset, double value);
  public void put(Key ref, long offset, short value);
  public void put(Key ref, long offset, long value);
  public void put(Key ref, long offset, float value);
  public void put(Key ref, long offset, boolean value);
  public void put(Key ref, long offset, byte value);
  public void put(Key ref, long offset, char value);
  
  public int getInt(Key ref, long offset);
  public double getDouble(Key ref, long offset);
  public short getShort(Key ref, long offset);
  public long getLong(Key ref, long offset);
  public float getFloat(Key ref, long offset);
  public boolean getBoolean(Key ref, long offset);
  public char getChar(Key ref, long offset);
  public byte getByte(Key ref, long offset);
  
  public void copy(Object o, long offset0, Key ref, long offset1, long size);
  public void copy(Key o, long offset0, Object ref, long offset1, long size);
  
  public void fill(Key ref, long offset0, long size, byte value);
  
  public Key allocate(ClassMap cm, long size);
  public Key allocate(long size);
  public Key allocate(long size, int len, boolean array);
  public Object allocate(Class<?> type) throws InstantiationException;
}